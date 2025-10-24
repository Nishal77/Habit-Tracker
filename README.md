# Habit Tracker - Full Stack Application

A modern habit tracking application built with React (Vite + Tailwind CSS) frontend and Spring Boot backend, using Supabase as the database.

## Features

- ✅ Create and manage habits
- ✅ Track daily completion
- ✅ View streak counts and progress
- ✅ Gamified badges system
- ✅ Responsive design with Tailwind CSS
- ✅ RESTful API with Spring Boot
- ✅ PostgreSQL database with Supabase

## Tech Stack

### Frontend
- React 19 with Vite
- Tailwind CSS for styling
- React Router for navigation
- Axios for API calls

### Backend
- Spring Boot 3.2.0
- Spring Data JPA
- PostgreSQL (Supabase)
- Maven for dependency management

### Database
- Supabase (PostgreSQL)

## Project Structure

```
Habit tracker/
├── frontend/                 # React application
│   ├── src/
│   │   ├── components/      # React components
│   │   │   ├── Home.jsx
│   │   │   ├── Dashboard.jsx
│   │   │   └── AddHabit.jsx
│   │   ├── App.jsx          # Main app component
│   │   └── main.jsx         # Entry point
│   └── package.json
├── backend/                 # Spring Boot application
│   ├── src/main/java/com/habittracker/
│   │   ├── controller/      # REST controllers
│   │   ├── service/         # Business logic
│   │   ├── repository/      # Data access layer
│   │   ├── entity/          # JPA entities
│   │   ├── dto/             # Data transfer objects
│   │   └── config/          # Configuration
│   └── pom.xml
└── README.md
```

## Setup Instructions

### Prerequisites
- Node.js (v18 or higher)
- Java 17 or higher
- Maven 3.6 or higher
- Supabase account

### 1. Database Setup

**Option A: H2 In-Memory Database (Default - Ready to Use)**
- The application is configured to use H2 in-memory database by default
- No setup required - just run the backend and it will work
- Data will be lost when the application stops (good for development)

**Option B: Supabase PostgreSQL (Production)**
1. Create a new project in [Supabase](https://supabase.com)
2. Go to Settings > Database
3. Copy your connection details:
   - Host: `db.your-project-ref.supabase.co`
   - Database: `postgres`
   - Username: `postgres`
   - Password: (your database password)
   - Port: `5432`
4. Update `backend/src/main/resources/application.properties`:
   - Comment out H2 configuration lines
   - Uncomment and update Supabase configuration lines

### 2. Backend Setup

1. Navigate to the backend directory:
   ```bash
   cd backend
   ```

2. (Optional) Update `src/main/resources/application.properties` with your Supabase credentials if you want to use PostgreSQL instead of H2:
   ```properties
   # Comment out H2 config and uncomment these lines:
   spring.datasource.url=jdbc:postgresql://db.your-project-ref.supabase.co:5432/postgres
   spring.datasource.username=postgres
   spring.datasource.password=your-password-here
   ```

3. Build and run the backend:
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

   The backend will be available at `http://localhost:8080`

### 3. Frontend Setup

1. Navigate to the frontend directory:
   ```bash
   cd frontend
   ```

2. Install dependencies:
   ```bash
   npm install
   ```

3. Start the development server:
   ```bash
   npm run dev
   ```

   The frontend will be available at `http://localhost:5173` (or `http://localhost:5174` if 5173 is in use)

## Access URLs

- **Frontend**: http://localhost:5173 (or 5174 if 5173 is in use)
- **Backend**: http://localhost:8080
- **API**: http://localhost:8080/api/habits/1
- **H2 Database Console**: http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:mem:testdb`
  - Username: `sa`
  - Password: `password`

## API Endpoints

### Habits
- `GET /api/habits/{userId}` - Get all habits for a user
- `POST /api/habits` - Create a new habit
- `GET /api/habits/habit/{id}` - Get habit by ID
- `PUT /api/habits/{id}` - Update habit
- `DELETE /api/habits/{id}` - Delete habit

### Habit Logs
- `POST /api/habits/{id}/log` - Mark habit as done for today
- `GET /api/habits/{id}/progress` - Get habit progress and streak
- `GET /api/habits/{id}/logs` - Get all logs for a habit
- `GET /api/habits/{id}/today` - Get today's log for a habit

## Usage

1. **Home Page**: Welcome screen with navigation to dashboard
2. **Dashboard**: View all your habits with progress and streak information
3. **Add Habit**: Create new habits with name, description, and daily goal
4. **Mark Done**: Click the "Mark Done" button to complete a habit for the day
5. **Badges**: Earn badges based on streak milestones:
   - 🌱 Newbie (0-2 days)
   - ⭐ Getting Started (3-6 days)
   - 🔥 Streak Master (7-13 days)
   - 🏆 Champion (14-29 days)
   - 👑 Legend (30+ days)

## Development

### Frontend Development
- Uses React 19 with modern JSX (no React import needed)
- Tailwind CSS for styling
- React Router for navigation
- Axios for API communication

### Backend Development
- Spring Boot with RESTful APIs
- JPA entities for database mapping
- Service layer for business logic
- Repository pattern for data access
- CORS configured for frontend integration

## Troubleshooting

### Common Issues

1. **React Import Error**: Fixed by adding explicit React imports to all components
2. **CORS Issues**: Backend now allows both `http://localhost:5173` and `http://localhost:5174`
3. **Database Connection**: Verify your Supabase credentials in `application.properties`
4. **Port Conflicts**: Frontend will automatically use port 5174 if 5173 is in use

### Database Schema

The application will automatically create the following tables:
- `habits`: Stores habit information
- `habit_logs`: Stores daily completion logs

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Test thoroughly
5. Submit a pull request

## License

This project is open source and available under the [MIT License](LICENSE).
