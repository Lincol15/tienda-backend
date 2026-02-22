# API Backend - Cristos Caporales / C'Origen

## Resumen

- **Backend:** Spring Boot 4 (Java 17)
- **Base de datos:** MySQL (`caporales`)
- **Seguridad:** JWT (login admin)
- **Público:** solo lectura en fotos, videos, productos y categorías
- **Admin:** panel en `/api/admin/**` con JWT

---

## 1. Autenticación

### POST `/api/auth/login`

Login del jefe (admin). Devuelve un token JWT.

**Body (JSON):**
```json
{
  "username": "albert.torres",
  "password": "albert.torres2026"
}
```

**Respuesta 200:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "username": "albert.torres",
  "rol": "ADMIN",
  "expiresIn": 86400
}
```

**Uso del token:** en todas las peticiones al panel admin enviar el header:
```
Authorization: Bearer <token>
```

**Usuario por defecto (creado al arrancar):**
- Usuario: `albert.torres`
- Contraseña: `albert.torres2026`  
*(cambiar en producción si aplica)*

---

## 2. APIs públicas (sin login)

### Fotos (Cristos Caporales)

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/api/fotos` | Lista de fotos activas |
| GET | `/api/fotos/{id}` | Una foto por ID |

### Secciones de galería (títulos + fotos por sección)

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/api/secciones-galeria` | Lista secciones activas con sus fotos activas (orden por `orden`) |
| GET | `/api/secciones-galeria/{id}` | Una sección con sus fotos |

**Response:** cada sección tiene `id`, `titulo`, `descripcion`, `orden`, `activo`, `fechaCreacion`, `fotos` (array de fotos). Para mostrar la galería por bloques (ej. "Cultural / Tradicional", "Trajes damas") sin hardcodear en el front.

### Videos (Cristos Caporales)

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/api/videos` | Lista de videos activos |
| GET | `/api/videos/{id}` | Un video por ID |

**Respuesta de video:** cada objeto incluye `urlVideo`, `tipoVideo` (YOUTUBE, FACEBOOK, DIRECTO) y `embedUrl`. Usar `embedUrl` para incrustar YouTube o video directo; si es Facebook, `embedUrl` es null y hay que mostrar un enlace a `urlVideo`.

### Productos (C'Origen - Tienda)

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/api/productos` | Lista de productos activos (incluye `stock`) |
| GET | `/api/productos?categoriaId=1` | Productos por categoría |
| GET | `/api/productos/{id}` | Un producto por ID |

**Response:** cada producto incluye `stock` (unidades disponibles) para carrito y validación de cantidad.

### Configuración tienda y métodos de pago (público)

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/api/configuracion-tienda` | `{ id, whatsappNumero }` – número para enlace WhatsApp |
| GET | `/api/metodos-pago` | Lista de métodos de pago activos (Yape, etc.): nombre, tipo, valor, imagenUrl |

### Configuración inicio (portada y logo)

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/api/configuracion-inicio` | URLs de la portada (banner) y del logo de la página principal. Sin auth. |

**Respuesta 200:** `{ "portadaUrl": "/uploads/portada/xxx.jpg", "logoUrl": "/uploads/logo/xxx.png" }`. Las URLs son relativas al servidor. Si no hay imagen configurada, el campo puede ser `null`. El frontend construye la URL completa con la baseUrl del API (ej. `http://localhost:8080` + portadaUrl).

### Pedidos (registro público, opcional)

| Método | Ruta | Descripción |
|--------|------|-------------|
| POST | `/api/pedidos` | Registrar un pedido (sin JWT). Body igual que crear venta (ver abajo). Crea la venta con estado PENDIENTE y descuenta stock. Útil cuando el usuario confirma el carrito y el frontend quiere guardar el pedido antes de redirigir a WhatsApp. |

