package com.proyecto.flowmanagement.backend.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyecto.flowmanagement.backend.persistence.entity.Alternative;

public interface IAlternativeRepo extends JpaRepository<Alternative,Integer>{

}
