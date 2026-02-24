# Cristos Caporales / C'Origen – API Backend

Backend en **Spring Boot** (Java 17) para la web **Cristos Caporales** (galería, videos) y la tienda **C'Origen** (productos, pedidos, ventas). API REST con autenticación JWT para el panel de administración.

## Requisitos

- **Java 17**
- **Maven** (o usar el wrapper `mvnw` / `mvnw.cmd` incluido)
- **MySQL** (base de datos `caporales`)

## Configuración

1. Crear en MySQL la base de datos:
   ```sql
   CREATE DATABASE caporales CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```

2. Ajustar `src/main/resources/application.properties`:
   - `spring.datasource.url`, `username` y `password` de tu MySQL
   - En producción, usar variables de entorno para la contraseña y para `app.jwt.secret`

3. (Opcional) Para configuraciones locales sin subir a Git, crea `application-local.properties` con tus valores (ese archivo está en `.gitignore`).

## Ejecución

```bash
# Con Maven wrapper (Windows)
.\mvnw.cmd spring-boot:run

# Con Maven instalado
mvn spring-boot:run
```

La API queda disponible en **http://localhost:8080**.

## Acceso al panel admin

- **Usuario:** `albert.torres`
- **Contraseña:** `albert.torres2026`

*(Se crea automáticamente al arrancar si no existe. En producción conviene cambiar la contraseña.)*

Login: `POST /api/admin/login` o `POST /api/auth/login` con JSON:
```json
{ "username": "albert.torres", "password": "albert.torres2026" }
```

## Documentación del API

- **API-BACKEND.md** – Resumen de endpoints (públicos y admin)
- **API-REST-ESPECIFICACION.md** – Especificación para el frontend
- **MODELO-TABLAS.md** – Modelo de datos y relaciones
- **POSTMAN-CAPORALES-CRISTOS.json** – Colección de Postman para probar la API
- **PROMPT-FRONTEND-ANGULAR.md** – Prompts para desarrollar el frontend en Angular

## Estructura del proyecto

- `src/main/java/.../controller/` – Controladores públicos y admin
- `src/main/java/.../entity/` – Entidades JPA
- `src/main/java/.../repository/` – Repositorios
- `src/main/java/.../service/` – Lógica de negocio
- `src/main/java/.../config/` – Seguridad (JWT), CORS, inicialización de datos
- `uploads/` – Archivos subidos (fotos, productos, videos, portada, logo); no se sube a Git

## Subir a GitHub

1. Revisa que no haya contraseñas ni secretos reales en `application.properties` si el repo es público (usa variables de entorno en producción).
2. Crea el repositorio en GitHub y enlaza el remoto:
   ```bash
   git remote add origin https://github.com/TU_USUARIO/TU_REPO.git
   ```
3. Sube el código:
   ```bash
   git add .
   git commit -m "Backend Cristos Caporales / C'Origen - Spring Boot"
   git push -u origin main
   ```
   *(Si tu rama se llama `master`, usa `git push -u origin master`.)*

## Licencia

Uso interno / proyecto propio.