### Categorías (Tienda)

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/api/categorias` | Lista de categorías |
| GET | `/api/categorias/{id}` | Una categoría por ID |

### Archivos subidos

Los archivos subidos se sirven en: `http://localhost:8080/uploads/<subfolder>/<archivo>`
- Fotos: `/uploads/fotos/...`
- Productos: `/uploads/productos/...`
- Portada y logo: `/uploads/portada/...`, `/uploads/logo/...`
- Videos (archivos subidos): `/uploads/videos/...`

---

## 3. Panel Admin (requiere JWT)

Todas las rutas bajo `/api/admin/**` exigen header `Authorization: Bearer <token>` y rol ADMIN.

### Fotos (multipart/form-data)

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/api/admin/fotos` | Lista todas (activas e inactivas) |
| GET | `/api/admin/fotos/{id}` | Una foto por ID |
| POST | `/api/admin/fotos` | Crear: part `datos` (JSON) + part `imagen` (archivo) |
| PUT | `/api/admin/fotos/{id}` | Actualizar: part `datos` (JSON), part `imagen` opcional |
| DELETE | `/api/admin/fotos/{id}` | Eliminar |

**Ejemplo `datos` (JSON):**
```json
{
  "titulo": "Presentación 2024",
  "descripcion": "Fiesta de aniversario",
  "activo": true
}
```

### Secciones de galería (JSON)

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/api/admin/secciones-galeria` | Lista todas (ordenadas por `orden`) |
| GET | `/api/admin/secciones-galeria/{id}` | Una por ID |
| POST | `/api/admin/secciones-galeria` | Crear (body JSON) |
| PUT | `/api/admin/secciones-galeria/{id}` | Actualizar (body JSON) |
| DELETE | `/api/admin/secciones-galeria/{id}` | Eliminar |

**Body:** `{ "titulo": "Trajes damas", "descripcion": "...", "orden": 0, "activo": true }`. El admin crea los títulos de cada bloque de galería; al subir fotos se asigna `seccionId` para que aparezcan bajo ese título.

### Videos (multipart/form-data)

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/api/admin/videos` | Lista todas |
| GET | `/api/admin/videos/{id}` | Uno por ID |
| POST | `/api/admin/videos` | Crear: part `datos` (JSON) + part `video` (archivo opcional) |
| PUT | `/api/admin/videos/{id}` | Actualizar: part `datos` (JSON), part `video` opcional |
| DELETE | `/api/admin/videos/{id}` | Eliminar |

**Dos formas de crear/actualizar:**
1. **Por URL:** part `datos` con `titulo`, `descripcion`, `urlVideo` (obligatorio), `activo`. No enviar part `video`.
2. **Por archivo (galería o archivos):** part `datos` con `titulo`, `descripcion`, `activo` (sin urlVideo); part `video` con el archivo (mp4, webm, etc.).

**Ejemplo `datos` (JSON) cuando es por URL:**
```json
{
  "titulo": "Danza Caporal",
  "descripcion": "Video oficial",
  "urlVideo": "https://www.youtube.com/watch?v=...",
  "activo": true
}
```

**Ejemplo `datos` cuando se sube archivo:** omitir `urlVideo` o enviar null.

### Productos (multipart/form-data)

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/api/admin/productos` | Lista todos |
| GET | `/api/admin/productos/{id}` | Uno por ID |
| POST | `/api/admin/productos` | Crear: part `datos` (JSON) + part `imagen` opcional |
| PUT | `/api/admin/productos/{id}` | Actualizar: part `datos`, part `imagen` opcional |
| DELETE | `/api/admin/productos/{id}` | Eliminar |

**Ejemplo `datos` (JSON):**
```json
{
  "nombre": "Traje Caporal",
  "descripcion": "Traje tradicional",
  "precio": 350.00,
  "categoriaId": 1,
  "stock": 10,
  "activo": true
}
```
`stock`: unidades disponibles (para carrito y admin).

