package integrationtest;

import com.meysam.logcollector.common.model.dto.LoginRequestDto;
import com.meysam.logcollector.common.model.dto.LoginResponseDto;
import com.meysam.logcollector.common.model.dto.RegisterUserRequestDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = {com.meysam.logcollector.auth.controller.UserAuthController.class,
                com.meysam.logcollector.auth.service.impl.KeycloakServiceImpl.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserAuthControllerIT {

    private WebClient webClient;
    private int port=8091;

    @BeforeAll
    public  void setUp() {
//        port = context.getEnvironment().getProperty("local.server.port", Integer.class);
        webClient = WebClient.create();
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

    @Test
    public void testLoginFail_InvalidCredentials() throws Exception {
        LoginRequestDto loginDto = new LoginRequestDto(USERNAME, "wrongPassword");

        Mono<ClientResponse> responseMono = webClient.post()
                .uri(BASE_URL + port + "/auth-user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(loginDto)
                .exchange();

        ClientResponse response = responseMono.block(); // Block for testing purposes

        assertEquals(HttpStatus.UNAUTHORIZED, response.statusCode()); // Assert unauthorized status code
    }

    @Test
    public void testRegisterSuccess() throws Exception {
        RegisterUserRequestDto registerDto =
                RegisterUserRequestDto.builder()
                        .username(USERNAME)
                        .password(PASSWORD)
                        .email(EMAIL)
                        .build();

        Mono<ClientResponse> responseMono = webClient.post()
                .uri(BASE_URL + port + "/auth-user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(registerDto)
                .exchange();

        ClientResponse response = responseMono.block(); // Block for testing purposes

        assertEquals(HttpStatus.CREATED, response.statusCode()); // Assert created status code
    }

    @Test
    public void testRegisterFail_MissingUsername() throws Exception {
        RegisterUserRequestDto registerDto =
                RegisterUserRequestDto.builder()
                        .password(PASSWORD)
                        .email(EMAIL)
                        .build();

        Mono<ClientResponse> responseMono = webClient.post()
                .uri(BASE_URL + port + "/auth-user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(registerDto)
                .exchange();

        ClientResponse response = responseMono.block(); // Block for testing purposes

        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode()); // Assert bad request status code
    }
}
