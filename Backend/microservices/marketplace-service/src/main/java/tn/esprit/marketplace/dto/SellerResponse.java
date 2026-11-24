package tn.esprit.marketplace.dto;

import tn.esprit.marketplace.entity.enums.SellerStatus;

public record SellerResponse(Long id, String userId, String shopName, String email, SellerStatus status) {
}