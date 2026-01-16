package com.ringkhang.freewill.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateDetails {

    @Size(min = 4,max = 50)
    @NotBlank
    private String username;

    @Size(max = 500)
    @NotBlank
    private String bio;
}
