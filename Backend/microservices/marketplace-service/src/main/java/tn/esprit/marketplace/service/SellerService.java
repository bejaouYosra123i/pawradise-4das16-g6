package tn.esprit.marketplace.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.marketplace.dto.SellerRequest;
import tn.esprit.marketplace.dto.SellerResponse;
import tn.esprit.marketplace.entity.Seller;
import tn.esprit.marketplace.exception.ResourceNotFoundException;
import tn.esprit.marketplace.repository.SellerRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SellerService {

    private final SellerRepository sellerRepository;

    public SellerResponse create(SellerRequest request) {
        if (sellerRepository.findByUserId(request.userId()).isPresent()) {
            throw new IllegalStateException("Un vendeur existe deja pour cet utilisateur");
        }
        Seller seller = new Seller();
        seller.setUserId(request.userId());
        seller.setShopName(request.shopName());
        seller.setEmail(request.email());
        return toResponse(sellerRepository.save(seller));
    }

    @Transactional(readOnly = true)
    public List<SellerResponse> findAll() {
        return sellerRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public SellerResponse findById(Long id) {
        return toResponse(getEntity(id));
    }

    public SellerResponse update(Long id, SellerRequest request) {
        Seller seller = getEntity(id);
        seller.setShopName(request.shopName());
        seller.setEmail(request.email());
        return toResponse(sellerRepository.save(seller));
    }

    public SellerResponse activate(Long id) {
        Seller seller = getEntity(id);
        seller.activate();
        return toResponse(sellerRepository.save(seller));
    }

    public SellerResponse suspend(Long id) {
        Seller seller = getEntity(id);
        seller.suspend();
        return toResponse(sellerRepository.save(seller));
    }

    public void delete(Long id) {
        sellerRepository.delete(getEntity(id));
    }

    private Seller getEntity(Long id) {
        return sellerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vendeur", id));
    }

    private SellerResponse toResponse(Seller s) {
        return new SellerResponse(s.getId(), s.getUserId(), s.getShopName(), s.getEmail(), s.getStatus());
    }
}