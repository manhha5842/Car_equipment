package com.car_equipment.Controller;


import com.car_equipment.Config.JwtTokenProvider;
import com.car_equipment.DTO.*;
import com.car_equipment.Model.User;
import com.car_equipment.Service.AddressService;
import com.car_equipment.Service.UserService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.imagekit.sdk.ImageKit;
import io.imagekit.sdk.config.Configuration;
import io.imagekit.sdk.exceptions.*;
import io.imagekit.sdk.models.FileCreateRequest;
import io.imagekit.sdk.models.results.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Autowired
    private UserService userService;
    @Autowired
    private AddressService addressService;
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
        return ResponseEntity.ok().body(getRespone(userService.saveUser(newUser)));
    }

    @PostMapping("/updateInfo")
    public ResponseEntity<?> updateInfo(
            @RequestParam("id") String id,
            @RequestParam("email") String email,
            @RequestParam("fullName") String fullName,
            @RequestParam("addresses") String addressesJson,
            @RequestParam("phoneNumber") String phoneNumber,
            @RequestParam("image") MultipartFile image) throws IOException, ForbiddenException, TooManyRequestsException, InternalServerException, UnauthorizedException, BadRequestException, UnknownException {

        try {
            // Chuyển đổi JSON của addresses thành Set<AddressDTO>
            ObjectMapper objectMapper = new ObjectMapper();
            Set<AddressDTO> addresses = objectMapper.readValue(addressesJson, new TypeReference<Set<AddressDTO>>() {
            });

            User user = userService.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));
            user.setEmail(email);
            user.setFullName(fullName);
            user.setPhoneNumber(phoneNumber);

            // Xử lý ảnh với ImageKit
            ImageKit imageKit = ImageKit.getInstance();
            Configuration config = new Configuration("public_YiJMjxdBcy00loCsmDp848aKnBM=", "private_y16gn+wwe5b3peEkVWUqy44bfT8=", "https://ik.imagekit.io/manhha5842/newsAPI");
            imageKit.setConfig(config);
            byte[] bytes = image.getBytes();
            FileCreateRequest fileCreateRequestRequest = new FileCreateRequest(bytes,
                    id.replace(" ", "_") + "" + new Timestamp(System.currentTimeMillis()).from(Instant.now())
                            + ".jpg");
            fileCreateRequestRequest.setUseUniqueFileName(false);
            Result result = ImageKit.getInstance().upload(fileCreateRequestRequest);
            String imagePath = result.getResponseMetaData().getMap().get("url").toString();
            user.setAvatar(imagePath);

            // Xử lý địa chỉ
            for (AddressDTO addressDTO : addresses) {
                addressDTO.setUserId(id);
                addressService.addAddress(addressDTO);
            }

            return ResponseEntity.ok().body(getRespone(userService.saveUser(user)));
        }catch (IllegalStateException e) {
            System.out.println(e);
            return ResponseEntity.badRequest().body(e);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginDTO loginDTO) {
        // Kiểm tra user có tồn tại trong hệ thống không dựa vào email
        Optional<User> user = userService.findByEmail(loginDTO.getEmail());
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
        }

        // Kiểm tra mật khẩu có khớp không
        User foundUser = user.get();
        if (!passwordEncoder.matches(loginDTO.getPassword(), foundUser.getPassword())) {
            System.out.println(passwordEncoder.encode(loginDTO.getPassword()));
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password");
        }

        return ResponseEntity.ok().body(getRespone(foundUser));
    }

    @PostMapping("/loginWithGoogle")
    public ResponseEntity<?> loginWithGoogle(@RequestBody UserLoginWithGoogleDTO loginDTO) {
        // Kiểm tra user có tồn tại trong hệ thống không dựa vào email
        Optional<User> user = userService.findByEmail(loginDTO.getEmail());
        if (user.isPresent()) {
            if (!loginDTO.getAvatar().isEmpty()) user.get().setAvatar(loginDTO.getAvatar());
            if (!loginDTO.getFullName().isEmpty()) user.get().setFullName(loginDTO.getFullName());
            if (!loginDTO.getPhoneNumber().isEmpty()) user.get().setPhoneNumber(loginDTO.getPhoneNumber());
            return ResponseEntity.ok().body(getRespone(userService.saveUser(user.get())));
        } else {
            User newUser = new User();
            newUser.setId(loginDTO.getId());
            newUser.setFullName(loginDTO.getFullName());
            newUser.setEmail(loginDTO.getEmail());
            newUser.setPhoneNumber(loginDTO.getPhoneNumber());
            newUser.setAvatar(loginDTO.getAvatar());
            newUser.setRole("USER");
            return ResponseEntity.ok().body(getRespone(userService.saveUser(newUser)));
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

        UserInfoDTO userInfoDTO = new UserInfoDTO(user.getId(), user.getEmail(), user.getFullName(), user.getAddresses().stream().map(AddressDTO::transferToDTO).collect(Collectors.toSet()), user.getPhoneNumber(), user.getAvatar(), user.getRole());
        Map<String, Object> response = new HashMap<>();
        response.put("user", userInfoDTO);
        response.put("token", token);
        return response;
    }
}
