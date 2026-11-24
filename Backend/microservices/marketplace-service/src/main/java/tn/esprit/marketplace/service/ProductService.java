package tn.esprit.marketplace.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.marketplace.dto.ProductRequest;
import tn.esprit.marketplace.dto.ProductResponse;
import tn.esprit.marketplace.entity.Category;
import tn.esprit.marketplace.entity.Product;
import tn.esprit.marketplace.entity.Seller;
import tn.esprit.marketplace.entity.enums.SellerStatus;
import tn.esprit.marketplace.entity.enums.TargetAnimal;
import tn.esprit.marketplace.exception.ResourceNotFoundException;
import tn.esprit.marketplace.repository.CategoryRepository;
import tn.esprit.marketplace.repository.ProductRepository;
import tn.esprit.marketplace.repository.SellerRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final SellerRepository sellerRepository;
    private final CategoryRepository categoryRepository;

    public ProductResponse create(ProductRequest request) {
        Seller seller = sellerRepository.findById(request.sellerId())
                .orElseThrow(() -> new ResourceNotFoundException("Vendeur", request.sellerId()));
        if (seller.getStatus() != SellerStatus.ACTIVE) {
            throw new IllegalStateException("Seul un vendeur actif peut ajouter des produits");
        }
        Category category = getCategory(request.categoryId());

        Product product = new Product();
        product.setSeller(seller);
        apply(product, request, category);
        return toResponse(productRepository.save(product));
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> findAll(Long categoryId, Long sellerId, TargetAnimal targetAnimal, String search) {
        List<Product> products;
        if (categoryId != null) {
            products = productRepository.findByCategoryId(categoryId);
        } else if (sellerId != null) {
            products = productRepository.findBySellerId(sellerId);
        } else if (targetAnimal != null) {
            products = productRepository.findByTargetAnimal(targetAnimal);
        } else if (search != null && !search.isBlank()) {
            products = productRepository.findByNameContainingIgnoreCase(search);
        } else {
            products = productRepository.findAll();
        }
        return products.stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public ProductResponse findById(Long id) {
        return toResponse(getEntity(id));
    }

    public ProductResponse update(Long id, ProductRequest request) {
        Product product = getEntity(id);
        if (!product.getSeller().getId().equals(request.sellerId())) {
            throw new IllegalArgumentException("Le vendeur d'un produit ne peut pas etre modifie");
        }
        apply(product, request, getCategory(request.categoryId()));
        return toResponse(productRepository.save(product));
    }

    public void delete(Long id) {
        productRepository.delete(getEntity(id));
    }

    private void apply(Product product, ProductRequest request, Category category) {
        product.setName(request.name());
        product.setDescription(request.description());
        product.updatePrice(request.price());
        product.setImageUrl(request.imageUrl());
        product.setTargetAnimal(request.targetAnimal());
        product.setStockQuantity(request.stockQuantity());
        product.setLowStockThreshold(request.lowStockThreshold());
        product.setCategory(category);
    }

    private Product getEntity(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produit", id));
    }

    private Category getCategory(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categorie", id));
    }

    private ProductResponse toResponse(Product p) {
        return new ProductResponse(
                p.getId(), p.getName(), p.getDescription(), p.getPrice(), p.getImageUrl(),
                p.getTargetAnimal(), p.getStockQuantity(), p.isAvailable(), p.isLowStock(),
                p.getSeller().getId(), p.getSeller().getShopName(),
                p.getCategory().getId(), p.getCategory().getName());
    }
}