package com.kpi.accidents.repository;

import org.springframework.data.repository.CrudRepository;
import com.kpi.accidents.model.User;

public interface UserRepository extends CrudRepository<User, Integer> {
}
