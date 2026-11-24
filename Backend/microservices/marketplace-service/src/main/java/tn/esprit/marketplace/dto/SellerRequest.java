package tn.esprit.marketplace.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SellerRequest(
        @NotBlank String userId,
        @NotBlank String shopName,
        @NotBlank @Email String email) {
}