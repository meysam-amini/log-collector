package com.meysam.logcollector.common.model.dtos.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Setter
@Builder@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestDto {

    @NotBlank
    private String username;
    @NotBlank
    private String password;

}
