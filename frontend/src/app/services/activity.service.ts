import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Activity, ActivityRequest, ActivityReq } from '../models/activity.model';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class ActivityService {
  private apiUrl = 'http://localhost:8080/api/actividades';

  constructor(private http: HttpClient, private authService: AuthService) {}

  private getHeaders(): HttpHeaders {
    const token = this.authService.getToken();
    console.log(token);
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    });
  }

  getActivities(): Observable<Activity[]> {
    return this.http.get<Activity[]>(this.apiUrl, { headers: this.getHeaders() });
  }

  createActivity(activity: ActivityReq): Observable<Activity> {
    console.log(activity);
    console.log(this.getHeaders().get('Authorization'));
    return this.http.post<Activity>(this.apiUrl, activity, { headers: this.getHeaders() });
  }

  updateActivity(id: number, activity: ActivityRequest): Observable<Activity> {
    return this.http.put<Activity>(`${this.apiUrl}/${id}`, activity, { headers: this.getHeaders() });
  }

  deleteActivity(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`, { headers: this.getHeaders() });
  }
}
