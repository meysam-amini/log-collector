package com.meysam.logcollector.auth.controller.integrationtest;

import com.meysam.logcollector.common.model.dtos.dto.LoginRequestDto;
import com.meysam.logcollector.common.model.dtos.dto.LoginResponseDto;
import dasniko.testcontainers.keycloak.KeycloakContainer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
        properties = "spring.profiles.active=test")
//beforeAll() must be static unless the test class is annotated with:
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserAuthControllerIT {
    private WebClient webClient;
    private int port=8091;

    //postgres
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:14.2")
            .withExposedPorts(5432)
            .withUsername("admin")
            .withPassword("password");
    //redis
    static GenericContainer<?> redis =
            new GenericContainer<>(DockerImageName.parse("redis:6")).withExposedPorts(6379);

    static final KeycloakContainer keycloak=new KeycloakContainer().withRealmImportFile("keycloak/keycloak-realm-export.json");

    @BeforeAll
    void beforeAll(){
        redis.start();
        System.setProperty("spring.data.redis.host", redis.getHost());
        System.setProperty("spring.data.redis.port", redis.getMappedPort(6379).toString());
        System.setProperty("spring.data.redis.connect-timeout", "10000");
        System.setProperty("spring.data.redis.timeout", "1000");
        System.setProperty("spring.data.redis.jedis.pool.min-idle", "20");
        System.setProperty("spring.data.redis.jedis.pool.max-idle", "100");

        postgres.start();
        System.setProperty("spring.datasource.driverClassName", "org.postgresql.Driver");
        System.setProperty("spring.datasource.url",postgres.getJdbcUrl()); //"jdbc:postgresql://"+postgres.getHost()+":"+postgres.getMappedPort(5432).toString()+"/postgres");
        System.setProperty("spring.datasource.username", postgres.getUsername());
        System.setProperty("spring.datasource.password", postgres.getPassword());
        System.setProperty("spring.jpa.hibernate.ddl-auto", "update");

        keycloak.start();

        webClient = WebClient.create();

    }

    @AfterAll
    void afterAll(){
        redis.stop();
        postgres.stop();
        keycloak.stop();
    }


    private static final String BASE_URL = "http://localhost:";
    private static final String USERNAME = "testuser";
    private static final String PASSWORD = "password123";
    private static final String EMAIL = "test@example.com";

    @Test
    public void testLoginSuccess() throws Exception {
        LoginRequestDto loginDto = new LoginRequestDto(USERNAME, PASSWORD);

        Mono<LoginResponseDto> responseMono = webClient.post()
                .uri(BASE_URL + port + "/auth-user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(loginDto)
                .retrieve()
                .bodyToMono(LoginResponseDto.class);

        LoginResponseDto response = responseMono.block(); // Block for testing purposes

        assertNotNull(response); // Assert successful response
    }
}