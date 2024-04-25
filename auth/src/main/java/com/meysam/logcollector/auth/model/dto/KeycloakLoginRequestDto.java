package com.meysam.logcollector.auth.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Setter
@Builder@Getter
@AllArgsConstructor
@NoArgsConstructor
public class KeycloakLoginRequestDto {

    @NotNull
    private String username;
    @NotNull
    private String password;
    @NotNull
    private String clientId;
    @NotNull
    private String clientSecret;
    @NotNull
    private String grantType;
}
