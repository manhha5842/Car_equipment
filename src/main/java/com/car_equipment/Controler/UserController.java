package com.car_equipment.Controler;


import com.car_equipment.Config.JwtTokenProvider;
import com.car_equipment.DTO.UserLoginDTO;
import com.car_equipment.DTO.UserLoginWithGoogleDTO;
import com.car_equipment.DTO.UserRegistrationDTO;
import com.car_equipment.Model.User;
import com.car_equipment.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    public UserController() {
    }


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRegistrationDTO registrationDTO) {
        if (userService.findByEmail(registrationDTO.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email already in use");
        }
        User newUser = new User();
        newUser.setFullName(registrationDTO.getFullName());
        newUser.setEmail(registrationDTO.getEmail());
        newUser.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
        newUser.setPhoneNumber(registrationDTO.getPhoneNumber());
        newUser.setRole("USER");
        System.out.println("đúng" + newUser);
        userService.saveUser(newUser);

        String token = jwtTokenProvider.createToken(newUser.getEmail(), newUser.getRole());

        // Trả về JWT token cho người dùng
        return ResponseEntity.ok().body("Bearer " + token);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginDTO loginDTO) {
        // Kiểm tra user có tồn tại trong hệ thống không dựa vào email
        Optional<User> user = userService.findByEmail(loginDTO.getEmail());
        if (!user.isPresent()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
        }

        // Kiểm tra mật khẩu có khớp không
        User foundUser = user.get();
        System.out.println("foundUser: " + foundUser.getPassword());
        System.out.println("input: " + passwordEncoder.encode(loginDTO.getPassword()));
        if (!passwordEncoder.matches(loginDTO.getPassword(), foundUser.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password");
        }

        // Tại đây, thông tin đăng nhập là hợp lệ và đúng, tạo JWT token
        String token = jwtTokenProvider.createToken(foundUser.getEmail(), foundUser.getRole());

        // Trả về JWT token cho người dùng
        return ResponseEntity.ok().body("Bearer " + token);
    }

    @PostMapping("/loginWithGoogle")
    public ResponseEntity<?> loginWithGoogle(@RequestBody UserLoginWithGoogleDTO loginDTO) {
        // Kiểm tra user có tồn tại trong hệ thống không dựa vào email
        Optional<User> user = userService.findByEmail(loginDTO.getEmail());
        if (!user.isPresent()) {
            String token = jwtTokenProvider.createToken(user.get().getEmail(), user.get().getRole());
            // Trả về JWT token cho người dùng
            return ResponseEntity.ok().body("Bearer " + token);
        }else {
            User newUser = new User();
            newUser.setFullName(loginDTO.getFullName());
            newUser.setEmail(loginDTO.getEmail());
            newUser.setRole("USER");
            System.out.println("đúng" + newUser);
            userService.saveUser(newUser);
            String token = jwtTokenProvider.createToken(newUser.getEmail(), newUser.getRole());

            // Trả về JWT token cho người dùng
            return ResponseEntity.ok().body("Bearer " + token);
        }

    }

    @PostMapping("/updatePassword")
    public ResponseEntity<?> updatePassword() {
        return null;
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<?> resetPassword() {
        return null;
    }

}
