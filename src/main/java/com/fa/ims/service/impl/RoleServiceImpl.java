package com.fa.ims.service.impl;


import com.fa.ims.entity.Role;
import com.fa.ims.repository.RoleRepository;
import com.fa.ims.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;


    @Override
    public List<Role> getAllRole() {
        return roleRepository.findAll();
    }
}
