package com.ringkhang.freewill.services;

import com.ringkhang.freewill.DTO.UserRegistrationDTO;
import com.ringkhang.freewill.DTO.UserResponseDTO;
import com.ringkhang.freewill.DTO.UserUpdateDetails;
import com.ringkhang.freewill.models.MyUserPrincipal;
import com.ringkhang.freewill.models.User;
import com.ringkhang.freewill.repo.UserDetailsRepo;
import com.ringkhang.freewill.util.CommonUtilMethods;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.ringkhang.freewill.util.CommonUtilMethods.convertLocalDateTimeToTimestamp;

@Service
public class UserService {

    private final UserDetailsRepo userDetailsRepo;
    private final BCryptPasswordEncoder encoder;

    public UserService(UserDetailsRepo userDetailsRepo, BCryptPasswordEncoder encoder) {
        this.userDetailsRepo = userDetailsRepo;
        this.encoder = encoder;
    }

    // To register user for the first time
    @Transactional
    public ResponseEntity<UserResponseDTO> registerUser( UserRegistrationDTO userDetails) {

        if (userDetailsRepo.existsByUsername(userDetails.getUsername())){
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        userDetails.setPassword(encoder.encode(userDetails.getPassword()));

        User user = new User();

        user.setUsername(userDetails.getUsername());
        user.setPassword(userDetails.getPassword());
        user.setBio(userDetails.getBio());
        user.setMetadata(userDetails.getMetadata());

        User savedUser = userDetailsRepo.save(user);

        UserResponseDTO responseDTO = new UserResponseDTO(
                savedUser.getUserId(),
                savedUser.getUsername(),
                savedUser.getBio(),
                CommonUtilMethods.convertLocalDateTimeToTimestamp(savedUser.getCreatedDate())
        );

        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }

    //Search for user in database for matching names
    public List<UserResponseDTO> getUsersByUsername(String username){
        return userDetailsRepo.getUsersByUsername(username);
    }

    //Returns current user id whose log-in
    public Long getCurrentUserId(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MyUserPrincipal principal = (MyUserPrincipal) authentication.getPrincipal();
        return principal.getUser().getUserId();
    }

    // Get full user details including - id , pass and meta date.
    public User getCurrentUserDetails() {
        return userDetailsRepo.findById(getCurrentUserId()).orElse(new User());
    }

    // Get full user details including - id , pass and meta date.
    public User getCurrentUserByUserName(String username) {
        return userDetailsRepo.findByUsername(username).orElse(new User());
    }

    // to get user details excluding crucial data like meta-data and passwords
    public ResponseEntity<UserResponseDTO> getEssentialUserDetails (){

        try{
            User u = getCurrentUserDetails();
            if (u.getUserId()==null){
                return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
            }else {
                UserResponseDTO userData = new UserResponseDTO(
                        u.getUserId(),
                        u.getUsername(),
                        u.getBio(),
                        convertLocalDateTimeToTimestamp(u.getCreatedDate())
                );
                return new ResponseEntity<>(userData,HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Updates the full user details of current user (Username & bio)
    @Transactional
    public ResponseEntity<UserResponseDTO> updateUserDetails(@Valid UserUpdateDetails newUserDetails) {

        userDetailsRepo.updateUserNameBio(
                newUserDetails.getUsername(),
                newUserDetails.getBio(),
                getCurrentUserId()
        );


        User u = getCurrentUserDetails();

        return ResponseEntity.status(HttpStatus.OK).body(
                new UserResponseDTO(
                        u.getUserId(),
                        u.getUsername(),
                        u.getBio(),
                        CommonUtilMethods.convertLocalDateTimeToTimestamp(u.getCreatedDate())
                )
        );
    }

    //Updates the username of current user
    @Transactional
    public ResponseEntity<UserResponseDTO> updateUsername(String newUsername) {
        userDetailsRepo.updateUsername(
                newUsername,getCurrentUserId()
        );

        User u = getCurrentUserDetails();

        return ResponseEntity.status(HttpStatus.OK).body(
                new UserResponseDTO(
                        u.getUserId(),
                        u.getUsername(),
                        u.getBio(),
                        CommonUtilMethods.convertLocalDateTimeToTimestamp(u.getCreatedDate())
                )
        );
    }

    //Updates the bio
    @Transactional
    public ResponseEntity<UserResponseDTO> updateBio(String newBio){

        userDetailsRepo.updateBio(
                newBio,getCurrentUserId()
        );

        User u = getCurrentUserDetails();

        return ResponseEntity.status(HttpStatus.OK).body(
                new UserResponseDTO(
                        u.getUserId(),
                        u.getUsername(),
                        u.getBio(),
                        CommonUtilMethods.convertLocalDateTimeToTimestamp(u.getCreatedDate())
                )
        );
    }
}