package com.ringkhang.freewill.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter @Setter
public class UserRegistrationDTO {

    @Size(min = 4,max = 50)
    @NotBlank
    private String username;
    @Size(min = 4,max = 100)
    @NotBlank
    private String password;
    @Size(min = 0,max = 500)
    private String bio;

    private Map<String,Object> metadata;

    public UserRegistrationDTO(String username, String password, String bio, Map<String, Object> metadata) {
        this.username = username;
        this.password = password;
        this.bio = bio;
        this.metadata = metadata;
    }
}
