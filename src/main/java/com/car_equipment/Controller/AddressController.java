package com.car_equipment.Controller;

import com.car_equipment.DTO.AddressDTO;
import com.car_equipment.Service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
public class AddressController {

    @Autowired
    private AddressService addressService;

    // Lấy danh sách địa chỉ của user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AddressDTO>> getAddressesByUserId(@PathVariable String userId) {
        List<AddressDTO> addresses = addressService.getAddressesActiveByUserId(userId);
        return ResponseEntity.ok(addresses);
    }

    // Xem chi tiết địa chỉ
    @GetMapping("/{id}")
    public ResponseEntity<AddressDTO> getAddressById(@PathVariable String id) {
        AddressDTO address = addressService.getAddressById(id);
        if (address != null) {
            return ResponseEntity.ok(address);
        }
        return ResponseEntity.notFound().build();
    }

    // Thêm địa chỉ
    @PostMapping
    public ResponseEntity<AddressDTO> addAddress(@RequestBody AddressDTO addressDTO) {
        AddressDTO newAddress = addressService.addAddress(addressDTO);
        if (newAddress != null) {
            return ResponseEntity.ok(newAddress);
        }
        return ResponseEntity.badRequest().build();
    }

    // Sửa địa chỉ
    @PutMapping("/{id}")
    public ResponseEntity<AddressDTO> updateAddress(@PathVariable String id, @RequestBody AddressDTO addressDTO) {
        AddressDTO updatedAddress = addressService.updateAddress(id, addressDTO);
        if (updatedAddress != null) {
            return ResponseEntity.ok(updatedAddress);
        }
        return ResponseEntity.notFound().build();
    }

    // Xoá địa chỉ
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAddress(@PathVariable String id) {
        boolean isDeleted = addressService.deleteAddress(id);
        if (isDeleted) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}