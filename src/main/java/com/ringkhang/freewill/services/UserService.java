package com.ringkhang.freewill.services;

import com.ringkhang.freewill.DTO.DeleteAccountRequestDTO;
import com.ringkhang.freewill.DTO.UserRegistrationDTO;
import com.ringkhang.freewill.DTO.UserResponseDTO;
import com.ringkhang.freewill.DTO.UserUpdateDetails;
import com.ringkhang.freewill.models.User;
import com.ringkhang.freewill.repo.UserDetailsRepo;
import com.ringkhang.freewill.util.CommonUtilMethods;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.ringkhang.freewill.util.CommonUtilMethods.convertLocalDateTimeToTimestamp;

@Service
public class UserService {

    private final UserDetailsRepo userDetailsRepo;
    private final BCryptPasswordEncoder encoder;
    private final UserServiceHelper userServiceHelper;

    public UserService(UserDetailsRepo userDetailsRepo, BCryptPasswordEncoder encoder, UserServiceHelper helper) {
        this.userDetailsRepo = userDetailsRepo;
        this.encoder = encoder;
        this.userServiceHelper = helper;
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

    // to get user details excluding crucial data like meta-data and passwords
    public ResponseEntity<UserResponseDTO> getEssentialUserDetails (){

        try{
            User u = userServiceHelper.getCurrentUserDetails();
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
                userServiceHelper.getCurrentUserId()
        );

        User u = userServiceHelper.getCurrentUserDetails();

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
                newUsername, userServiceHelper.getCurrentUserId()
        );

        User u = userServiceHelper.getCurrentUserDetails();

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
                newBio, userServiceHelper.getCurrentUserId()
        );

        User u = userServiceHelper.getCurrentUserDetails();

        return ResponseEntity.status(HttpStatus.OK).body(
                new UserResponseDTO(
                        u.getUserId(),
                        u.getUsername(),
                        u.getBio(),
                        CommonUtilMethods.convertLocalDateTimeToTimestamp(u.getCreatedDate())
                )
        );
    }

    // Delete user fully -- can't recover later
    @Transactional
    public ResponseEntity<String> deleteUserFully(Long uId,String password) {
        userServiceHelper.validateDeleteEditRequest(uId,password);

        int rowEffected =userDetailsRepo.deleteUserFully(uId);
        if (rowEffected>=1){
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body("User deleted successfully");
        }

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body("User already deleted");
    }

    // to delete the user partially -- can be re cover later
    @Transactional
    public ResponseEntity<String> deleteUserPartially(@Valid DeleteAccountRequestDTO deleteAccReqDTO) {

        User user = userServiceHelper.validateDeleteEditRequest(
                deleteAccReqDTO.getUId(),
                deleteAccReqDTO.getPassword());

        if (!user.getIsActive()){
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("The account/user is already disabled.");
        }

        int rowEffected =userDetailsRepo.deleteUserPartially(deleteAccReqDTO.getUId());

        if (rowEffected>=1){
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .build();
        }

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body("The account/user already disabled");
    }

}