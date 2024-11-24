package com.example.specommercy.service;

import com.example.specommercy.exception.APIException;
import com.example.specommercy.exception.ResourceNotFoundException;
import com.example.specommercy.model.*;
import com.example.specommercy.payload.OrderDTO;
import com.example.specommercy.payload.OrderItemDTO;
import com.example.specommercy.payload.PaymentInfoRequest;
import com.example.specommercy.payload.PaymentInfoResponse;
import com.example.specommercy.repository.*;
import com.stripe.exception.StripeException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
@Service
public class OrderServiceImpl implements OrderService{
    private final CartRepository cartRepository;
    private final AddressRepository addressRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final CartService cartService;
    private final ModelMapper modelMapper;
    private final ProductRepository productRepository;
    private final PaymentService paymentService;

    @Autowired
    public OrderServiceImpl(CartRepository cartRepository, AddressRepository addressRepository, OrderItemRepository orderItemRepository, OrderRepository orderRepository, PaymentRepository paymentRepository, CartService cartService, ModelMapper modelMapper, ProductRepository productRepository, PaymentService paymentService) {
        this.cartRepository = cartRepository;
        this.addressRepository = addressRepository;
        this.orderItemRepository = orderItemRepository;
        this.orderRepository = orderRepository;
        this.paymentRepository = paymentRepository;
        this.cartService = cartService;
        this.modelMapper = modelMapper;
        this.productRepository = productRepository;
        this.paymentService = paymentService;
    }

    @Override
    @Transactional
    public OrderDTO placeOrder(String emailId, Long addressId, String paymentMethod, String pgName, String pgPaymentId, String pgStatus, String pgResponseMessage) throws StripeException {
        Cart cart = cartRepository.findCartByEmail(emailId);
        if (cart == null) {
            throw new ResourceNotFoundException("Cart", "email", emailId);
        }

        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "addressId", addressId));

        Order order = new Order();
        order.setEmail(emailId);
        order.setOrderDate(LocalDate.now());
        order.setTotalAmount(cart.getTotalPrice());
        order.setOrderStatus("Order Accepted !");
        order.setAddress(address);

        List<CartItem> cartItems = cart.getCartItems();
        List<PaymentInfoRequest> paymentInfoRequests = cartItems.stream()
                .map(item -> {
                    PaymentInfoRequest paymentInfoRequest = new PaymentInfoRequest();
                    paymentInfoRequest.setQuantity((long)item.getQuantity());
                    paymentInfoRequest.setCurrency("usd");
                    paymentInfoRequest.setName(item.getProduct().getProductName());
                    paymentInfoRequest.setAmount(item.getProductPrice());
                    return paymentInfoRequest;
                }).toList();
        PaymentInfoResponse paymentInfoResponse = paymentService.checkoutProducts(paymentInfoRequests);
        Payment payment = new Payment(paymentInfoResponse.getPgMethod(), paymentInfoResponse.getSessionId(), paymentInfoResponse.getStatus(), paymentInfoResponse.getResponseMessage(), paymentInfoResponse.getPgName());
        payment.setOrder(order);
        payment = paymentRepository.save(payment);
        order.setPayment(payment);

        Order savedOrder = orderRepository.save(order);
        if (cartItems.isEmpty()) {
            throw new APIException("Cart is empty");
        }

        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setDiscount(cartItem.getDiscount());
            orderItem.setOrderedProductPrice(cartItem.getProductPrice());
            orderItem.setOrder(savedOrder);
            orderItems.add(orderItem);
        }

        orderItems = orderItemRepository.saveAll(orderItems);

        cart.getCartItems().forEach(item -> {
            int quantity = item.getQuantity();
            Product product = item.getProduct();

            // Reduce stock quantity
            product.setQuantity(product.getQuantity() - quantity);

            // Save product back to the database
            productRepository.save(product);

            // Remove items from cart
            cartService.deleteProductFromCart(cart.getCartId(), item.getProduct().getProductId());
        });

        OrderDTO orderDTO = modelMapper.map(savedOrder, OrderDTO.class);
        orderItems.forEach(item -> orderDTO.getOrderItems().add(modelMapper.map(item, OrderItemDTO.class)));

        orderDTO.setAddressId(addressId);
        orderDTO.setSessionUrl(paymentInfoResponse.getSessionUrl());
        return orderDTO;
    }
}
