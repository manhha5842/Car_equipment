package com.car_equipment.Service;

import com.car_equipment.DTO.CartDTO;
import com.car_equipment.DTO.ProductDTO;
import com.car_equipment.Model.Cart;
import com.car_equipment.Model.Product;
import com.car_equipment.Model.User;
import com.car_equipment.Repository.CartRepository;
import com.car_equipment.Repository.ProductRepository;
import com.car_equipment.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    // Thêm sản phẩm vào giỏ hàng
    public CartDTO addProductToCart(String cartId, String productId) {
        Optional<Cart> cartOptional = cartRepository.findById(cartId);
        Optional<Product> productOptional = productRepository.findById(productId);

        if (cartOptional.isPresent() && productOptional.isPresent()) {
            Cart cart = cartOptional.get();
            Product product = productOptional.get();
            cart.getProducts().add(product);
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
            cart.getProducts().remove(product);
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
            cart.getProducts().clear();
            Cart updatedCart = cartRepository.save(cart);
            return CartDTO.transferToDTO(updatedCart);
        }
        return null;
    }
}