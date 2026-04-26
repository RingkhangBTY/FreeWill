package com.ringkhang.freewill.controller;

import com.ringkhang.freewill.DTO.DeleteAccountRequestDTO;
import com.ringkhang.freewill.DTO.UserRegistrationDTO;
import com.ringkhang.freewill.DTO.UserResponseDTO;
import com.ringkhang.freewill.DTO.UserUpdateDetails;
import com.ringkhang.freewill.helperClasses.AnyResponse;
import com.ringkhang.freewill.models.Posts;
import com.ringkhang.freewill.models.User;
import com.ringkhang.freewill.services.PostService;
import com.ringkhang.freewill.services.UserService;
import com.ringkhang.freewill.services.UserServiceHelper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    private final UserService userService;
    private final PostService postService;
    private final UserServiceHelper userServiceHelper;

    public UserController(UserService userService ,
                          PostService postService,
                          UserServiceHelper userServiceHelper) {
        this.userService = userService;
        this.postService = postService;
        this.userServiceHelper = userServiceHelper;
    }

    // follow another user
    @PostMapping("/follow/{userID}")
    public ResponseEntity<?> follow(
            @PathVariable @NotNull(message = "User ID is not provided") Long userID){
        userService.followUser(userID);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Followed successfully");
    }

    // Register new user no auth needed
    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> userRegister (
            @RequestBody @Valid UserRegistrationDTO userDetails) {

        UserResponseDTO responseDTO = userService.registerUser(userDetails);

        log.info("User created successfully");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(responseDTO);
    }

    // Retrieve current users details
    @GetMapping("/details")
    public ResponseEntity<UserResponseDTO> getUserDetails(){
        log.info("User details requested");
        UserResponseDTO responseDTO = userService.getEssentialUserDetails();

        return ResponseEntity.status(HttpStatus.OK)
                .body(responseDTO);
    }

    // Updates current user details including both username & bio
    @PutMapping("/details")
    public ResponseEntity<UserResponseDTO> updateUserDetails(
            @RequestBody @Valid UserUpdateDetails newUserDetails){

        UserResponseDTO responseDTO = userService.updateUserDetails(newUserDetails);

        log.info("Requested for user details update");
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(responseDTO);
    }

    // Update the username of current user
    @PutMapping("/username")
    public ResponseEntity<UserResponseDTO> updateUsername(
            @RequestParam @NotBlank(message = "New username can't be blank.") String username){

        UserResponseDTO userResponseDTO = userService.updateUsername(username);
        log.info("Username updated successfully");
        return ResponseEntity.status(HttpStatus.OK)
                .body(userResponseDTO);
    }

    // Update the bio of current user
    @PutMapping("/bio")
    public ResponseEntity<AnyResponse<UserResponseDTO>> updateBio(
            @RequestParam @NotBlank(message = "Bio can't ne blank") String bio){
        UserResponseDTO responseDTO = userService.updateBio(bio);
        log.info("Bio updated successfully");

        return ResponseEntity.status(HttpStatus.OK)
                .body(new AnyResponse<>("Bio updated successfully",responseDTO));
    }

    // Search user's by username
    @GetMapping("search-user/{username}")
    public ResponseEntity<List<UserResponseDTO>> searchUsersByUsername(
            @PathVariable @RequestParam @NotBlank(message = "Username can't be blank") String username){

        List<UserResponseDTO> users = userService.getUsersByUsername(username);

        return ResponseEntity.status(HttpStatus.OK)
                .body(users);
    }

    // to fully delete the account
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUserFully(
            @RequestBody @Valid DeleteAccountRequestDTO deleteAccReqDTO){

        userService.deleteUserFully(deleteAccReqDTO);

        return ResponseEntity.status(HttpStatus.OK)
                .body("User deleted successfully");
    }

    // to disable the account by partially deleting user
    @PutMapping("/disable")
    public ResponseEntity<String> deleteUserPartially(
            @RequestBody @Valid DeleteAccountRequestDTO deleteAccReqDTO){

        userService.deleteUserPartially(deleteAccReqDTO);

        return ResponseEntity.status(HttpStatus.OK)
                .body("User disabled successfully ");
    }
}