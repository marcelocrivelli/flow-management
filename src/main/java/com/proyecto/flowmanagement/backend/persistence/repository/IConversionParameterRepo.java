package com.proyecto.flowmanagement.backend.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyecto.flowmanagement.backend.persistence.entity.ConversionParameter;


public interface IConversionParameterRepo extends JpaRepository<ConversionParameter,Integer> {

}
