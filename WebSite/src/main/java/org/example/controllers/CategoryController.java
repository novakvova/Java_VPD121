package org.example.controllers;

import lombok.RequiredArgsConstructor;
import org.example.dto.category.CategoryCreateDTO;
import org.example.dto.category.CategoryItemDTO;
import org.example.dto.category.CategoryUpdateDTO;
import org.example.entities.CategoryEntity;
import org.example.mappers.CategoryMapper;
import org.example.repositories.CategoryRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper mapper;
    @GetMapping("/category")
    public ResponseEntity<List<CategoryItemDTO>> index() {
        var list = mapper.listCategoriesToItemDTO(categoryRepository.findAll());
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
    @PostMapping("/category")
    public CategoryItemDTO create(@RequestBody CategoryCreateDTO dto) {
        CategoryEntity entity = CategoryEntity
                .builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .build();
        categoryRepository.save(entity);
        return mapper.categoryToItemDTO(entity);
    }
    @GetMapping("/category/{id}")
    public ResponseEntity<CategoryItemDTO> getCategoryById(@PathVariable int id) {
        Optional<CategoryEntity> catOpt = categoryRepository.findById(id);
        return catOpt
                .map(cat -> ResponseEntity.ok().body(mapper.categoryToItemDTO(cat)))
                .orElse(ResponseEntity.notFound().build());
    }
    @PutMapping("/category/{id}")
    public ResponseEntity<CategoryItemDTO> updateCategory(@PathVariable int id, @RequestBody CategoryUpdateDTO dto) {
        Optional<CategoryEntity> catOpt = categoryRepository.findById(id);
        return catOpt.map(cat-> {
            cat.setName(dto.getName());
            cat.setDescription(dto.getDescription());
            categoryRepository.save(cat);
            return ResponseEntity.ok().body(mapper.categoryToItemDTO(cat));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/category/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable int id) {
        Optional<CategoryEntity> catOpt = categoryRepository.findById(id);
        if(catOpt.isPresent()) {
            categoryRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }


}
