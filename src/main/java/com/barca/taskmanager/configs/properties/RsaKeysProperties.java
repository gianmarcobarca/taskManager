package com.barca.taskmanager.configs.properties;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "rsa", ignoreInvalidFields = true)
public record RsaKeysProperties(RSAPublicKey publicKey, RSAPrivateKey privateKey) {
}
