package com.barca.taskmanager.repositorites;

import static org.junit.Assert.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import com.barca.taskmanager.configs.MongoConfig;
import com.barca.taskmanager.models.User;

@DataMongoTest()
@Import({ MongoConfig.class })
@Transactional
class UserRepositorySliceTest {
  @Autowired
  private UserRepository userRepository;

  @BeforeEach
  void setup() {
    userRepository.deleteAll();
  }

  @Test
  void findByEmail_should_find_document() {

    User user = User.builder()
        .firstName("john")
        .lastName("doe")
        .email("johndoe@example.com")
        .password("encryptedpassword")
        .build();

    userRepository.save(user);
    Optional<User> result = userRepository.findByEmail("johndoe@example.com");

    assertEquals(true, result.isPresent());
  }

  @Test
  void findByEmail_should_not_find_document() {

    Optional<User> result = userRepository.findByEmail("johndoe@example.com");

    assertEquals(true, result.isEmpty());
  }
}
