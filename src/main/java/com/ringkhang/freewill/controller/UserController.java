package com.ringkhang.freewill.controller;

import com.ringkhang.freewill.DTO.DeleteAccountRequestDTO;
import com.ringkhang.freewill.DTO.UserRegistrationDTO;
import com.ringkhang.freewill.DTO.UserResponseDTO;
import com.ringkhang.freewill.DTO.UserUpdateDetails;
import com.ringkhang.freewill.models.Posts;
import com.ringkhang.freewill.services.PostService;
import com.ringkhang.freewill.services.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    private final UserService userService;
    private final PostService postService;

    public UserController(UserService userService , PostService postService) {
        this.userService = userService;
        this.postService = postService;
    }

    // follow another user
    @PostMapping("/follow/{userID}")
    public ResponseEntity<?> follow(@PathVariable Long userID){
        return userService.followUser(userID);
    }

    // Register new user no auth needed
    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> userRegister (@Valid @RequestBody UserRegistrationDTO userDetails){
        log.info("User created successfully");
        return userService.registerUser(userDetails);
    }

    // Retrieve current users details
    @GetMapping("/details")
    public ResponseEntity<UserResponseDTO> getUserDetails(){
        log.info("User details requested");
        return userService.getEssentialUserDetails();
    }

    // Updates current user details including both username & bio
    @PutMapping("/details")
    public ResponseEntity<UserResponseDTO> updateUserDetails(@Valid @RequestBody UserUpdateDetails newUserDetails){
        return userService.updateUserDetails(newUserDetails);
    }

    // Update the username of current user
    @PutMapping("/username")
    public ResponseEntity<UserResponseDTO> updateUsername(@RequestParam String username){
        return userService.updateUsername(username);
    }

    // Update the bio of current user
    @PutMapping("/bio")
    public ResponseEntity<UserResponseDTO> updateBio(@RequestParam String bio){
        return userService.updateBio(bio);
    }

    // Search user's by username
    @GetMapping("search-user/{username}")
    public ResponseEntity<List<UserResponseDTO>> searchUsersByUsername(@PathVariable @RequestParam String username){
        try{
            List<UserResponseDTO> users = userService.getUsersByUsername(username);
            if (!users.isEmpty()){
                return new ResponseEntity<>(users,HttpStatus.OK);
            }
            System.out.println(users);
            return new ResponseEntity<>(users,HttpStatus.OK);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // to fully delete the account
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUserFully(@Valid @RequestBody DeleteAccountRequestDTO deleteAccReqDTO){
        return userService.deleteUserFully(deleteAccReqDTO.getUId(),deleteAccReqDTO.getPassword());
    }

    // to disable the account by partially deleting user
    @PutMapping("/disable")
    public ResponseEntity<String> deleteUserPartially(@Valid @RequestBody DeleteAccountRequestDTO deleteAccReqDTO){
        return userService.deleteUserPartially(deleteAccReqDTO);
    }
}