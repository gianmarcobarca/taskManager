package com.barca.taskmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.barca.taskmanager.configs.properties.RsaKeysProperties;

@SpringBootApplication
@EnableConfigurationProperties(RsaKeysProperties.class)
public class TaskmanagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaskmanagerApplication.class, args);
	}

	// TODO think about using Log4j2
}