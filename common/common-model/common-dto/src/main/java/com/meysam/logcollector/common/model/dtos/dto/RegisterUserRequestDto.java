package com.meysam.logcollector.common.model.dtos.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.io.Serializable;

@Setter
@Builder@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterUserRequestDto implements Serializable {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private boolean enabled;


}
