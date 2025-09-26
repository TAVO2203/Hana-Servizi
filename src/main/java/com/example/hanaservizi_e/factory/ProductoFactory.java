package com.example.hanaservizi_e.factory;

import com.example.hanaservizi_e.dto.ProductoDto;
import com.example.hanaservizi_e.dto.TallasStockDTO;
import com.example.hanaservizi_e.model.*;
import com.example.hanaservizi_e.repository.CategoriaRepository;
import com.example.hanaservizi_e.repository.MarcaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductoFactory {

    private final CategoriaRepository categoriaRepository;
    private final MarcaRepository marcaRepository;

    public ProductoFactory(CategoriaRepository categoriaRepository,
                           MarcaRepository marcaRepository) {
        this.categoriaRepository = categoriaRepository;
        this.marcaRepository = marcaRepository;
    }

    public Producto crearProducto(ProductoDto dto, User vendedor, String imagenGuardada) {
        Producto producto = new Producto();
        producto.setNombre(dto.getNombre());
        producto.setPrecio(dto.getPrecio());
        producto.setDescripcion(dto.getDescripcion());
        producto.setEstadoProducto(dto.getEstadoProducto());
        producto.setFechaAgregacion(dto.getFechaAgregacion());
        producto.setVendedor(vendedor);
        producto.setImagen(imagenGuardada);

        if (dto.getCategoriaId() != null) {
            Categorias categoria = categoriaRepository.findById(dto.getCategoriaId()).orElse(null);
            producto.setCategorias(categoria);
        }

        if (dto.getNombreMarca() != null && !dto.getNombreMarca().isBlank()) {
            Marca marca = marcaRepository.findByNombreMarca(dto.getNombreMarca())
                    .orElseGet(() -> marcaRepository.save(new Marca(dto.getNombreMarca())));
            producto.setMarca(marca);
        }

        if (dto.getTallas() != null && !dto.getTallas().isEmpty()) {
            List<TallaStock> tallas = dto.getTallas().stream()
                    .map(tallaDto -> {
                        TallaStock t = new TallaStock();
                        t.setTalla(tallaDto.getTalla());
                        t.setStock(tallaDto.getStock() != null ? tallaDto.getStock() : 0);
                        t.setProducto(producto);
                        return t;
                    })
                    .collect(Collectors.toList());
            producto.setTallas(tallas);

            // 🔹 Recalcular stock total a partir de tallas
            int stockTotal = tallas.stream()
                    .mapToInt(TallaStock::getStock)
                    .sum();
            producto.setStock(stockTotal);
        } else {
            // 🔹 Solo si NO hay tallas, usamos el stock general
            producto.setStock(dto.getStock() != null ? dto.getStock() : 0);
        }

        return producto;
    }

    public Producto actualizarProductoExistente(Producto producto, ProductoDto dto, String imagenGuardada) {
        producto.setNombre(dto.getNombre());
        producto.setPrecio(dto.getPrecio());
        producto.setDescripcion(dto.getDescripcion());
        producto.setEstadoProducto(dto.getEstadoProducto());
        producto.setFechaAgregacion(dto.getFechaAgregacion());

        if (imagenGuardada != null) {
            producto.setImagen(imagenGuardada);
        }

        if (dto.getCategoriaId() != null) {
            Categorias categoria = categoriaRepository.findById(dto.getCategoriaId()).orElse(null);
            producto.setCategorias(categoria);
        }

        if (dto.getNombreMarca() != null && !dto.getNombreMarca().isBlank()) {
            Marca marca = marcaRepository.findByNombreMarca(dto.getNombreMarca())
                    .orElseGet(() -> marcaRepository.save(new Marca(dto.getNombreMarca())));
            producto.setMarca(marca);
        }

        // 🔹 Limpiar tallas previas
        producto.getTallas().clear();

        if (dto.getTallas() != null && !dto.getTallas().isEmpty()) {
            for (TallasStockDTO tallaDto : dto.getTallas()) {
                if (tallaDto.getTalla() != null && !tallaDto.getTalla().isBlank()) {
                    TallaStock talla = new TallaStock();
                    talla.setTalla(tallaDto.getTalla());
                    talla.setStock(tallaDto.getStock() != null ? tallaDto.getStock() : 0);
                    talla.setProducto(producto);
                    producto.getTallas().add(talla);
                }
            }

            // 🔹 Stock desde tallas
            int stockTotal = producto.getTallas().stream()
                    .mapToInt(TallaStock::getStock)
                    .sum();
            producto.setStock(stockTotal);
        } else {
            // 🔹 Stock general si no hay tallas
            producto.setStock(dto.getStock() != null ? dto.getStock() : 0);
        }

        return producto;
    }
}