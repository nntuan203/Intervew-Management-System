package com.fa.ims.service.impl;

import com.fa.ims.entity.Department;
import com.fa.ims.repository.DepartRepository;
import com.fa.ims.service.DepartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartServiceImpl implements DepartService {
    @Autowired
    private DepartRepository departRepository;

    @Override
    public List<Department> getAllDepart() {
        return departRepository.findAll();
    }
}
