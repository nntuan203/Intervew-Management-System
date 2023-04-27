package com.fa.ims.repository;

import com.fa.ims.entity.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SkillsRepository extends JpaRepository<Skill, Long> {

    Skill findSkillBySkillsId(Long skillId);

    Skill findSkillBySkillsDescLike(String skillName);
}
