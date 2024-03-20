package com.bcd.big_cheese_delivery.web.service;


import com.bcd.big_cheese_delivery.domain.User;
import com.bcd.big_cheese_delivery.exception.UserAlreadyExistsException;
import com.bcd.big_cheese_delivery.repository.UserRepository;
import com.bcd.big_cheese_delivery.service.UserService;
import com.bcd.big_cheese_delivery.testcontainers.TestContainersConfig;
import com.bcd.big_cheese_delivery.utils.MongoRepositoryUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ContextConfiguration(classes = TestContainersConfig.class)
public class UserServiceTest {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private UserService userService;

  @Autowired
  private PasswordEncoder passwordEncoder;

  public static List<User> generateUsers(int count) {
    List<User> users = new ArrayList<>();
    for (int i = 1; i <= count; i++) {
      User user = new User();
      user.setId(Integer.toString(i));
      user.setUsername("User" + i);
      user.setFirstName("John");
      user.setLastName("Doe");
      user.setEmail("user" + i + "@example.com");
      user.setPassword("password" + i);
      users.add(user);
    }
    return users;
  }

  @BeforeEach
  void addUsers() {
    userRepository.saveAll(generateUsers(5));
  }

  @AfterEach
  void cleanAll() {
    userRepository.deleteAll();
  }

  @Test
  @DisplayName("Should update user with non taken username")
  void testUpdate() {
    var username = "NewUsername";
    var id = "1";
    var user = MongoRepositoryUtils.getById(id, userRepository);
    user.setUsername(username);

    userService.update(user);

    var updatedUser = MongoRepositoryUtils.getById(id, userRepository);

    assertEquals(username, updatedUser.getUsername());
  }

  @Test
  @DisplayName("Should not update user with taken username")
  void testUpdateWithTakenUsername() {
    var username = "User2";
    var id = "1";
    var user = MongoRepositoryUtils.getById(id, userRepository);

    user.setUsername(username);

    assertThatThrownBy(() ->
        userService.update(user)).isInstanceOf(UserAlreadyExistsException.class)
        .message()
        .contains(username);
  }

  @Test
  @DisplayName("Should create new user")
  void testCreateNewUser() {
    var password = "newUserPassword";
    var id = UUID.randomUUID().toString();
    User user = new User();
    user.setId(id);
    user.setUsername("newUser");
    user.setFirstName("newUserFirstName");
    user.setLastName("newUserLastName");
    user.setEmail("newUser@example.com");
    user.setPassword(password);
    userService.create(user);

    var createdUser = MongoRepositoryUtils.getById(id, userRepository);
    assertEquals(user.getId(), createdUser.getId());
    assertTrue(passwordEncoder.matches(password, createdUser.getPassword()));
  }

  @Test
  @DisplayName("Should not create new user")
  void testCreateNewUserWithTakenCredentials() {
    var password = "userPassword";
    var username = "User1";
    var user = new User();
    user.setUsername(username);
    user.setFirstName("newUserFirstName");
    user.setLastName("newUserLastName");
    user.setEmail("newUser@example.com");
    user.setPassword(password);
    assertThatThrownBy(() ->
        userService.create(user)).isInstanceOf(UserAlreadyExistsException.class)
        .message()
        .contains(username);

  }
}
