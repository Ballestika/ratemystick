package com.ratemystick.ratemystick.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Configuration
@EntityScan("com.ratemystick.ratemystick.domain")
@EnableJpaRepositories("com.ratemystick.ratemystick.repos")
@EnableTransactionManagement
public class DomainConfig {
}
