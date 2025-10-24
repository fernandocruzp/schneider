import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';
import { LoginRequest, RegisterRequest, AuthResponse, User } from '../models/user.model';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/api/auth';
  private userApiUrl = 'http://localhost:8080/api/users';
  private currentUserSubject = new BehaviorSubject<any>(null);
  public currentUser$ = this.currentUserSubject.asObservable();

  constructor(private http: HttpClient) {
    const token = localStorage.getItem('token');
    const email = localStorage.getItem('email');
    const nombre = localStorage.getItem('nombre');
    const apellido = localStorage.getItem('apellido');
    if (token && email) {
      this.currentUserSubject.next({ token, email, nombre, apellido });
    }
  }

  login(credentials: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/signin`, credentials);
  }

  register(userData: RegisterRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/signup`, userData);
  }

  getCurrentUser(): Observable<User> {
    const token = this.getToken();
    const headers = { 'Authorization': `Bearer ${token}` };
    return this.http.get<User>(`${this.userApiUrl}/me`, { headers });
  }

  setCurrentUser(authResponse: AuthResponse) {
    localStorage.setItem('token', authResponse.token);
    this.currentUserSubject.next(authResponse);
    
    // Fetch user details after setting token
    this.getCurrentUser().subscribe({
      next: (user) => {
        console.log('User details from /me endpoint:', user);
        localStorage.setItem('email', user.email);
        localStorage.setItem('nombre', user.nombre);
        localStorage.setItem('apellido', user.apellido);
        this.currentUserSubject.next({ ...authResponse, ...user });
      },
      error: (error) => {
        console.log('Error fetching user details:', error);
      }
    });
  }

  logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('email');
    localStorage.removeItem('nombre');
    localStorage.removeItem('apellido');
    this.currentUserSubject.next(null);
  }

  isAuthenticated(): boolean {
    return !!localStorage.getItem('token');
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }
}
