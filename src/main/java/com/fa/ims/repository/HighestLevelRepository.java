package com.fa.ims.repository;

import com.fa.ims.entity.HighestLevel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HighestLevelRepository extends JpaRepository<HighestLevel, Long> {

    HighestLevel findByHighestId(Long highestId);
}
