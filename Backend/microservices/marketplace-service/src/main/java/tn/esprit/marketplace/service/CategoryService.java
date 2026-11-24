package tn.esprit.marketplace.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.marketplace.dto.CategoryRequest;
import tn.esprit.marketplace.dto.CategoryResponse;
import tn.esprit.marketplace.entity.Category;
import tn.esprit.marketplace.exception.ResourceNotFoundException;
import tn.esprit.marketplace.repository.CategoryRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryResponse create(CategoryRequest request) {
        if (categoryRepository.findByName(request.name()).isPresent()) {
            throw new IllegalStateException("Cette categorie existe deja");
        }
        Category category = new Category();
        category.setName(request.name());
        category.setDescription(request.description());
        return toResponse(categoryRepository.save(category));
    }

    @Transactional(readOnly = true)
    public List<CategoryResponse> findAll() {
        return categoryRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public CategoryResponse findById(Long id) {
        return toResponse(getEntity(id));
    }

    public CategoryResponse update(Long id, CategoryRequest request) {
        Category category = getEntity(id);
        category.setName(request.name());
        category.setDescription(request.description());
        return toResponse(categoryRepository.save(category));
    }

    public void delete(Long id) {
        categoryRepository.delete(getEntity(id));
    }

    private Category getEntity(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categorie", id));
    }

    private CategoryResponse toResponse(Category c) {
        return new CategoryResponse(c.getId(), c.getName(), c.getDescription());
    }
}