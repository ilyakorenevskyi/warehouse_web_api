package com.ilyakor.warehouse_web_api.repositories;

import com.ilyakor.warehouse_web_api.entities.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User,Integer> {
    public Optional<User> findByLogin(String login);
}
