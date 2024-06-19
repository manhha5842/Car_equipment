package com.car_equipment.Service;

import com.car_equipment.DTO.CartDTO;
import com.car_equipment.Model.Cart;
import com.car_equipment.Model.CartProduct;
import com.car_equipment.Model.Product;
import com.car_equipment.Model.User;
import com.car_equipment.Repository.CartRepository;
import com.car_equipment.Repository.ProductRepository;
import com.car_equipment.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    // Thêm sản phẩm vào giỏ hàng thông qua ID của User
    public CartDTO addProductToCartByUserId(String userId, String productId, int quantity) {
        Optional<User> userOptional = userRepository.findById(userId);
        Optional<Product> productOptional = productRepository.findById(productId);

        if (userOptional.isPresent() && productOptional.isPresent()) {
            User user = userOptional.get();
            Cart cart = cartRepository.findByUser(user).orElseGet(() -> {
                Cart newCart = new Cart();
                newCart.setUser(user);
                return newCart;
            });

            Product product = productOptional.get();
            CartProduct cartProduct = new CartProduct();
            cartProduct.setCart(cart);
            cartProduct.setProduct(product);
            cartProduct.setQuantity(quantity);

            cart.getCartProducts().add(cartProduct);
            Cart updatedCart = cartRepository.save(cart);
            return CartDTO.transferToDTO(updatedCart);
        }
        return null;
    }

    // Thêm sản phẩm vào giỏ hàng
    public CartDTO addProductToCart(String cartId, String productId, int quantity) {
        Optional<Cart> cartOptional = cartRepository.findById(cartId);
        Optional<Product> productOptional = productRepository.findById(productId);

        if (cartOptional.isPresent() && productOptional.isPresent()) {
            Cart cart = cartOptional.get();
            Product product = productOptional.get();

            boolean productExists = cart.getCartProducts().stream()
                    .anyMatch(cp -> cp.getProduct().getId().equals(product.getId()));

            if (productExists) {
                cart.getCartProducts().forEach(cp -> {
                    if (cp.getProduct().getId().equals(product.getId())) {
                        cp.setQuantity(cp.getQuantity() + quantity);
                    }
                });
            } else {
                CartProduct cartProduct = new CartProduct();
                cartProduct.setCart(cart);
                cartProduct.setProduct(product);
                cartProduct.setQuantity(quantity);
                cart.getCartProducts().add(cartProduct);
            }

            Cart updatedCart = cartRepository.save(cart);
            return CartDTO.transferToDTO(updatedCart);
        }
        return null;
    }

    // Lấy giỏ hàng theo ID
    public CartDTO getCartById(String id) {
        Optional<Cart> cart = cartRepository.findById(id);
        return cart.map(CartDTO::transferToDTO).orElse(null);
    }

    // Lấy giỏ hàng theo User
    public CartDTO getCartByUser(String userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Optional<Cart> cart = cartRepository.findByUser(user);
            return cart.map(CartDTO::transferToDTO).orElse(null);
        }
        return null;
    }

    // Xoá từng sản phẩm khỏi giỏ hàng
    public CartDTO removeProductFromCart(String cartId, String productId) {
        Optional<Cart> cartOptional = cartRepository.findById(cartId);
        Optional<Product> productOptional = productRepository.findById(productId);

        if (cartOptional.isPresent() && productOptional.isPresent()) {
            Cart cart = cartOptional.get();
            Product product = productOptional.get();
            cart.getCartProducts().removeIf(cp -> cp.getProduct().getId().equals(product.getId()));
            Cart updatedCart = cartRepository.save(cart);
            return CartDTO.transferToDTO(updatedCart);
        }
        return null;
    }

    // Xoá hết sản phẩm khỏi giỏ hàng
    public CartDTO clearCart(String cartId) {
        Optional<Cart> cartOptional = cartRepository.findById(cartId);

        if (cartOptional.isPresent()) {
            Cart cart = cartOptional.get();
            cart.getCartProducts().clear();
            Cart updatedCart = cartRepository.save(cart);
            return CartDTO.transferToDTO(updatedCart);
        }
        return null;
    }
}