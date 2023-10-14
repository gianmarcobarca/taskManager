package com.barca.taskmanager.repositories;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.barca.taskmanager.models.User;

public interface UserRepository extends MongoRepository<User, String> {

  Optional<User> findByEmail(String email);
}
