import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { ActivityService } from '../../services/activity.service';
import { Activity, ActivityRequest, ActivityStatus } from '../../models/activity.model';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  currentUser: any;
  activities: Activity[] = [];
  showForm = false;
  editingActivity: Activity | null = null;
  
  newActivity: ActivityRequest = {
    titulo: '',
    descripcion: '',
    fechaInicio: '',
    fechaFin: '',
    horaInicio: '',
    horaFin: ''
  };

  ActivityStatus = ActivityStatus;

  constructor(
    private authService: AuthService,
    private activityService: ActivityService,
    private router: Router
  ) {}

  ngOnInit() {
    this.authService.currentUser$.subscribe(user => {
      this.currentUser = user;
      if (!user) {
        this.router.navigate(['/login']);
      }
    });
    this.loadActivities();
  }

  loadActivities() {
    this.activityService.getActivities().subscribe({
      next: (activities) => {
        this.activities = activities;
      },
      error: (error) => {
        console.error('Error cargando actividades:', error);
      }
    });
  }

  onSubmit() {
    // Combine date and time for backend

    console.log("aaaaaa");
    const activityData = {
      titulo: this.newActivity.titulo,
      descripcion: this.newActivity.descripcion,
      fechaCreacion: `${this.newActivity.fechaInicio}T${this.newActivity.horaInicio}:00`,
      fechaVencimiento: `${this.newActivity.fechaFin}T${this.newActivity.horaFin}:00`
    };
    console.log(activityData);
    if (this.currentUser) {
      console.log(this.currentUser.id)
      this.activityService.createActivity(activityData).subscribe({
        next: () => {
          this.loadActivities();
          this.resetForm();
        },
        error: (error) => {
          console.error('Error actualizando actividad:', error);
        }
      });
    } else {
      this.activityService.createActivity(activityData).subscribe({
        next: () => {
          this.loadActivities();
          this.resetForm();
        },
        error: (error) => {
          console.error('Error creando actividad:', error);
        }
      });
    }
  }

  editActivity(activity: Activity) {
    this.editingActivity = activity;
    
    // Split datetime into date and time parts
    const startDate = new Date(activity.fechaCreacion);
    const endDate = new Date(activity.fechaVencimiento);
    
    this.newActivity = {
      titulo: activity.titulo,
      descripcion: activity.descripcion,
      fechaInicio: startDate.toISOString().split('T')[0],
      fechaFin: endDate.toISOString().split('T')[0],
      horaInicio: startDate.toTimeString().slice(0, 5),
      horaFin: endDate.toTimeString().slice(0, 5)
    };
    this.showForm = true;
  }

  deleteActivity(id: number) {
    if (confirm('¿Estás seguro de que quieres eliminar esta actividad?')) {
      this.activityService.deleteActivity(id).subscribe({
        next: () => {
          this.loadActivities();
        },
        error: (error) => {
          console.error('Error eliminando actividad:', error);
        }
      });
    }
  }

  resetForm() {
    this.newActivity = { 
      titulo: '', 
      descripcion: '', 
      fechaInicio: '', 
      fechaFin: '', 
      horaInicio: '', 
      horaFin: '' 
    };
    this.editingActivity = null;
    this.showForm = false;
  }

  calculateHours(start: string, end: string): number {
    const startTime = new Date(start);
    const endTime = new Date(end);
    return Math.abs(endTime.getTime() - startTime.getTime()) / (1000 * 60 * 60);
  }

  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
