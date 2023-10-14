package com.barca.taskmanager;

import java.util.Base64;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.barca.taskmanager.dtos.JwtDto;
import com.barca.taskmanager.dtos.TaskCreationDto;
import com.barca.taskmanager.dtos.UserCreationDto;
import com.barca.taskmanager.repositories.TaskRepository;
import com.barca.taskmanager.repositories.UserRepository;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(OrderAnnotation.class)
@ActiveProfiles("test")
class TaskmanagerApplicationTests {

	@Autowired
	private WebTestClient client;

	@Autowired
	private JwtDecoder decoder;

	@Autowired
	private TaskRepository taskRepository;

	@Autowired
	private UserRepository userRepository;

	@BeforeAll
	void setup() {
		taskRepository.deleteAll();
		userRepository.deleteAll();
	}

	@AfterAll
	void destroy() {
		taskRepository.deleteAll();
		userRepository.deleteAll();
	}

	@Test
	@Order(1)
	void signup_and_getToken_should_return_201_and_200() {

		var dto = new UserCreationDto("john", "doe", "johndoe@example.com", "password");
		String auth = Base64.getEncoder().encodeToString("johndoe@example.com:password".getBytes());

		client
				.post().uri("/auth/signup")
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(dto)
				.exchange()
				.expectStatus().isCreated();

		var result = client
				.get().uri("/auth/token")
				.header("Authorization", "Basic " + auth)
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk()
				.expectBody(JwtDto.class)
				.returnResult();

		Jwt token = decoder.decode(result.getResponseBody().tokenValue());
		assertThat(token.getClaim("email").toString()).hasToString("johndoe@example.com");
	}

	@Test
	@Order(2)
	void createTask_and_getTasks_should_return_201_and_200() {

		var taskCreationDto1 = new TaskCreationDto("Task example from john doe 1");
		var taskCreationDto2 = new TaskCreationDto("Task example from john doe 2");
		String auth = Base64.getEncoder().encodeToString("johndoe@example.com:password".getBytes());

		JwtDto jwtDto = client
				.get().uri("/auth/token")
				.header("Authorization", "Basic " + auth)
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk()
				.expectBody(JwtDto.class)
				.returnResult()
				.getResponseBody();

		client.post().uri("/api/tasks")
				.header("Authorization", "Bearer " + jwtDto.tokenValue())
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(taskCreationDto1)
				.exchange()
				.expectStatus().isCreated();

		client.post().uri("/api/tasks")
				.header("Authorization", "Bearer " + jwtDto.tokenValue())
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(taskCreationDto2)
				.exchange()
				.expectStatus().isCreated();

		client
				.get().uri("/api/tasks")
				.header("Authorization", "Bearer " + jwtDto.tokenValue())
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("$.numberOfElements").isEqualTo(2)
				.jsonPath("$.content[0].content").isEqualTo("Task example from john doe 2")
				.jsonPath("$.content[1].content").isEqualTo("Task example from john doe 1");
	}

	@Test
	@Order(3)
	void deleteTasks_and_getTasks_should_return_204_and_200() {
		String auth = Base64.getEncoder().encodeToString("johndoe@example.com:password".getBytes());

		JwtDto jwtDto = client
				.get().uri("/auth/token")
				.header("Authorization", "Basic " + auth)
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk()
				.expectBody(JwtDto.class)
				.returnResult()
				.getResponseBody();

		client.delete().uri("/api/tasks")
				.header("Authorization", "Bearer " + jwtDto.tokenValue())
				.exchange()
				.expectStatus().isNoContent();

		client
				.get().uri("/api/tasks")
				.header("Authorization", "Bearer " + jwtDto.tokenValue())
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("$.numberOfElements").isEqualTo(0);
	}

	@Test
	@Order(4)
	void createTask_and_deleteUser_should_return_201_and_204() {
		String auth = Base64.getEncoder().encodeToString("johndoe@example.com:password".getBytes());
		var taskCreationDto1 = new TaskCreationDto("Task example from john doe 1");

		JwtDto jwtDto = client
				.get().uri("/auth/token")
				.header("Authorization", "Basic " + auth)
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk()
				.expectBody(JwtDto.class)
				.returnResult()
				.getResponseBody();

		client.post().uri("/api/tasks")
				.header("Authorization", "Bearer " + jwtDto.tokenValue())
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(taskCreationDto1)
				.exchange()
				.expectStatus().isCreated();

		client.delete().uri("/auth/deregister")
				.header("Authorization", "Basic " + auth)
				.exchange()
				.expectStatus().isNoContent();

		client
				.get().uri("/api/tasks")
				.header("Authorization", "Bearer " + jwtDto.tokenValue())
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("$.numberOfElements").isEqualTo(0);

		client
				.get().uri("/auth/token")
				.header("Authorization", "Basic " + auth)
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isUnauthorized();
	}

}
