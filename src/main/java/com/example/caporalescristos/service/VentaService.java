package com.example.caporalescristos.service;

import com.example.caporalescristos.dto.*;
import com.example.caporalescristos.entity.DetalleVenta;
import com.example.caporalescristos.entity.Producto;
import com.example.caporalescristos.entity.Venta;
import com.example.caporalescristos.exception.ResourceNotFoundException;
import com.example.caporalescristos.repository.ProductoRepository;
import com.example.caporalescristos.repository.VentaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VentaService {

    private final VentaRepository ventaRepository;
    private final ProductoRepository productoRepository;

    @Transactional(readOnly = true)
    public List<VentaDto> listarTodas() {
        return ventaRepository.findAllWithDetalles().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public VentaDto obtenerPorId(Long id) {
        Venta venta = ventaRepository.findByIdWithDetalles(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venta", id));
        return toDto(venta);
    }

    @Transactional
    public VentaDto crear(VentaRequest request) {
        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new IllegalArgumentException("La venta debe tener al menos un ítem");
        }
        Venta venta = new Venta();
        venta.setFecha(java.time.LocalDateTime.now());
        venta.setEstado(request.getEstado() != null ? request.getEstado() : "PENDIENTE");
        venta.setClienteNombre(request.getClienteNombre());
        venta.setClienteTelefono(request.getClienteTelefono());
        venta.setMetodoPago(request.getMetodoPago());
        venta.setNotas(request.getNotas());
        venta.setTotal(BigDecimal.ZERO);

        venta = ventaRepository.save(venta);
        BigDecimal total = BigDecimal.ZERO;

        for (DetalleVentaRequest item : request.getItems()) {
            if (item.getProductoId() == null || item.getCantidad() == null || item.getCantidad() <= 0) continue;
            Producto producto = productoRepository.findById(item.getProductoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Producto", item.getProductoId()));
            BigDecimal precioUnitario = item.getPrecioUnitario() != null ? item.getPrecioUnitario() : producto.getPrecio();
            int cantidad = item.getCantidad();
            if (producto.getStock() != null && producto.getStock() < cantidad) {
                throw new IllegalArgumentException("Stock insuficiente para " + producto.getNombre() + " (disponible: " + producto.getStock() + ")");
            }
            BigDecimal subtotal = precioUnitario.multiply(BigDecimal.valueOf(cantidad)).setScale(2, RoundingMode.HALF_UP);
            DetalleVenta detalle = DetalleVenta.builder()
                    .venta(venta)
                    .producto(producto)
                    .cantidad(cantidad)
                    .precioUnitario(precioUnitario)
                    .subtotal(subtotal)
                    .build();
            venta.getDetalles().add(detalle);
            total = total.add(subtotal);
            producto.setStock(producto.getStock() - cantidad);
        }

        venta.setTotal(total.setScale(2, RoundingMode.HALF_UP));
        venta = ventaRepository.save(venta);
        return toDto(venta);
    }

    @Transactional
    public VentaDto actualizarEstado(Long id, String estado) {
        Venta venta = ventaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venta", id));
        venta.setEstado(estado != null ? estado : venta.getEstado());
        venta = ventaRepository.save(venta);
        return toDto(venta);
    }

    private VentaDto toDto(Venta v) {
        List<DetalleVentaDto> detallesDto = v.getDetalles().stream()
                .map(d -> DetalleVentaDto.builder()
                        .id(d.getId())
                        .productoId(d.getProducto().getId())
                        .productoNombre(d.getProducto().getNombre())
                        .cantidad(d.getCantidad())
                        .precioUnitario(d.getPrecioUnitario())
                        .subtotal(d.getSubtotal())
                        .build())
                .collect(Collectors.toList());
        return VentaDto.builder()
                .id(v.getId())
                .fecha(v.getFecha())
                .estado(v.getEstado())
                .clienteNombre(v.getClienteNombre())
                .clienteTelefono(v.getClienteTelefono())
                .total(v.getTotal())
                .metodoPago(v.getMetodoPago())
                .notas(v.getNotas())
                .fechaCreacion(v.getFechaCreacion())
                .detalles(detallesDto)
                .build();
    }
}
