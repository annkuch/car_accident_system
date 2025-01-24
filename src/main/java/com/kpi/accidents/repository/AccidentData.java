package com.kpi.accidents.repository;

import com.kpi.accidents.model.Accident;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface AccidentData extends CrudRepository<Accident, Integer> {


    List<Accident> findAll();


}
