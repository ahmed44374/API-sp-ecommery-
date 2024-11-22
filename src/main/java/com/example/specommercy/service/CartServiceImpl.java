package com.example.specommercy.service;

import com.example.specommercy.exception.APIException;
import com.example.specommercy.exception.ResourceNotFoundException;
import com.example.specommercy.model.Cart;
import com.example.specommercy.model.CartItem;
import com.example.specommercy.model.Product;
import com.example.specommercy.model.User;
import com.example.specommercy.payload.CartDTO;
import com.example.specommercy.payload.ProductDTO;
import com.example.specommercy.repository.CartItemRepository;
import com.example.specommercy.repository.CartRepository;
import com.example.specommercy.repository.ProductRepository;
import com.example.specommercy.util.AuthUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class CartServiceImpl implements CartService{

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final AuthUtil authUtil;
    private final ModelMapper modelMapper;

    @Autowired
    public CartServiceImpl(CartRepository cartRepository, ProductRepository productRepository, AuthUtil authUtil, ModelMapper modelMapper) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.authUtil = authUtil;
        this.modelMapper = modelMapper;
    }

    @Override
    public CartDTO addProductToCart(Long productId, Integer quantity) {
        Cart cart = createCart();
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("product", "productId", productId));
        int existingProductQuantity = product.getQuantity();
        if (existingProductQuantity == 0)
            throw new APIException(product.getProductName() + " is not available");
        if(existingProductQuantity < quantity)
            throw new APIException("The required product quantity isn't available");
        CartItem cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setProductPrice(product.getPrice());
        cartItem.setDiscount(product.getDiscount());
        cartItem.setQuantity(quantity);
        cartItem.setCart(cart);

        cart.setTotalPrice(cart.getTotalPrice() + product.getPrice() * quantity);
        Cart savedCart = cartRepository.save(cart);
        CartDTO cartDTO = modelMapper.map(savedCart,CartDTO.class);

        List<CartItem> cartItems = savedCart.getCartItems();
        List<ProductDTO> productDTOS = cartItems.stream()
                .map(item -> {
                    return modelMapper.map(item.getProduct(),ProductDTO.class);
                }).toList();

        cartDTO.setProducts(productDTOS);
        return cartDTO;
    }

    @Override
    public List<CartDTO> getAllCarts() {
        List<Cart> carts = cartRepository.findAll();
        if(carts.isEmpty())
            throw  new APIException("No cart exists");

        return carts.stream().map(cart -> {
            CartDTO cartDTO = modelMapper.map(cart,CartDTO.class);
            List<ProductDTO> productDTOS = cart
                    .getCartItems()
                    .stream()
                    .map(cartItem -> modelMapper.map(cartItem.getProduct(),ProductDTO.class))
                    .toList();
            cartDTO.setProducts(productDTOS);
            return cartDTO;
        }).toList();
    }

    @Override
    public CartDTO getCart(String emailId, Long cartID) {
        return null;
    }

    @Override
    public CartDTO getLoggedInUserCart() {
        String currentUsername = authUtil.loggedInUsername();
        Cart cart = cartRepository.findCartByUsername(currentUsername);
        if(cart == null)
        {
            Cart newCart = new Cart();
            User user = authUtil.loggedInUser();
            newCart.setUser(user);
            return modelMapper.map(cartRepository.save(newCart),CartDTO.class);
        }
        List<ProductDTO> productDTOS = cart.getCartItems().stream()
                .map(cartItem -> modelMapper.map(cartItem.getProduct(),ProductDTO.class))
                .toList();

        CartDTO cartDTO = modelMapper.map(cart,CartDTO.class);
        cartDTO.setProducts(productDTOS);
        return cartDTO;
    }

    private Cart createCart() {
        Cart userCart = cartRepository.findCartByUsername(authUtil.loggedInUsername());
        if (userCart != null)
            return userCart;
        Cart createdCart = new Cart();
        createdCart.setUser(authUtil.loggedInUser());
        return cartRepository.save(createdCart);
    }
}
