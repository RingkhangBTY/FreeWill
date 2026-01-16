package com.ringkhang.freewill.controller;

import com.ringkhang.freewill.DTO.UserRegistrationDTO;
import com.ringkhang.freewill.DTO.UserResponseDTO;
import com.ringkhang.freewill.DTO.UserUpdateDetails;
import com.ringkhang.freewill.models.Posts;
import com.ringkhang.freewill.models.User;
import com.ringkhang.freewill.services.PostService;
import com.ringkhang.freewill.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("user")
public class UserController {

    private final UserService userService;
    private final PostService postService;

    public UserController(UserService userService , PostService postService) {
        this.userService = userService;
        this.postService = postService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> userRegister (@Valid @RequestBody UserRegistrationDTO userDetails){
        return userService.registerUser(userDetails);
    }

    @GetMapping("/details")
    public ResponseEntity<UserResponseDTO> getUserDetails(){
        return userService.getEssentialUserDetails();
    }

    @GetMapping("/posts")
    public List<Posts> userPosts(){
        return postService.getCurrentUserPosts();
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

    // Update the username of current user
    @PutMapping("/bio")
    public ResponseEntity<UserResponseDTO> updateBio(@RequestParam String bio){
        return userService.updateBio(bio);
    }

    @GetMapping("search-user")
    public ResponseEntity<List<UserResponseDTO>> searchUsersByUsername(@RequestParam String username){
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
}