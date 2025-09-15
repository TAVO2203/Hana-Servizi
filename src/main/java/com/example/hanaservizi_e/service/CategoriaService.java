package com.example.hanaservizi_e.service;

import com.example.hanaservizi_e.model.Categorias;
import com.example.hanaservizi_e.repository.CategoriaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    public List<Categorias> listarTodo(){
        return categoriaRepository.findAll();
    }

    public Categorias guardar(Categorias categorias){
        return categoriaRepository.save(categorias);
    }

    public void eliminar(Long id){
        categoriaRepository.deleteById(id);
    }

}