### Configuración tienda (WhatsApp)

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/api/admin/configuracion-tienda` | Obtener configuración |
| PUT | `/api/admin/configuracion-tienda` | Actualizar: body `{ "whatsappNumero": "51987654321" }` |

### Configuración inicio (portada y logo)

| Método | Ruta | Descripción |
|--------|------|-------------|
| PUT | `/api/admin/configuracion-inicio` | Actualizar portada y/o logo. **Content-Type:** multipart/form-data. Partes: `portada` (archivo opcional), `logo` (archivo opcional). Al menos una obligatoria. Se guardan en `/uploads/portada/` y `/uploads/logo/` con nombre único. Se eliminan los archivos antiguos al subir nuevos. Formatos permitidos: JPEG, PNG, GIF, WebP. Respuesta 200: `{ "portadaUrl": "...", "logoUrl": "..." }` (siempre ambos campos; null si no hay imagen). |

### Métodos de pago (Yape, etc.)

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET/POST/PUT/DELETE | `/api/admin/metodos-pago` (y `/{id}`) | CRUD de métodos. Body: `{ "nombre": "Yape", "tipo": "YAPE", "valor": "999 888 777", "imagenUrl": "...", "activo": true, "orden": 0 }`. |

### Ventas / Pedidos (JSON)

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/api/admin/ventas` | Lista todas las ventas (orden por fecha descendente), con detalles (productos, cantidades, precios). |
| GET | `/api/admin/ventas/{id}` | Una venta por ID con sus líneas (detalles). |
| POST | `/api/admin/ventas` | Crear venta manual. Body: ver `VentaRequest` abajo. Calcula total y descuenta stock. |
| PATCH | `/api/admin/ventas/{id}/estado` | Actualizar solo el estado. Body: `{ "estado": "CONFIRMADA" }`. |

**Body crear venta (POST /api/admin/ventas o POST /api/pedidos):**
```json
{
  "clienteNombre": "Juan Pérez",
  "clienteTelefono": "987654321",
  "estado": "PENDIENTE",
  "metodoPago": "WhatsApp",
  "notas": "Entregar en la tarde",
  "items": [
    { "productoId": 1, "cantidad": 2, "precioUnitario": 90.00 },
    { "productoId": 3, "cantidad": 1 }
  ]
}
```
- `items`: array de líneas. `productoId` y `cantidad` obligatorios. `precioUnitario` opcional (si no se envía, se usa el precio actual del producto).
- Al crear la venta se valida stock; si no hay suficiente se devuelve error. Se descuenta el stock de cada producto.

**Respuesta venta (GET o POST):** incluye `id`, `fecha`, `estado`, `clienteNombre`, `clienteTelefono`, `total`, `metodoPago`, `notas`, `fechaCreacion`, `detalles` (array con `productoId`, `productoNombre`, `cantidad`, `precioUnitario`, `subtotal`).

### Categorías (JSON)

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/api/admin/categorias` | Lista todas |
| GET | `/api/admin/categorias/{id}` | Una por ID |
| POST | `/api/admin/categorias` | Crear (body JSON) |
| PUT | `/api/admin/categorias/{id}` | Actualizar (body JSON) |
| DELETE | `/api/admin/categorias/{id}` | Eliminar |

**Body ejemplo:**
```json
{
  "nombre": "Trajes",
  "descripcion": "Vestimenta tradicional"
}
```

---

## 4. WhatsApp (frontend)

El pago no es automático. El frontend debe:

1. Construir el mensaje con producto y precio.
2. Redirigir a: `https://wa.me/51NUMERO?text=MENSAJE`
3. El número (51XXXXXXXXX) puede estar en variables de entorno del frontend.

No hay endpoint en el backend para esto; todo se hace en Angular.

---

## 5. Configuración

- **application.properties:** BD, JWT, carpeta de subidas, tamaño máximo de archivo (10 MB).
- **Base de datos:** crear en MySQL la base `caporales`; las tablas se crean con `ddl-auto=update`.
- **Carpeta de subidas:** por defecto `uploads/` (fotos y productos).

---

## 6. CORS

El backend permite todos los orígenes en `/api/**` y `/uploads/**` para que Angular (otro puerto o dominio) pueda consumir la API.
