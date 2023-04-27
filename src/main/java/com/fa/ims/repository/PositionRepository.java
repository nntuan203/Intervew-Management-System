package com.fa.ims.repository;

import com.fa.ims.entity.Position;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PositionRepository extends JpaRepository<Position, Long> {

    Position findPositionByPositionId(Long id);
}
