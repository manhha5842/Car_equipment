package com.car_equipment.Controller;

import com.car_equipment.DTO.CartDTO;
import com.car_equipment.Service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carts")
public class CartController {

    @Autowired
    private CartService cartService;

    // Thêm sản phẩm vào giỏ hàng thông qua ID của User
    @PostMapping("/user/{userId}/products/{productId}")
    public ResponseEntity<CartDTO> addProductToCartByUserId(@PathVariable String userId, @PathVariable String productId, @RequestParam int quantity) {
        System.out.println("o day");
        CartDTO updatedCart = cartService.addProductToCartByUserId(userId, productId, quantity);
        if (updatedCart != null) {
            return ResponseEntity.ok(updatedCart);
        }
        return ResponseEntity.notFound().build();
    }


    // Giảm số lượn sản phẩm giỏ hàng thông qua ID của User
    @PostMapping("/user/{userId}/removeProducts/{productId}")
    public ResponseEntity<CartDTO> removeQuantityProductToCart(@PathVariable String userId, @PathVariable String productId, @RequestParam int quantity) {
        CartDTO updatedCart = cartService.removeProductQuantityFromCart(userId, productId, quantity);
        if (updatedCart != null) {
            return ResponseEntity.ok(updatedCart);
        }
        return ResponseEntity.notFound().build();
    }

    // Lấy giỏ hàng theo ID
    @GetMapping("/{id}")
    public ResponseEntity<CartDTO> getCartById(@PathVariable String id) {
        CartDTO cart = cartService.getCartById(id);
        if (cart != null) {
            return ResponseEntity.ok(cart);
        }
        return ResponseEntity.notFound().build();
    }

    // Lấy giỏ hàng theo User
    @GetMapping("/user/{userId}")
    public ResponseEntity<CartDTO> getCartByUser(@PathVariable String userId) {
        CartDTO cart = cartService.getCartByUser(userId);
        if (cart != null) {
            return ResponseEntity.ok(cart);
        }
        return ResponseEntity.notFound().build();
    }

    // Xoá từng sản phẩm khỏi giỏ hàng
    @DeleteMapping("/{cartId}/products/{productId}")
    public ResponseEntity<CartDTO> removeProductFromCart(@PathVariable String cartId, @PathVariable String productId) {
        CartDTO updatedCart = cartService.removeProductFromCart(cartId, productId);
        if (updatedCart != null) {
            return ResponseEntity.ok(updatedCart);
        }
        return ResponseEntity.notFound().build();
    }

    // Xoá hết sản phẩm khỏi giỏ hàng
    @DeleteMapping("/{cartId}/products")
    public ResponseEntity<CartDTO> clearCart(@PathVariable String cartId) {
        CartDTO updatedCart = cartService.clearCart(cartId);
        if (updatedCart != null) {
            return ResponseEntity.ok(updatedCart);
        }
        return ResponseEntity.notFound().build();
    }
}