package com.aswe.communicraft.controllers;

import com.aswe.communicraft.exceptions.NotFoundException;
import com.aswe.communicraft.models.dto.ProjectLeaderDto;
import com.aswe.communicraft.models.dto.UserDto;
import com.aswe.communicraft.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/users")
@RequiredArgsConstructor

/*
  The UserController class is a REST controller that handles user-related requests.
 */

public class UserController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    @PostMapping
    public ResponseEntity<String> updateInformation(HttpServletRequest request, @RequestBody UserDto userDto) throws NotFoundException {
        userService.update(userDto , request);
        LOGGER.info("updating info for user: {}", userDto.getUserName());
        return ResponseEntity.ok("User updated successfully.");
    }

    /**
     * Retrieves user information by user ID.
     *
     * @param userID the unique identifier of the user to retrieve
     * @return ResponseEntity containing the DTO representation of the user
     * @throws NotFoundException if the user with the specified ID is not found
     */

    @GetMapping("/{userID}")
    public ResponseEntity<UserDto> findById(@PathVariable int userID) throws NotFoundException {
        UserDto userDto = userService.findById(userID);
        LOGGER.info("finding user with id: {}", userID);
        return ResponseEntity.ok(userDto);
    }
    /**
     * Deletes a user by user ID.
     *
     * @param userID the unique identifier of the user to delete
     * @return ResponseEntity indicating successful deletion of the user
     * @throws NotFoundException if the user with the specified ID is not found
     */

    @DeleteMapping("/{userID}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> deleteUser(@PathVariable int userID) throws NotFoundException {
        userService.deleteUser(userID);
        LOGGER.info("deleting user with id: {}", userID);
        return ResponseEntity.ok("User deleted successfully.");

    }

    /**
     * Retrieves all users in the system.
     *
     * @return ResponseEntity containing a list of DTO representations of all users
     * @throws NotFoundException if no users are found in the system
     */
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<UserDto>> findAllUsers() throws NotFoundException {
       List<UserDto> users = userService.findAllUsers();
        LOGGER.info("finding all users in the system");
        return ResponseEntity.ok(users);
    }

    /**
     * Retrieves user information by username.
     *
     * @param userName the username of the user to retrieve
     * @return ResponseEntity containing the DTO representation of the user
     * @throws NotFoundException if the user with the specified username is not found
     */
    @GetMapping("username/{userName}")
    public ResponseEntity<UserDto> findByUsername(@PathVariable String userName) throws NotFoundException {
        UserDto user = userService.findByUsername(userName);
        LOGGER.info("finding user with username: {}", userName);
        return ResponseEntity.ok(user);
    }
    /**
     * Retrieves users by craft name.
     *
     * @param craftName the name of the craft to search for
     * @return ResponseEntity containing a list of DTO representations of users with the specified craft
     * @throws NotFoundException if no users with the specified craft are found
     */
    @GetMapping("/crafts/{craftName}")
    public ResponseEntity<List<UserDto>> findUsersByCraft(@PathVariable String craftName) throws NotFoundException {
        List<UserDto> users = userService.findUsersByCraft(craftName);
        LOGGER.info("finding user with craft: {}", craftName);
        return ResponseEntity.ok(users);
    }
    /**
     * Assigns a user as a leader for a project.
     *
     * @param projectLeaderDto DTO containing details of the user and project for leader assignment
     * @return ResponseEntity indicating successful assignment of the user as a leader
     * @throws NotFoundException if the user or project specified in the DTO is not found
     */
    @PostMapping("/leader")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> makeLeader(@RequestBody ProjectLeaderDto projectLeaderDto) throws NotFoundException {

        userService.makeLeader(projectLeaderDto);
        LOGGER.info("making {} a leader for project {}", projectLeaderDto.getUserName(), projectLeaderDto.getProjectName());
        return ResponseEntity.ok("the user is now a leader");
    }
}
