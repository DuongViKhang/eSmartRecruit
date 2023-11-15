package com.example.eSmartRecruit.services.impl;

import com.example.eSmartRecruit.authentication.request_reponse.RegisterRequest;
import com.example.eSmartRecruit.controllers.request_reponse.ResponseObject;
import com.example.eSmartRecruit.controllers.request_reponse.request.EditUserRequest;
import com.example.eSmartRecruit.controllers.request_reponse.request.UserRequest;
import com.example.eSmartRecruit.exception.UserException;
import com.example.eSmartRecruit.models.User;
import com.example.eSmartRecruit.models.enumModel.Role;
import com.example.eSmartRecruit.models.enumModel.UserStatus;
import com.example.eSmartRecruit.repositories.UserRepos;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    private UserService userService;
    private UserRepos userRepository;
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setup() {
        userRepository = mock(UserRepos.class);
        passwordEncoder = mock(PasswordEncoder.class);
        userService = new UserService(userRepository, passwordEncoder);
    }

    @Test
    void testGetAllUser() throws UserException {
        // Arrange
        List<User> expectedUsers = new ArrayList<>();
        when(userRepository.findAll()).thenReturn(expectedUsers);

        // Act
        List<User> result = userService.getAllUser();

        // Assert
        assertEquals(expectedUsers, result);
    }

    @Test
    void testFindByUsername_Successful() throws UserException {
        // Arrange
        String username = "john_doe";
        User expectedUser = new User();
        expectedUser.setUsername(username);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(expectedUser));

        // Act
        User result = userService.findByUsername(username);

        // Assert
        assertEquals(expectedUser, result);
    }

    @Test
    void testFindByUsername_UserNotFound() {
        // Arrange
        String username = "non_existent_user";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(UserException.class, () -> userService.findByUsername(username));
    }
    @Test
    void testGetUserRole() {
        // Arrange
        int userId = 1;
        User user = new User();
        user.setId(userId);
        user.setRoleName(Role.Candidate);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act
        String result = userService.getUserRole(userId);

        // Assert
        assertEquals("Candidate", result);
    }
    @Test
    void testUpdateUserPassword_Successful() throws UserException {
        // Arrange
        String username = "john_doe";
        String newPassword = "new_password";
        User expectedUser = new User();
        expectedUser.setUsername(username);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(expectedUser));
        when(passwordEncoder.encode(newPassword)).thenReturn("encoded_password");

        // Act
        String result = userService.updateUserpassword(username, newPassword);

        // Assert
        assertEquals("Successfully saved", result);
        verify(userRepository, times(1)).save(expectedUser);
    }
    @Test
    void testUpdateUserPassword_UserNotFound() {
        // Arrange
        String username = "non_existent_user";
        String newPassword = "new_password";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(UserException.class, () -> userService.updateUserpassword(username, newPassword));
        verify(userRepository, never()).save(any());
    }
    @Test
    void testUpdateUser_DuplicateEmail() {
        // Arrange
        int userId = 1;
        UserRequest userRequest = new UserRequest("duplicate_email@example.com", "new_phone_number");
        User existingUser = new User();
        existingUser.setUsername("john_doe");
        existingUser.setEmail("old_email@example.com");
        existingUser.setPhoneNumber("old_phone_number");
        existingUser.setId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.findByEmail(userRequest.getEmail())).thenReturn(Optional.of(new User()));

        // Act and Assert
        assertThrows(UserException.class, () -> userService.updateUser(userRequest, userId));
        verify(userRepository, never()).save(any());
    }
    @Test
    void testUpdateUser_DuplicatePhoneNumber() {
        // Arrange
        int userId = 1;
        UserRequest userRequest = new UserRequest("new_email@example.com", "duplicate_phone_number");
        User existingUser = new User();
        existingUser.setUsername("john_doe");
        existingUser.setEmail("old_email@example.com");
        existingUser.setPhoneNumber("old_phone_number");
        existingUser.setId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.findByPhoneNumber(userRequest.getPhoneNumber())).thenReturn(Optional.of(new User()));

        // Act and Assert
        assertThrows(UserException.class, () -> userService.updateUser(userRequest, userId));
        verify(userRepository, never()).save(any());
    }
    @Test
    void testCheckDuplicatePhone_DuplicatePhone() {
        // Arrange
        String phoneNumber = "123456789";
        User user = new User();
        user.setPhoneNumber(phoneNumber);
        when(userRepository.findByPhoneNumber(phoneNumber)).thenReturn(Optional.of(new User()));

        // Act
        String result = userService.checkDuplicatePhone(user);

        // Assert
        assertEquals("This phone number is already used by another user!", result);
    }
    @Test
    void testCheckDuplicatePhone_NonDuplicatePhone() {
        // Arrange
        String phoneNumber = "123456789";
        User user = new User();
        user.setPhoneNumber(phoneNumber);
        when(userRepository.findByPhoneNumber(phoneNumber)).thenReturn(Optional.empty());

        // Act
        String result = userService.checkDuplicatePhone(user);

        // Assert
        assertNull(result);
    }
    @Test
    void testCheckDuplicate_UsernameExists() {
        // Arrange
        String username = "john_doe";
        User user = new User();
        user.setUsername(username);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(new User()));

        // Act
        String result = userService.checkDuplicate(user);

        assertEquals("This name already exist!", result);
    }
    @Test
    void testCheckDuplicate_EmailExists() {
        // Arrange
        String email = "john@example.com";
        User user = new User();
        user.setEmail(email);
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(new User()));

        // Act
        String result = userService.checkDuplicate(user);

        // Assert
        assertEquals("This email is already used by another user!", result);
    }
    @Test
    void testCheckDuplicate_PhoneExists() {
        // Arrange
        String phoneNumber = "123456789";
        User user = new User();
        user.setPhoneNumber(phoneNumber);
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(userRepository.findByPhoneNumber(phoneNumber)).thenReturn(Optional.of(new User()));

        // Act
        String result = userService.checkDuplicate(user);

        // Assert
        assertEquals("This phone number is already used by another user!", result);
    }
    @Test
    void testCheckDuplicate_NonDuplicate() {
        // Arrange
        User user = new User();
        user.setUsername("john_doe");
        user.setEmail("john@example.com");
        user.setPhoneNumber("123456789");
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(userRepository.findByPhoneNumber(user.getPhoneNumber())).thenReturn(Optional.empty());

        // Act
        String result = userService.checkDuplicate(user);

        // Assert
        assertNull(result);
    }
    @Test
    void testIsEnabled_UserEnabled() throws UserException {
        // Arrange
        int userId = 1;
        User user = new User();
        user.setId(userId);
        user.setStatus(UserStatus.Active);
        user.setEnabled(true);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        // Act
        boolean result = userService.isEnabled(userId);

        // Assert
        System.out.println("Result: " + result);
        assertTrue(result);
    }
    @Test
    void testIsEnabled_UserNotEnabled() throws UserException {
        // Arrange
        int userId = 2;
        User user = new User();
        user.setId(userId);
        user.setEnabled(false);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act
        boolean result = userService.isEnabled(userId);

        // Assert
        assertFalse(result);
    }
    @Test
    void testIsEnabled_UserNotFound() {
        // Arrange
        int userId = 3;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(UserException.class, () -> userService.isEnabled(userId));
    }
    @Test
    void testGetUserById_UserFound() throws UserException {
        // Arrange
        int userId = 4;
        User user = new User();
        user.setId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act
        User result = userService.getUserById(userId);

        // Assert
        assertNotNull(result);
        assertEquals(userId, result.getId());
    }
    @Test
    void testGetUserById_UserNotFound() {
        // Arrange
        int userId = 5;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(UserException.class, () -> userService.getUserById(userId));
    }
    @Test
    void testUpdateUser_SuccessfulUpdate() throws UserException {
        // Arrange
        UserRequest userRequest = new UserRequest("new_email@example.com", "new_phone_number");
        User existingUser = new User();
        existingUser.setUsername("john_doe");
        existingUser.setEmail("old_email@example.com");
        existingUser.setPhoneNumber("old_phone_number");
        when(userRepository.findById(1)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(existingUser);

        // Act
        User result = userService.updateUser(userRequest, 1);

        // Assert
        assertEquals(userRequest.getEmail(), result.getEmail());
        assertEquals(userRequest.getPhoneNumber(), result.getPhoneNumber());
    }
//    @Test
//    void testUpdateUser_UnsuccessfulUpdate() throws UserException {
//        // Arrange
//        UserRequest userRequest = new UserRequest("new_email@example.com", "new_phone_number");
//        when(userRepository.findById(1)).thenReturn(Optional.empty());
//
//        // Act and Assert
//        assertNull(userService.updateUser(userRequest, 1));
//    }
@Test
void testGetCountUser() {
    // Arrange
    when(userRepository.count()).thenReturn(10L); // Giả lập kết quả trả về

    // Act
    Long result = userService.getcountUser();

    // Assert
    assertEquals(10L, result); // Kiểm tra xem kết quả có phải là 10L như mong đợi hay không
    verify(userRepository, times(1)).count(); // Kiểm tra xem phương thức count đã được gọi một lần hay không
}
    @Test
    void testSaveUser_Success() throws UserException {
        // Arrange
        RegisterRequest request = new RegisterRequest("john_doe", "john_doe@example.com", "John@123", "0123456789","Candidate");
//
//        User user = new User();

        // Giả lập việc kiểm tra trùng lặp không có lỗi
//        when(userRepository.save(any(User.class))).thenReturn(any(User.class));
        ResponseObject result = userService.saveUser(request);
        assertEquals(result.getStatus(),"SUCCESS");
//        when(userService.checkDuplicate(any())).thenReturn(null);
//        when(passwordEncoder.encode("password")).thenReturn("encoded_password");
//
//        // Act
//        ResponseObject result = userService.saveUser(request);
//
//        // Assert
        assertNotNull(result);
        assertEquals("SUCCESS", result.getStatus());
        assertEquals("Create user successfully!", result.getMessage());
        verify(userRepository, times(1)).save(any());
    }
    //    @Test
//    void testSaveUser_DuplicateUser() {
//        // Arrange
//
//        RegisterRequest request = new RegisterRequest("john_doe", "john_doe@example.com", "John@123", "0123456789","Candidate");
//        User user = new User(1,"john_doe","john_doe@example.com", "John@123", "0123456789",Role.Candidate,UserStatus.Active, Date.valueOf("2023-11-02"), Date.valueOf("2023-11-02"));
//        // Giả lập việc kiểm tra trùng lặp có lỗi
//        lenient().when(userService.checkDuplicate(user)).thenReturn("sdg");
//
//        // Act and Assert
//        assertThrows(UserException.class, () -> userService.saveUser(request));
//    }
    @Test
    void testFindById_UserExists() throws UserException {
        // Arrange
        int userId = 1;
        User expectedUser = new User(userId, "john_doe", "john_doe@example.com","John@123", "0123456789",Role.Candidate,UserStatus.Active, Date.valueOf("2023-11-02"), Date.valueOf("2023-11-02"));

        // Mocking the behavior of userRepository.findById
        when(userRepository.findById(userId)).thenReturn(Optional.of(expectedUser));

        // Act
        User actualUser = userService.findById(userId);

        // Assert
        assertEquals(expectedUser, actualUser);

        // Verify that userRepository.findById was called exactly once with the correct userId
        verify(userRepository, times(1)).findById(userId);
    }
    @Test
    void testFindById_UserNotFound() {
        // Arrange
        int userId = 2;

        // Mocking the behavior of userRepository.findById to return an empty Optional
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act and Assert
        UserException exception = assertThrows(UserException.class, () -> userService.findById(userId));

        // Verify that userRepository.findById was called exactly once with the correct userId
        verify(userRepository, times(1)).findById(userId);

        // Verify that the exception message is correct
        assertEquals("User not found!", exception.getMessage());
    }

//    @Test
//    void testEditUser_SuccessfulEdit() throws UserException {
//        // Arrange
//        int userId = 1;
//        EditUserRequest editUserRequest = new EditUserRequest("john_doe1", "john1_doe@example.com","John@1234", "0123456799","Candidate","Active");
//        User existingUser = new User(userId, "john_doe", "john_doe@example.com","John@123", "0123456789",Role.Candidate,UserStatus.Active,Date.valueOf("2023-11-02"), Date.valueOf("2023-11-02"));
//
//        // Mocking the behavior of findById
//        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
//
//        // Mocking the behavior of other checkDuplicate methods
//        when(userService.checkDuplicateUsername(existingUser)).thenReturn(null);
//        when(userService.checkDuplicateEmail(existingUser)).thenReturn(null);
//        when(userService.checkDuplicatePhone(existingUser)).thenReturn(null);
//
//        // Mocking the behavior of userRepository.save
//        when(userRepository.save(any())).thenReturn(existingUser);
//
//        // Act
//        User editedUser = userService.editUser(userId, editUserRequest);
//
//        // Assert
//        assertNotNull(editedUser);
//        assertEquals("john_doe1", editedUser.getUsername());
//        assertEquals("john1_doe@example.com", editedUser.getEmail());
//        assertEquals("0123456799", editedUser.getPhoneNumber());
//        assertEquals(UserStatus.Active, editedUser.getStatus());
//
//        // Verify that userRepository.findById was called exactly once with the correct userId
//        verify(userRepository, times(1)).findById(userId);
//
//        // Verify that userRepository.save was called exactly once
//        verify(userRepository, times(1)).save(existingUser);
//    }

    @Test
    public void testEditUser_DuplicateUsername() {
        // Arrange
        int userId = 1;
        EditUserRequest editUserRequest = new EditUserRequest("john_doe", "newpassword", "john.doe@example.com", "1234567890", "Candidate", "Active");
        User existingUser = new User(userId, "john_doe", "password", "john.doe@example.com", "1234567890", Role.Candidate, UserStatus.Active, Date.valueOf("2021-01-12"), Date.valueOf("2021-01-02"));

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.findByUsername(editUserRequest.getUsername())).thenReturn(Optional.of(existingUser));

        // Act and Assert
        assertThrows(UserException.class, () -> userService.editUser(userId, editUserRequest));
    }

    @Test
    public void testEditUser_DuplicateEmail() {
        // Arrange
        int userId = 1;
        EditUserRequest editUserRequest = new EditUserRequest("john_doe", "newpassword", "john.doe@example.com", "1234567890", "Candidate", "Active");
        User existingUser = new User(userId, "john_doe", "password", "john.doe@example.com", "1234567890", Role.Candidate, UserStatus.Active, Date.valueOf("2021-01-12"), Date.valueOf("2021-01-02"));

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.findByEmail(editUserRequest.getEmail())).thenReturn(Optional.of(existingUser));

        // Act and Assert
        assertThrows(UserException.class, () -> userService.editUser(userId, editUserRequest));
    }

    @Test
    public void testEditUser_DuplicatePhone() {
        // Arrange
        int userId = 1;
        EditUserRequest editUserRequest = new EditUserRequest("john_doe", "newpassword", "john.doe@example.com", "1234567890", "Candidate", "Active");
        User existingUser = new User(userId, "john_doe", "password", "john.doe@example.com", "1234567890", Role.Candidate, UserStatus.Active, Date.valueOf("2021-01-12"), Date.valueOf("2021-01-02"));

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.findByPhoneNumber(editUserRequest.getPhonenumber())).thenReturn(Optional.of(existingUser));

        // Act and Assert
        assertThrows(UserException.class, () -> userService.editUser(userId, editUserRequest));
    }

    @Test
    public void testEditUser_SuccessfulEdit() throws UserException {
        // Arrange
        int userId = 1;
        EditUserRequest editUserRequest = new EditUserRequest("john_doe", "newpassword", "john.doe@example.com", "1234567890", "Candidate", "Active");
        User existingUser = new User(userId, "john_doe", "password", "john.doe@example.com", "1234567890", Role.Candidate, UserStatus.Active, Date.valueOf("2021-01-12"), Date.valueOf("2021-01-02"));

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(existingUser)).thenReturn(existingUser);

        // Act
        User updatedUser = userService.editUser(userId, editUserRequest);

        // Assert
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(existingUser);
        assertNotNull(updatedUser);
        assertEquals(editUserRequest.getUsername(), updatedUser.getUsername());
        // Additional assertions for other fields
    }
}
