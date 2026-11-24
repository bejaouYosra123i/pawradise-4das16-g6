package tn.esprit.marketplace.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.marketplace.dto.CheckoutRequest;
import tn.esprit.marketplace.dto.OrderLineRequest;
import tn.esprit.marketplace.dto.OrderRequest;
import tn.esprit.marketplace.dto.OrderResponse;
import tn.esprit.marketplace.entity.Cart;
import tn.esprit.marketplace.entity.CartItem;
import tn.esprit.marketplace.entity.Order;
import tn.esprit.marketplace.entity.OrderItem;
import tn.esprit.marketplace.entity.Payment;
import tn.esprit.marketplace.entity.Product;
import tn.esprit.marketplace.entity.enums.CartStatus;
import tn.esprit.marketplace.entity.enums.PaymentMethod;
import tn.esprit.marketplace.entity.enums.PaymentStatus;
import tn.esprit.marketplace.exception.ResourceNotFoundException;
import tn.esprit.marketplace.mapper.OrderMapper;
import tn.esprit.marketplace.repository.CartRepository;
import tn.esprit.marketplace.repository.OrderRepository;
import tn.esprit.marketplace.repository.ProductRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final OrderMapper mapper;

    // Panier -> commande
    public OrderResponse checkout(String userId, CheckoutRequest request) {
        Cart cart = cartRepository.findByUserIdAndStatus(userId, CartStatus.ACTIVE)
                .orElseThrow(() -> new IllegalStateException("Aucun panier actif pour cet utilisateur"));
        if (cart.getItems().isEmpty()) {
            throw new IllegalStateException("Le panier est vide");
        }
        Order order = newOrder(userId, request.shippingAddress());
        for (CartItem item : cart.getItems()) {
            addLine(order, item.getProduct(), item.getQuantity());
        }
        cart.setStatus(CartStatus.CHECKED_OUT);
        return mapper.toResponse(saveWithPayment(order, request.paymentMethod()));
    }

    // Commande directe sans panier (utilisee par le service Donation)
    public OrderResponse createDirect(OrderRequest request) {
        Order order = newOrder(request.userId(), request.shippingAddress());
        for (OrderLineRequest line : request.items()) {
            Product product = productRepository.findById(line.productId())
                    .orElseThrow(() -> new ResourceNotFoundException("Produit", line.productId()));
            addLine(order, product, line.quantity());
        }
        return mapper.toResponse(saveWithPayment(order, request.paymentMethod()));
    }

    @Transactional(readOnly = true)
    public OrderResponse findById(Long id) {
        return mapper.toResponse(getEntity(id));
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> findByUser(String userId) {
        return orderRepository.findByUserId(userId).stream().map(mapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> findBySeller(Long sellerId) {
        return orderRepository.findDistinctByItemsSellerId(sellerId).stream().map(mapper::toResponse).toList();
    }

    // Annulation : remet le stock et rembourse si deja paye
    public OrderResponse cancel(Long id) {
        Order order = getEntity(id);
        order.cancel();
        for (OrderItem item : order.getItems()) {
            item.getProduct().increaseStock(item.getQuantity());
        }
        Payment payment = order.getPayment();
        if (payment != null) {
            if (payment.getStatus() == PaymentStatus.SUCCESS) {
                payment.refund();
            } else if (payment.getStatus() == PaymentStatus.PENDING) {
                payment.setStatus(PaymentStatus.FAILED);
            }
        }
        return mapper.toResponse(order);
    }

    private Order newOrder(String userId, String shippingAddress) {
        Order order = new Order();
        order.setUserId(userId);
        order.setShippingAddress(shippingAddress);
        return order;
    }

    // Le stock baisse en meme temps que la ligne est ajoutee
    private void addLine(Order order, Product product, int quantity) {
        product.decreaseStock(quantity);
        order.addItem(product, quantity);
    }

    private Order saveWithPayment(Order order, PaymentMethod method) {
        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setMethod(method);
        payment.setAmount(order.getTotalAmount());
        order.setPayment(payment);
        return orderRepository.save(order);
    }

    private Order getEntity(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Commande", id));
    }
}