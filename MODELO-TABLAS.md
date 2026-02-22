# Modelo de tablas y conexiones – Cristos Caporales / C'Origen

## Resumen

- **Contenido público:** fotos, videos, productos, categorías, secciones de galería, configuración tienda, métodos de pago. Solo lectura.
- **Admin:** login para gestionar todo lo anterior; además puede ver y gestionar **ventas/pedidos** (crear venta, listar, ver detalle, actualizar estado).
- **Pedidos:** el frontend puede registrar un pedido (POST público) al confirmar el carrito; el backend crea la venta con estado PENDIENTE y descuenta stock.

---

## Tablas y relaciones

### 1. `usuarios`
- **Uso:** solo login del admin.
- **Relación:** ninguna. Tabla independiente.
- **Campos:** id, username, password, rol (ADMIN), activo, fecha_creacion.

### 2. `galeria_secciones`
- **Uso:** títulos/bloques de la galería (ej. "Cultural", "Trajes damas"). El admin los crea; las fotos se asignan a una sección.
- **Relación:** **una sección tiene muchas fotos** (1 → N).
- **Campos:** id, titulo, descripcion, orden, activo, fecha_creacion.

### 3. `fotos` (Cristos Caporales)
- **Uso:** galería pública; el admin las sube/edita/elimina.
- **Relación:** **muchas fotos pueden tener una sección** (N → 1). Campo `seccion_id` (FK opcional).
- **Campos:** id, titulo, descripcion, url_imagen, seccion_id (FK), activo, fecha_creacion.
- **Público:** solo ve las que tienen `activo = true`, agrupadas por sección (GET /api/secciones-galeria).

### 4. `videos` (Cristos Caporales)
- **Uso:** videos públicos (YouTube, Facebook, archivo subido). El admin los gestiona.
- **Relación:** ninguna. Tabla independiente.
- **Campos:** id, titulo, descripcion, url_video, tipo_video, embed_url, activo, fecha_creacion.
- **Público:** solo ve los que tienen `activo = true`.

### 5. `categorias` (C'Origen – tienda)
- **Uso:** agrupar productos (ej. Trajes, Accesorios).
- **Relación:** **una categoría tiene muchos productos** (1 → N). Sin cascade que borre productos al eliminar categoría.
- **Campos:** id, nombre, descripcion.

### 6. `productos` (C'Origen – tienda)
- **Uso:** catálogo con nombre, precio, imagen, stock.
- **Relación:** **muchos productos pertenecen a una categoría** (N → 1). Campo `categoria_id` (FK opcional).
- **Campos:** id, nombre, descripcion, precio, url_imagen, categoria_id (FK opcional), stock, activo, fecha_creacion.
- **Público:** solo ve productos con `activo = true`. El stock se usa para carrito y ventas (se descuenta al crear venta).

### 7. `configuracion_tienda`
- **Uso:** configuración global de la tienda (ej. número de WhatsApp).
- **Relación:** ninguna. Una sola fila.
- **Campos:** id, whatsapp_numero.

### 8. `configuracion_inicio`
- **Uso:** imagen de portada (banner) y logo de la página de inicio. El admin las sube desde el panel; el público las lee por GET /api/configuracion-inicio.
- **Relación:** ninguna. Una sola fila.
- **Campos:** id, portada_url, logo_url (rutas relativas, ej. /uploads/portada/xxx.jpg).

### 9. `metodos_pago`
- **Uso:** métodos de pago mostrados en la tienda (Yape, Plin, etc.).
- **Relación:** ninguna. Tabla independiente.
- **Campos:** id, nombre, tipo, valor, imagen_url, activo, orden, fecha_creacion.

### 10. `ventas`
- **Uso:** venta o pedido (cabecera). Creada por el admin manualmente o por el frontend al registrar pedido (POST /api/pedidos).
- **Relación:** **una venta tiene muchas líneas** (1 → N) en `detalle_venta`.
- **Campos:** id, fecha, estado (PENDIENTE, CONFIRMADA, etc.), cliente_nombre, cliente_telefono, total, metodo_pago, notas, fecha_creacion.

### 11. `detalle_venta`
- **Uso:** cada línea de una venta: producto, cantidad, precio unitario, subtotal.
- **Relación:** **N líneas pertenecen a una venta** (N → 1) y **cada línea es un producto** (N → 1 producto).
- **Campos:** id, venta_id (FK), producto_id (FK), cantidad, precio_unitario, subtotal.

---

## Esquema de conexiones

```
usuarios              (sin relación con el resto)

galeria_secciones     ←—— fotos (seccion_id)
     │
videos                (independiente)

categorias            ←—— productos (categoria_id, stock)
     │
configuracion_tienda  (una fila)
configuracion_inicio  (una fila: portada_url, logo_url)
metodos_pago          (independiente)

ventas                ←—— detalle_venta (venta_id)
     │                        │
     └────────────────────────┴—— producto_id → productos
```

**Resumen:** Las conexiones son: **GaleriaSeccion ↔ Foto**, **Categoria ↔ Producto**, **Venta ↔ DetalleVenta** y **DetalleVenta ↔ Producto**. Al crear una venta, el backend descuenta el stock de cada producto según las cantidades de los ítems.
