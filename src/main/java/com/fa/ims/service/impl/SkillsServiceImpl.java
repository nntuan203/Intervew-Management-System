package com.fa.ims.service.impl;

import com.fa.ims.entity.Skill;
import com.fa.ims.repository.SkillsRepository;
import com.fa.ims.service.SkillsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class SkillsServiceImpl implements SkillsService {

    @Autowired
    SkillsRepository skillsRepository;

    @Override
    public List<Skill> getAllSkills() {
        return skillsRepository.findAll();
    }

    @Override
    public Optional<Skill> findById(Long skillId) {
        return skillsRepository.findById(skillId);
    }
}
