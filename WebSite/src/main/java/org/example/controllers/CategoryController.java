package org.example.controllers;

import lombok.RequiredArgsConstructor;
import org.example.dto.category.CategoryCreateDTO;
import org.example.dto.category.CategoryItemDTO;
import org.example.dto.category.CategoryUpdateDTO;
import org.example.entities.CategoryEntity;
import org.example.mappers.CategoryMapper;
import org.example.repositories.CategoryRepository;
import org.example.storage.StorageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
    private final StorageService storageService;
    @GetMapping("/category")
    public ResponseEntity<List<CategoryItemDTO>> index() {
        var cats = categoryRepository.findAll();
        var list = mapper.listCategoriesToItemDTO(cats);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
    @PostMapping(value = "/category", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CategoryItemDTO create(@ModelAttribute CategoryCreateDTO dto) {
        var fileName = storageService.saveMultipartFile(dto.getImage());
        CategoryEntity entity = CategoryEntity
                .builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .image(fileName)
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
    @PutMapping(value = "/category/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CategoryItemDTO> updateCategory(@PathVariable int id, @ModelAttribute CategoryUpdateDTO dto) {
        Optional<CategoryEntity> catOpt = categoryRepository.findById(id);
        if(!catOpt.isPresent())
        {
            return ResponseEntity.notFound().build();
        }
        var category = catOpt.get();
        if (category.getImage() != null && !category.getImage().isEmpty()) {
            storageService.removeFile(category.getImage());
        }
        var fileName = storageService.saveMultipartFile(dto.getImage());

        category.setName(dto.getName());
        category.setDescription(dto.getDescription());
        category.setImage(fileName);

        categoryRepository.save(category);
        return ResponseEntity.ok().body(mapper.categoryToItemDTO(category));
    }

    @DeleteMapping("/category/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable int id) {
        Optional<CategoryEntity> catOpt = categoryRepository.findById(id);
        if(catOpt.isPresent()) {
            var category = catOpt.get();
            if (category.getImage() != null && !category.getImage().isEmpty()) {
                storageService.removeFile(category.getImage());
            }
            categoryRepository.delete(category);
            return ResponseEntity.noContent().build();
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }
}
