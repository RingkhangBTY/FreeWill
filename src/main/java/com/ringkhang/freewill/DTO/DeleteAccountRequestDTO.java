package com.ringkhang.freewill.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class DeleteAccountRequestDTO {
    @NotNull(message = "User id can't be null or empty")
    private Long uId;
    @NotBlank(message ="Password can't be null or empty")
    private String password;
}
