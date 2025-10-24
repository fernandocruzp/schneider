export interface Activity {
  id?: number;
  titulo: string;
  descripcion: string;
  fechaCreacion: string;
  fechaVencimiento: string;
  completada?: boolean;
  fechaActualizacion?: string;
  usuarioId?: number;
  usuarioUsername?: string;
}

export interface ActivityRequest {
  titulo: string;
  descripcion: string;
  fechaInicio: string;
  fechaFin: string;
  horaInicio?: string;
  horaFin?: string;
}

export interface ActivityReq{
  titulo: string;
  descripcion: string;
  fechaVencimiento: string;
  fechaCreacion: string;
}

export enum ActivityStatus {
  PENDING = 'Pendiente',
  IN_PROGRESS = 'En Progreso',
  COMPLETED = 'Completado'
}
