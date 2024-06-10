package com.car_equipment.Repository;

import com.car_equipment.Model.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReplyRepository extends JpaRepository<Reply, String> {
}