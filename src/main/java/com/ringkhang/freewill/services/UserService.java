package com.ringkhang.freewill.services;

import com.ringkhang.freewill.DTO.DeleteAccountRequestDTO;
import com.ringkhang.freewill.DTO.UserRegistrationDTO;
import com.ringkhang.freewill.DTO.UserResponseDTO;
import com.ringkhang.freewill.DTO.UserUpdateDetails;
import com.ringkhang.freewill.exception.CreateNewResourceException;
import com.ringkhang.freewill.exception.UpdateEditResourceException;
import com.ringkhang.freewill.exception.NoUserFound;
import com.ringkhang.freewill.models.Followers;
import com.ringkhang.freewill.models.User;
import com.ringkhang.freewill.repo.FollowRepo;
import com.ringkhang.freewill.repo.UserDetailsRepo;
import com.ringkhang.freewill.util.CommonUtilMethods;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.ringkhang.freewill.util.CommonUtilMethods.convertLocalDateTimeToTimestamp;

@Service
public class UserService {

    private final UserDetailsRepo userDetailsRepo;
    private final BCryptPasswordEncoder encoder;
    private final UserServiceHelper userServiceHelper;
    private final FollowRepo followRepo;

    public UserService(UserDetailsRepo userDetailsRepo, BCryptPasswordEncoder encoder, UserServiceHelper helper, FollowRepo followRepo) {
        this.userDetailsRepo = userDetailsRepo;
        this.encoder = encoder;
        this.userServiceHelper = helper;
        this.followRepo = followRepo;
    }

    // To register user for the first time
    @Transactional
    public UserResponseDTO registerUser( UserRegistrationDTO userDetails) {

        if (userDetailsRepo.existsByUsername(userDetails.getUsername())){
            throw new CreateNewResourceException("User already exist");
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

        if (responseDTO.getUsername().isBlank()){
            throw new CreateNewResourceException("Fails to create new user due to unexpected reasons.");
        }

        return responseDTO;
    }

    //Search for user in database for matching names
    public List<UserResponseDTO> getUsersByUsername(String username){
        List<UserResponseDTO> userResponseDTOList = userDetailsRepo.getUsersByUsername(username);

        if (userResponseDTOList.isEmpty()){
            throw new NoUserFound("No user found with username : "+username);
        }

        return userResponseDTOList;
    }

    // to get user details excluding crucial data like meta-data and passwords
    public UserResponseDTO getEssentialUserDetails (){

        User u = userServiceHelper.getCurrentUserDetails();

        return  new UserResponseDTO(
                u.getUserId(),
                u.getUsername(),
                u.getBio(),
                convertLocalDateTimeToTimestamp(u.getCreatedDate())
        );
    }

    // Updates the full user details of current user (Username & bio)
    @Transactional
    public UserResponseDTO updateUserDetails(UserUpdateDetails newUserDetails) {

        int rowEffected = userDetailsRepo.updateUserNameBio(
                newUserDetails.getUsername(),
                newUserDetails.getBio(),
                userServiceHelper.getCurrentUserId()
        );

        if (rowEffected == 0 ){
            throw new UpdateEditResourceException("Fails to update new user details ");
        }

        User u = userServiceHelper.getCurrentUserDetails();

        return new UserResponseDTO(
                u.getUserId(),
                u.getUsername(),
                u.getBio(),
                CommonUtilMethods.convertLocalDateTimeToTimestamp(u.getCreatedDate())
        );
    }

    //Updates the username of current user
    @Transactional
    public UserResponseDTO updateUsername(String newUsername) {

        int rowEffected = userDetailsRepo.updateUsername(
                newUsername, userServiceHelper.getCurrentUserId()
        );

        if (rowEffected == 0 ){
            throw new UpdateEditResourceException("Fails to update new username");
        }

        User u = userServiceHelper.getCurrentUserDetails();
        return new UserResponseDTO(
                u.getUserId(),
                u.getUsername(),
                u.getBio(),
                CommonUtilMethods.convertLocalDateTimeToTimestamp(u.getCreatedDate())
        );
    }

    //Updates the bio
    @Transactional
    public UserResponseDTO updateBio(String newBio){

        int rowEffected = userDetailsRepo.updateBio(
                newBio, userServiceHelper.getCurrentUserId()
        );

        if (rowEffected == 0 ){
            throw new UpdateEditResourceException("Fails to update new bio");
        }

        User u = userServiceHelper.getCurrentUserDetails();

        return new UserResponseDTO(
                u.getUserId(),
                u.getUsername(),
                u.getBio(),
                CommonUtilMethods.convertLocalDateTimeToTimestamp(u.getCreatedDate())
        );
    }

    // Delete user fully -- can't recover later
    @Transactional
    public void deleteUserFully(DeleteAccountRequestDTO deleteAccountRequestDTO) {

        userServiceHelper.validateDeleteEditRequest(
                deleteAccountRequestDTO.getUId(),
                deleteAccountRequestDTO.getPassword());

        int rowEffected = userDetailsRepo.deleteUserFully(deleteAccountRequestDTO.getUId());

        if (rowEffected==0){
            throw new RuntimeException("User already deleted or does not exist");
        }
    }

    // to delete the user partially -- can be re cover later
    @Transactional
    public void deleteUserPartially(@Valid DeleteAccountRequestDTO deleteAccReqDTO) {

        User user = userServiceHelper.validateDeleteEditRequest(
                deleteAccReqDTO.getUId(),
                deleteAccReqDTO.getPassword());

        if (!user.getIsActive()){
            throw new UpdateEditResourceException(
                    "The account/user is already disabled."
            );
        }

        int rowEffected = userDetailsRepo.deleteUserPartially(deleteAccReqDTO.getUId());

        if (rowEffected == 0){
            throw new UpdateEditResourceException(
                    "Fails to disable account due to unexpected reasons"
            );
        }
    }

    // Current use follow the other user
    public void followUser(Long userID) {

        User followUser = userDetailsRepo.findById(userID)
                .orElseThrow(()-> new NoUserFound("Unable to find the user."));

        if (followUser.getIsActive() == false ){
            throw new CreateNewResourceException(
                    "The requested follow user is not active"
            );
        }

        if (isAlreadyFollowed(userID)){
            throw new CreateNewResourceException("Current user already follows this user..");
        }

        Followers follow = new Followers();
        follow.setFollower(userServiceHelper.getCurrentUserDetails());
        follow.setFollows(followUser);

        Followers followers = followRepo.save(follow);

        if (followers.getFollower() == null || followers.getFollows() == null){
            throw new CreateNewResourceException(
                    "Failed to follow user"
            );
        }

    }

    private boolean isAlreadyFollowed(Long userId){
        Followers followers = followRepo.findByUserId(userServiceHelper.getCurrentUserId(),userId);
        return followers != null;
    }
}