package com.car_equipment.Service;

import com.car_equipment.DTO.CartDTO;
import com.car_equipment.Model.Cart;
import com.car_equipment.Model.CartProduct;
import com.car_equipment.Model.CartProductId;
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
        System.out.println("user" +userOptional.isPresent());
        System.out.println("product" +productOptional.isPresent());
        if (userOptional.isPresent() && productOptional.isPresent()) {
            User user = userOptional.get();
            Cart cart = cartRepository.findByUser(user).orElseGet(() -> {
                Cart newCart = new Cart();
                newCart.setUser(user);
                return newCart;
            });

            Product product = productOptional.get();
            CartProductId cartProductId = new CartProductId();
            cartProductId.setCartId(cart.getId());
            cartProductId.setProductId(product.getId());

            CartProduct cartProduct = cart.getCartProducts().stream()
                    .filter(cp -> cp.getId().equals(cartProductId))
                    .findFirst()
                    .orElseGet(() -> {
                        CartProduct newCartProduct = new CartProduct();
                        newCartProduct.setId(cartProductId);
                        newCartProduct.setCart(cart);
                        newCartProduct.setProduct(product);
                        newCartProduct.setQuantity(0);
                        cart.getCartProducts().add(newCartProduct);
                        return newCartProduct;
                    });

            cartProduct.setQuantity(cartProduct.getQuantity() + quantity);
            Cart updatedCart = cartRepository.save(cart);
            return CartDTO.transferToDTO(updatedCart);
        }
        return null;
    }

    // Giảm số lượng sản phẩm trong giỏ hàng
    public CartDTO removeProductQuantityFromCart(String userId, String productId,int quantity) {
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
            CartProductId cartProductId = new CartProductId();
            cartProductId.setCartId(cart.getId());
            cartProductId.setProductId(product.getId());

            CartProduct cartProduct = cart.getCartProducts().stream()
                    .filter(cp -> cp.getId().equals(cartProductId))
                    .findFirst()
                    .orElse(null);

            if (cartProduct == null) {
                return null; // Sản phẩm không có trong giỏ hàng
            }

            if (cartProduct.getQuantity() - quantity >= 1) {
                cartProduct.setQuantity(cartProduct.getQuantity() - quantity);
            } else {
                cart.getCartProducts().remove(cartProduct); // Xóa sản phẩm nếu số lượng bằng 1
            }

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

            CartProductId cartProductId = new CartProductId();
            cartProductId.setCartId(cart.getId());
            cartProductId.setProductId(product.getId());

            CartProduct cartProduct = cart.getCartProducts().stream()
                    .filter(cp -> cp.getId().equals(cartProductId))
                    .findFirst()
                    .orElseGet(() -> {
                        CartProduct newCartProduct = new CartProduct();
                        newCartProduct.setId(cartProductId);
                        newCartProduct.setCart(cart);
                        newCartProduct.setProduct(product);
                        newCartProduct.setQuantity(0);
                        cart.getCartProducts().add(newCartProduct);
                        return newCartProduct;
                    });

            cartProduct.setQuantity(cartProduct.getQuantity() + quantity);
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
            CartProductId cartProductId = new CartProductId();
            cartProductId.setCartId(cart.getId());
            cartProductId.setProductId(product.getId());

            cart.getCartProducts().removeIf(cp -> cp.getId().equals(cartProductId));
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