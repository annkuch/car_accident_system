package com.kpi.accidents.repository;

import org.springframework.data.repository.CrudRepository;
import com.kpi.accidents.model.Authority;



public interface AuthorityRepository extends CrudRepository<Authority, Integer> {
    Authority findByAuthority(String authority);
}
