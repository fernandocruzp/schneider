import { Component, OnInit } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [FormsModule, CommonModule, RouterLink],
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {
  userData = {
    nombre: '',
    apellido: '',
    email: '',
    password: ''
  };
  error = '';

  constructor(private authService: AuthService, private router: Router) {}

  ngOnInit() {
    if (this.authService.isAuthenticated()) {
      this.router.navigate(['/dashboard']);
    }
  }

  onSubmit() {
    const registerRequest = {
      nombre: this.userData.nombre,
      apellido: this.userData.apellido,
      email: this.userData.email,
      password: this.userData.password
    };

    this.authService.register(registerRequest).subscribe({
      next: (response) => {
        this.authService.setCurrentUser(response);
        this.router.navigate(['/dashboard']);
      },
      error: (error) => {
        if (error.status === 400 || error.status === 409) {
          this.error = 'Usuario ya registrado';
        } else {
          this.error = 'Error al registrar';
        }
      }
    });
  }
}
