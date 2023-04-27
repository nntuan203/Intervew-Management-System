package com.fa.ims.service;

import com.fa.ims.entity.Skill;

import java.util.List;
import java.util.Optional;

public interface SkillsService {
    List<Skill> getAllSkills();
    Optional<Skill> findById(Long skillId);
}
