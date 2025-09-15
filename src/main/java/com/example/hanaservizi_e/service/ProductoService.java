package com.example.hanaservizi_e.service;

import com.example.hanaservizi_e.model.Producto;
import com.example.hanaservizi_e.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    public Page<Producto> buscarPorTodosLosCampos(String keyword, Long vendedorId, Pageable pageable) {
        return productoRepository.buscarPorVendedorYKeyword(vendedorId, keyword, pageable);
    }

    public Page<Producto> listarProductosPorVendedor(Long vendedorId, Pageable pageable) {
        return productoRepository.findByVendedorId(vendedorId, pageable);
    }

    public Page<Producto> buscarProductosPorVendedorYKeyword(String keyword, Long vendedorId, Pageable pageable) {
        return productoRepository.buscarPorVendedorYKeyword(vendedorId, keyword, pageable);
    }
}