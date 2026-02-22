# API REST – Caporales Cristos / C'Origen (Spring Boot)

Especificación para el frontend. Base URL configurable (ej. `http://localhost:8080` vía `environment.apiUrl`).

---

## 1. Endpoints públicos (sin auth)

### Fotos

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/api/fotos` | Lista de fotos activas |
| GET | `/api/fotos/{id}` | Foto por ID |

**Response foto:** `{ id, titulo, descripcion, urlImagen, activo, seccionId }` (+ fechaCreacion si aplica). `seccionId` indica a qué sección de galería pertenece (null = sin sección).

---

### Secciones de galería (títulos + fotos por sección)

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/api/secciones-galeria` | Lista de secciones activas, cada una con su lista de fotos activas |
| GET | `/api/secciones-galeria/{id}` | Una sección por ID (con sus fotos) |

**Response sección:** `{ id, titulo, descripcion, orden, activo, fechaCreacion, fotos: [ { id, titulo, descripcion, urlImagen, activo, ... } ] }`. Usar para construir la galería por bloques (ej. "Cultural / Tradicional", "Trajes damas") sin hardcodear títulos en el código.

---

### Videos

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/api/videos` | Lista de videos activos |
| GET | `/api/videos/{id}` | Video por ID |

**Response video:** `{ id, titulo, descripcion, urlVideo, tipoVideo, embedUrl, activo }`

- **tipoVideo:** `YOUTUBE` \| `FACEBOOK` \| `DIRECTO`
- **embedUrl:** URL para iframe (YouTube embed) o ruta directa a .mp4/.webm; `null` si es Facebook (no se puede incrustar)

---

### Productos

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/api/productos` | Lista de productos activos |
| GET | `/api/productos?categoriaId={id}` | Lista filtrada por categoría (opcional) |
| GET | `/api/productos/{id}` | Producto por ID |
| GET | `/api/categorias` | Lista de categorías |
| GET | `/api/categorias/{id}` | Categoría por ID |

**Response producto:** `{ id, nombre, descripcion, precio, urlImagen, categoriaId, categoriaNombre, stock, activo }`. Usar `stock` para límite de cantidad en carrito y para mostrar "X disponibles".

---

### Configuración tienda y métodos de pago (público)

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/api/configuracion-tienda` | Devuelve `{ id, whatsappNumero }`. Número al que se envía el mensaje de compra por WhatsApp. |
| GET | `/api/configuracion-inicio` | Devuelve `{ portadaUrl, logoUrl }`. URLs relativas (ej. `/uploads/portada/xxx.jpg`). Null si no hay imagen. Para banner y logo de la página de inicio. |
| GET | `/api/metodos-pago` | Lista de métodos de pago activos (Yape, etc.): `{ id, nombre, tipo, valor, imagenUrl, activo, orden }`. `tipo`: YAPE, PLIN, OTRO. `valor`: número o instrucciones. `imagenUrl`: opcional (ej. QR). |

---

### Pedidos (registro público, opcional)

| Método | Ruta | Descripción |
|--------|------|-------------|
| POST | `/api/pedidos` | Registrar un pedido (sin token). Body igual que crear venta (ver Admin - Ventas). Crea la venta con estado PENDIENTE y descuenta stock. Si no hay stock suficiente, devuelve error. |

---

### Archivos estáticos

Imágenes y videos subidos se sirven en:

- `{baseUrl}/uploads/fotos/xxx.jpg`
- `{baseUrl}/uploads/productos/xxx.jpg`
- `{baseUrl}/uploads/portada/xxx.jpg`, `{baseUrl}/uploads/logo/xxx.png`
- `{baseUrl}/uploads/videos/xxx.mp4`

Ejemplo: `http://localhost:8080/uploads/fotos/abc.jpg`

---

## 2. Admin (requiere JWT Bearer)

Todas las peticiones al panel admin (excepto login) deben llevar el header:

```
Authorization: Bearer <token>
```

### Auth

| Método | Ruta | Descripción |
|--------|------|-------------|
| POST | `/api/admin/login` | Login del admin (público) |

**Request body:** `{ "username": "albert.torres", "password": "albert.torres2026" }`

**Response:** `{ "token": "...", "username": "albert.torres", "rol": "ADMIN", "expiresIn": 86400 }`

*(También disponible por compatibilidad: POST `/api/auth/login` con el mismo body/response.)*

---

### Fotos

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/api/admin/fotos` | Lista todas (activas e inactivas) |
| GET | `/api/admin/fotos/{id}` | Una por ID |
| POST | `/api/admin/fotos` | Crear |
| PUT | `/api/admin/fotos/{id}` | Actualizar |
| DELETE | `/api/admin/fotos/{id}` | Eliminar |

**Crear/Actualizar:** `multipart/form-data`

- Part **`datos`:** Blob `application/json` → `{ titulo, descripcion, activo }`
- Part **`imagen`:** File (obligatorio en crear; opcional en actualizar)

---

### Secciones de galería

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/api/admin/secciones-galeria` | Lista todas las secciones (ordenadas por `orden`) |
| GET | `/api/admin/secciones-galeria/{id}` | Una sección por ID |
| POST | `/api/admin/secciones-galeria` | Crear sección (body JSON) |
| PUT | `/api/admin/secciones-galeria/{id}` | Actualizar sección (body JSON) |
| DELETE | `/api/admin/secciones-galeria/{id}` | Eliminar sección (las fotos quedan sin sección) |

