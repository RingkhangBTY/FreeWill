package com.ringkhang.freewill.DTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateDetails {

    @Size(min = 4,max = 50, message = "Username must be between 4 and 50 characters")
    @NotBlank(message = "Username can't be blank")
    private String username;

    @Size(max = 1000,message = "Bio needs to be between 1000 characters")
    @NotBlank(message = "Bio can't be blank")
    private String bio;
}
