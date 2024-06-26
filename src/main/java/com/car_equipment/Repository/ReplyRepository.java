package com.car_equipment.Repository;

import com.car_equipment.Model.Address;
import com.car_equipment.Model.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReplyRepository extends JpaRepository<Reply, String> {
    List<Reply> findByCommentId(String userId);
}