# CampusLK Deployment Guide

CampusLK has two deployable parts:

- `campuslk-api/campuslk-api`: Spring Boot backend API
- `campuslk_app`: Flutter app that can be built for web, Android, and iOS

## 1. Production Database

Use a hosted MySQL database for real users. Do not use the local Workbench database for production.

Create a database named `campuslk` and a dedicated user such as `campuslk_user`.

Required backend environment variables:

```text
DB_URL=jdbc:mysql://your-db-host:3306/campuslk?useSSL=true
DB_USERNAME=campuslk_user
DB_PASSWORD=your_strong_password
JWT_SECRET=your_very_long_random_secret_key
CORS_ALLOWED_ORIGIN_PATTERNS=https://your-campuslk-web-domain.com
```

## 2. Backend Deployment

Deploy the Spring Boot API from:

```text
campuslk-api/campuslk-api
```

Build command:

```bash
./mvnw package -DskipTests
```

Start command:

```bash
java -jar target/campuslk-api-0.0.1-SNAPSHOT.jar
```

The backend supports both `PORT` and `SERVER_PORT` environment variables. Most cloud platforms set `PORT` automatically.

After deployment, your backend URL will look like:

```text
https://your-campuslk-api-domain.com
```

## 3. Flutter Web Deployment

Build the web app from:

```text
campuslk_app
```

Use your deployed backend URL:

```bash
flutter build web --dart-define=API_BASE_URL=https://your-campuslk-api-domain.com
```

Deploy the generated folder:

```text
campuslk_app/build/web
```

## 4. CORS

After the web app has a real domain, set backend CORS:

```text
CORS_ALLOWED_ORIGIN_PATTERNS=https://your-campuslk-web-domain.com
```

For multiple domains, separate them with commas:

```text
CORS_ALLOWED_ORIGIN_PATTERNS=https://campuslk.com,https://www.campuslk.com
```

## 5. Local Development

Keep local secrets in:

```text
campuslk-api/campuslk-api/src/main/resources/application-local.properties
```

That file is ignored by Git and should not be committed.

Run local backend:

```powershell
$env:SPRING_PROFILES_ACTIVE="local"
.\mvnw.cmd spring-boot:run
```

Run Flutter locally:

```bash
flutter run -d chrome --dart-define=API_BASE_URL=http://localhost:8080
```
