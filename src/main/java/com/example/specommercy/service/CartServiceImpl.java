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
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
public class CartServiceImpl implements CartService{

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;
    private final AuthUtil authUtil;
    private final ModelMapper modelMapper;

    @Autowired
    public CartServiceImpl(CartRepository cartRepository, ProductRepository productRepository, CartItemRepository cartItemRepository, AuthUtil authUtil, ModelMapper modelMapper) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.cartItemRepository = cartItemRepository;
        this.authUtil = authUtil;
        this.modelMapper = modelMapper;
    }

    @Override
    public CartDTO addProductToCart(Long productId, Integer quantity) {
        Cart cart  = createCart();

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cart.getCartId(), productId);

        if (cartItem != null) {
            throw new APIException("Product " + product.getProductName() + " already exists in the cart");
        }

        if (product.getQuantity() == 0) {
            throw new APIException(product.getProductName() + " is not available");
        }

        if (product.getQuantity() < quantity) {
            throw new APIException("Please, make an order of the " + product.getProductName()
                    + " less than or equal to the quantity " + product.getQuantity() + ".");
        }

        CartItem newCartItem = new CartItem();

        newCartItem.setProduct(product);
        newCartItem.setCart(cart);
        newCartItem.setQuantity(quantity);
        newCartItem.setDiscount(product.getDiscount());
        newCartItem.setProductPrice(product.getSpecialPrice());

        cartItemRepository.save(newCartItem);

        product.setQuantity(product.getQuantity());

        cart.setTotalPrice(cart.getTotalPrice() + (product.getSpecialPrice() * quantity));

        cartRepository.save(cart);

        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);

        List<CartItem> cartItems = cart.getCartItems();

        Stream<ProductDTO> productStream = cartItems.stream().map(item -> {
            ProductDTO map = modelMapper.map(item.getProduct(), ProductDTO.class);
            map.setQuantity(item.getQuantity());
            return map;
        });

        cartDTO.setProducts(productStream.toList());

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

    @Transactional
    @Override
    public CartDTO updateProductQuantityInCart(Long productId, Integer quantity) {


        Cart userCart = cartRepository.findCartByUsername(authUtil.loggedInUsername());
        Long cartId  = userCart.getCartId();

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        if (product.getQuantity() == 0) {
            throw new APIException(product.getProductName() + " is not available");
        }

        if (product.getQuantity() < quantity) {
            throw new APIException("Please, make an order of the " + product.getProductName()
                    + " less than or equal to the quantity " + product.getQuantity() + ".");
        }

        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cartId, productId);

        if (cartItem == null) {
            throw new APIException("Product " + product.getProductName() + " not available in the cart!!!");
        }

        cartItem.setProductPrice(product.getSpecialPrice());
        cartItem.setQuantity(cartItem.getQuantity() + quantity);
        cartItem.setDiscount(product.getDiscount());
        cart.setTotalPrice(cart.getTotalPrice() + (cartItem.getProductPrice() * quantity));
        cartRepository.save(cart);
        CartItem updatedItem = cartItemRepository.save(cartItem);
        if(updatedItem.getQuantity() == 0){
            cartItemRepository.deleteById(updatedItem.getCartItemId());
        }


        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);

        List<CartItem> cartItems = cart.getCartItems();

        Stream<ProductDTO> productStream = cartItems.stream().map(item -> {
            ProductDTO prd = modelMapper.map(item.getProduct(), ProductDTO.class);
            prd.setQuantity(item.getQuantity());
            return prd;
        });


        cartDTO.setProducts(productStream.toList());

        return cartDTO;
    }

    @Override
    public String deleteProductFromCart(Long cartId, Long productId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));

        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cartId, productId);

        if (cartItem == null) {
            throw new ResourceNotFoundException("Product", "productId", productId);
        }

        cart.setTotalPrice(cart.getTotalPrice() -
                (cartItem.getProductPrice() * cartItem.getQuantity()));

        cartItemRepository.deleteCartItemByProductIdAndCartId(cartId, productId);

        return "Product " + cartItem.getProduct().getProductName() + " removed from the cart !!!";
    }

    private Cart createCart() {
        Cart userCart = cartRepository.findCartByUsername(authUtil.loggedInUsername());
        if (userCart != null)
            return userCart;
        Cart createdCart = new Cart();
        createdCart.setUser(authUtil.loggedInUser());
        return cartRepository.save(createdCart);
    }
    private Product isProductAvailable(int quantity,Long productId){
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("product", "productId", productId));
        int existingProductQuantity = product.getQuantity();
        if (existingProductQuantity == 0)
            throw new APIException(product.getProductName() + " is not available");
        if(existingProductQuantity < quantity)
            throw new APIException("The required product quantity isn't available");
        return product;
    }
}
