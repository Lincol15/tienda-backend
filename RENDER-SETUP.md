# Configurar backend en Render

## Por qué falla al arrancar

En Render no hay MySQL en localhost. La app necesita una base de datos accesible desde internet. Render ofrece **PostgreSQL gratis**; el código ya está preparado para usarla.

---

## Pasos en Render

### 1. Crear base de datos PostgreSQL

1. En el dashboard de Render, clic en **"+ New"** → **"PostgreSQL"**.
2. Pon un nombre (ej. `tienda-db`) y elige región. Deja el plan **Free**.
3. Clic en **"Create Database"**.
4. Espera a que esté en estado **Available**.

### 2. Obtener la cadena de conexión

1. Entra en la base de datos que creaste.
2. En **"Connections"** verás **"Internal Database URL"** (para usarla desde tu Web Service en Render).
3. Copia esa URL. Tiene esta forma:
   ```
   postgresql://usuario:contraseña@hostname/database?options
   ```
4. Para Spring Boot necesitas pasarla en formato JDBC. Convierte así:
   - Si la URL es: `postgresql://usuario:contraseña@dpg-xxxxx-a.oregon-postgres.render.com/tienda_db`
   - La JDBC sería: `jdbc:postgresql://dpg-xxxxx-a.oregon-postgres.render.com:5432/tienda_db`
   - El **usuario** y la **contraseña** los pones aparte en variables de entorno (abajo).

   O bien usa la **"External Database URL"** si Render te da una **JDBC URL** directamente; si no, construye la JDBC como arriba (host, puerto 5432, nombre de base de datos) y usuario/contraseña por separado.

### 3. Variables de entorno del Web Service

1. Entra en tu **Web Service** (tienda-backend).
2. Menú **"Environment"** (Manage → Environment).
3. Añade estas variables (usa los valores de tu PostgreSQL de Render):

   | Key | Value |
   |-----|--------|
   | `SPRING_DATASOURCE_URL` | `jdbc:postgresql://HOST:5432/NOMBRE_BD` (sustituye HOST y NOMBRE_BD por los de tu Internal Connection String) |
   | `SPRING_DATASOURCE_USERNAME` | Usuario de la BD (ej. el que sale en la URL de Render) |
   | `SPRING_DATASOURCE_PASSWORD` | Contraseña de la BD |
   | `SPRING_JPA_HIBERNATE_DIALECT` | `org.hibernate.dialect.PostgreSQLDialect` |

   Ejemplo (con valores de ejemplo):
   - `SPRING_DATASOURCE_URL` = `jdbc:postgresql://dpg-xxxxx-a.oregon-postgres.render.com:5432/tienda_db`
   - `SPRING_DATASOURCE_USERNAME` = `tienda_user`
   - `SPRING_DATASOURCE_PASSWORD` = (la que te dio Render)
   - `SPRING_JPA_HIBERNATE_DIALECT` = `org.hibernate.dialect.PostgreSQLDialect`

4. Guarda los cambios. Render volverá a desplegar el servicio.

### 4. (Opcional) JWT y URL pública

Si quieres cambiar el secreto JWT o la URL base en producción, añade también:

- `APP_JWT_SECRET` = una clave larga y aleatoria (mín. 32 caracteres)
- `APP_BASE_URL` = `https://tienda-backend-ntfj.onrender.com` (tu URL de Render)

---

Después del deploy, la app creará las tablas sola (`ddl-auto=update`). El usuario admin se crea al arrancar (albert.torres / albert.torres2026).