**Body JSON crear/actualizar:** `{ "titulo": "...", "descripcion": "...", "orden": 0, "activo": true }`. El admin puede crear títulos como "Trajes damas", "Cultural / Tradicional", etc., y luego al subir fotos asignarles `seccionId` para que aparezcan bajo ese título.

---

### Videos

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/api/admin/videos` | Lista todas |
| GET | `/api/admin/videos/{id}` | Uno por ID |
| POST | `/api/admin/videos` | Crear |
| PUT | `/api/admin/videos/{id}` | Actualizar |
| DELETE | `/api/admin/videos/{id}` | Eliminar |

**Crear/Actualizar:** `multipart/form-data`

- Part **`datos`:** Blob `application/json` → `{ titulo, descripcion, urlVideo? (opcional si se sube archivo), activo }`
- Part **`video`:** File opcional (archivo de video: .mp4, .webm, etc.). Si se envía, se usa en lugar de `urlVideo`.

---

### Configuración tienda (WhatsApp)

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/api/admin/configuracion-tienda` | Obtener configuración actual |
| PUT | `/api/admin/configuracion-tienda` | Actualizar (body JSON: `{ "whatsappNumero": "51987654321" }`). Número al que llegan los mensajes de compra por WhatsApp. |

---

### Configuración inicio (portada y logo)

| Método | Ruta | Descripción |
|--------|------|-------------|
| PUT | `/api/admin/configuracion-inicio` | Actualizar portada y/o logo. **Multipart:** part `portada` (file opcional), part `logo` (file opcional). Al menos una obligatoria. Respuesta: `{ "portadaUrl": "...", "logoUrl": "..." }` (siempre ambos; null si no configurado). Imágenes en `/uploads/portada/` y `/uploads/logo/`. Formatos: JPEG, PNG, GIF, WebP. |

---

### Métodos de pago (Yape, etc.)

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/api/admin/metodos-pago` | Lista todos |
| GET | `/api/admin/metodos-pago/{id}` | Uno por ID |
| POST | `/api/admin/metodos-pago` | Crear (body JSON) |
| PUT | `/api/admin/metodos-pago/{id}` | Actualizar (body JSON) |
| DELETE | `/api/admin/metodos-pago/{id}` | Eliminar |

**Body JSON:** `{ "nombre": "Yape", "tipo": "YAPE", "valor": "999 888 777", "imagenUrl": "/uploads/...", "activo": true, "orden": 0 }`. `tipo`: YAPE, PLIN, OTRO. `valor`: número o instrucciones. `imagenUrl`: opcional (ej. QR de Yape).

---

### Ventas / Pedidos

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/api/admin/ventas` | Lista todas las ventas (orden por fecha desc), cada una con `detalles` (ítems con productoId, productoNombre, cantidad, precioUnitario, subtotal). |
| GET | `/api/admin/ventas/{id}` | Una venta por ID con sus líneas. |
| POST | `/api/admin/ventas` | Crear venta manual (body JSON, ver abajo). Calcula total y descuenta stock. |
| PATCH | `/api/admin/ventas/{id}/estado` | Actualizar solo estado. Body: `{ "estado": "CONFIRMADA" }`. |

**Body crear venta (POST /api/admin/ventas o POST /api/pedidos):**

```json
{
  "clienteNombre": "Juan Pérez",
  "clienteTelefono": "987654321",
  "estado": "PENDIENTE",
  "metodoPago": "WhatsApp",
  "notas": "Opcional",
  "items": [
    { "productoId": 1, "cantidad": 2, "precioUnitario": 90.00 },
    { "productoId": 2, "cantidad": 1 }
  ]
}
```

- `items`: array de líneas. `productoId` y `cantidad` obligatorios. `precioUnitario` opcional (si no se envía, se usa el precio actual del producto).
- Al crear se valida stock; si no hay suficiente se devuelve error (ej. "Stock insuficiente para [nombre] (disponible: X)").

**Response venta:** `{ id, fecha, estado, clienteNombre, clienteTelefono, total, metodoPago, notas, fechaCreacion, detalles: [ { id, productoId, productoNombre, cantidad, precioUnitario, subtotal } ] }`.

---

### Productos

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/api/admin/productos` | Lista todos |
| GET | `/api/admin/productos/{id}` | Uno por ID |
| POST | `/api/admin/productos` | Crear |
| PUT | `/api/admin/productos/{id}` | Actualizar |
| DELETE | `/api/admin/productos/{id}` | Eliminar |

**Crear/Actualizar:** `multipart/form-data`

- Part **`datos`:** Blob `application/json` → `{ nombre, descripcion, precio, categoriaId, stock, activo }`. `stock`: número (unidades disponibles).
- Part **`imagen`:** File opcional

---

### Categorías

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/api/admin/categorias` | Lista todas |
| GET | `/api/admin/categorias/{id}` | Una por ID |
| POST | `/api/admin/categorias` | Crear (body JSON) |
| PUT | `/api/admin/categorias/{id}` | Actualizar (body JSON) |
| DELETE | `/api/admin/categorias/{id}` | Eliminar |

**Body JSON:** `{ "nombre": "...", "descripcion": "..." }`

---

## 3. CORS

- Permitido origen del frontend (ej. `http://localhost:4200` o dominio de producción).
- Métodos: GET, POST, PUT, PATCH, DELETE, OPTIONS.
- Headers: `*`, `allowCredentials: true` para `/api/**` y `/uploads/**`.

---

## 4. Base URL

El frontend usa `environment.apiUrl` (ej. `http://localhost:8080`) para todas las llamadas a la API y para construir URLs de archivos: `apiUrl + urlImagen` o `apiUrl + urlVideo`.
