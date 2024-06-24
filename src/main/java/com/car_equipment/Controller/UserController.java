package com.car_equipment.Controller;


import com.car_equipment.Config.JwtTokenProvider;
import com.car_equipment.DTO.*;
import com.car_equipment.Model.User;
import com.car_equipment.Service.AddressService;
import com.car_equipment.Service.UserService;
import io.imagekit.sdk.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
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
    @Value("${FreeimageHostApiKey}")
    private String freeimageHostApiKey;

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
            @RequestParam("phoneNumber") String phoneNumber,
            @RequestParam(value = "image", required = false) MultipartFile image) throws IOException, ForbiddenException, TooManyRequestsException, InternalServerException, UnauthorizedException, BadRequestException, UnknownException {

        try {


            User user = userService.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));
            user.setEmail(email);
            user.setFullName(fullName);
            user.setPhoneNumber(phoneNumber);


            // Lưu tệp ảnh nếu có
            if (image != null && !image.isEmpty()) {
                String imagePath = uploadImageToFreeimageHost(image);
                user.setAvatar(imagePath);
            }
            return ResponseEntity.ok().body(getRespone(userService.saveUser(user)));
        } catch (IllegalStateException e) {
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
        Optional<User> user = userService.findById(loginDTO.getId());
        if (user.isPresent()) {
            if (!loginDTO.getEmail().isEmpty()) user.get().setEmail(loginDTO.getEmail());
            if (!loginDTO.getAvatar().isEmpty()) user.get().setAvatar(loginDTO.getAvatar());
            if (!loginDTO.getFullName().isEmpty()) user.get().setFullName(loginDTO.getFullName());
            if (!loginDTO.getPhoneNumber().isEmpty()) user.get().setPhoneNumber(loginDTO.getPhoneNumber());
            return ResponseEntity.ok().body(getRespone(userService.saveUser(user.get())));
        } else {
            User newUser = new User();
            newUser.setId(loginDTO.getId());
            if (!loginDTO.getEmail().isEmpty()) newUser.setEmail(loginDTO.getEmail());
            if (!loginDTO.getAvatar().isEmpty()) newUser.setAvatar(loginDTO.getAvatar());
            if (!loginDTO.getFullName().isEmpty()) newUser.setFullName(loginDTO.getFullName());
            if (!loginDTO.getPhoneNumber().isEmpty()) newUser.setPhoneNumber(loginDTO.getPhoneNumber());
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
    public ResponseEntity<?> resetPassword(@RequestParam("email") String email) {
        System.out.println(email);
        User user = userService.findByEmail(email).orElse(null);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        String ALPHABET = "abcdefghijklmnopqrstuvwxyz1234567890";
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder(10);
        for (int i = 0; i < 10; i++) {
            stringBuilder.append(ALPHABET.charAt(random.nextInt(ALPHABET.length())));
        }
        String password = stringBuilder.toString();

        String newPassword = passwordEncoder.encode(password);
        user.setPassword(newPassword);
        userService.saveUser(user);
        Map<String, Object> response = new HashMap<>();
        response.put("password", password);
        return ResponseEntity.ok().body(response);
    }

    private Map<String, Object> getRespone(User user) {
        String token = jwtTokenProvider.createToken(user.getEmail(), user.getRole());

        // Kiểm tra nếu addresses là null
        Set<AddressDTO> addressDTOs = user.getAddresses() != null ?
                user.getAddresses().stream().map(AddressDTO::transferToDTO).collect(Collectors.toSet()) :
                Collections.emptySet();

        UserInfoDTO userInfoDTO = new UserInfoDTO(
                user.getId(),
                user.getEmail(),
                user.getFullName(),
                addressDTOs,
                user.getPhoneNumber(),
                user.getAvatar(),
                user.getRole()
        );

        Map<String, Object> response = new HashMap<>();
        response.put("user", userInfoDTO);
        response.put("token", token);
        return response;
    }

    private String uploadImageToFreeimageHost(MultipartFile image) throws IOException {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("key", freeimageHostApiKey);
        body.add("action", "upload");
        body.add("source", new ByteArrayResource(image.getBytes()) {
            @Override
            public String getFilename() {
                return image.getOriginalFilename();
            }
        });

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                "https://freeimage.host/api/1/upload",
                HttpMethod.POST,
                requestEntity,
                Map.class
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            Map<String, Object> responseBody = response.getBody();
            if (responseBody != null && responseBody.containsKey("image")) {
                Map<String, Object> imageInfo = (Map<String, Object>) responseBody.get("image");
                return (String) imageInfo.get("url");
            }
        }

        throw new IOException("Failed to upload image to Freeimage.host");
    }
}
