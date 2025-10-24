# Task Management Frontend

Angular frontend for the task management application.

## Features

- User registration with First Name, Last Name, Email, and Password
- Login using email as username
- Dashboard with user info in header
- CRUD operations for activities with:
  - Name
  - Date and time (start/end with calculated hours)
  - Comments
  - Status tracking

## Setup

1. Install dependencies:
```bash
npm install
```

2. Start development server:
```bash
ng serve
```

3. Build for production:
```bash
ng build
```

## Backend Integration

The frontend connects to the Spring Boot backend at `http://localhost:8080/api`

Make sure the backend is running before starting the frontend.

## Usage

1. Register a new account or login with existing credentials
2. View and manage your personal activities
3. Add, edit, or delete activities
4. Track working hours automatically calculated from start/end times
