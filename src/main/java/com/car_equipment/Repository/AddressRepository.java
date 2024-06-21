package com.car_equipment.Repository;

import com.car_equipment.Model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address, String> {
    List<Address> findByUserId(String userId);
    List<Address> findByUserIdAndStatusNot(String userId, String status);

}