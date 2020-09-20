package com.proyecto.flowmanagement.backend.service;

import com.proyecto.flowmanagement.backend.persistence.entity.Rol;
import com.proyecto.flowmanagement.backend.persistence.repository.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RolService {

    @Autowired
    private RolRepository rolRepository;

    public List<Rol> findAll() {
        return rolRepository.findAll();
    }

    public Map<String, Integer> getStats() {
        HashMap<String, Integer> stats = new HashMap<>();
        findAll().forEach(rol ->
                stats.put(rol.getName(), 1));
        return stats;
    }
}
