package com.car_equipment.Controler;


import com.car_equipment.Config.JwtTokenProvider;
import com.car_equipment.DTO.*;
import com.car_equipment.Model.User;
import com.car_equipment.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
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
        userService.saveUser(newUser);
        return ResponseEntity.ok().body(getRespone(userService.findByEmail(newUser.getEmail()).get()));
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
        if (!passwordEncoder.matches(loginDTO.getPassword(), foundUser.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password");
        }

        return ResponseEntity.ok().body(getRespone(foundUser));
    }

    @PostMapping("/loginWithGoogle")
    public ResponseEntity<?> loginWithGoogle(@RequestBody UserLoginWithGoogleDTO loginDTO) {
        // Kiểm tra user có tồn tại trong hệ thống không dựa vào email
        Optional<User> user = userService.findByEmail(loginDTO.getEmail());
        if (user.isPresent()) {
            return ResponseEntity.ok().body(getRespone(user.get()));
        } else {
            User newUser = new User();
            newUser.setFullName(loginDTO.getFullName());
            newUser.setEmail(loginDTO.getEmail());
            newUser.setRole("USER");
            userService.saveUser(newUser);

            User foundUser = userService.findByEmail(loginDTO.getEmail()).get();
            return ResponseEntity.ok().body(getRespone(foundUser));
        }

    }

    @PostMapping("/updatePassword")
    public ResponseEntity<?> updatePassword(@AuthenticationPrincipal User userDetails, @RequestBody UpdatePasswordDTO updatePasswordDTO) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated");
        }

        // Kiểm tra mật khẩu hiện tại
        User user = userService.findByEmail(userDetails.getEmail()).orElse(null);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        // So sánh mật khẩu hiện tại với mật khẩu trong request
        if (!passwordEncoder.matches(updatePasswordDTO.getCurrentPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Current password is incorrect");
        }
        // Cập nhật mật khẩu và lưu vào database
        System.out.println("new password " + updatePasswordDTO.getNewPassword());
        String newPassword = passwordEncoder.encode(updatePasswordDTO.getNewPassword());
        System.out.println(newPassword);
        user.setPassword(newPassword);
        userService.saveUser(user);

        return ResponseEntity.ok().body("Password updated successfully");
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<?> resetPassword() {
        return null;
    }

    private Map<String, Object> getRespone(User user) {
        String token = jwtTokenProvider.createToken(user.getEmail(), user.getRole());

        UserInfoDTO userInfoDTO = new UserInfoDTO(user.getId(), user.getEmail(), user.getFullName(), user.getAddresses(), user.getPhoneNumber(), user.getAvatar(), user.getRole());
        Map<String, Object> response = new HashMap<>();
        response.put("user", userInfoDTO);
        response.put("token", token);
        return response;
    }
}
