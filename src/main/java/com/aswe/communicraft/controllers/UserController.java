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
public class UserController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;


    @PostMapping
    public ResponseEntity<String> updateInformation(HttpServletRequest request, @RequestBody UserDto userDto) throws NotFoundException {
        userService.update(userDto , request);
        LOGGER.info("updating info for user: {}", userDto.getUserName());
        return ResponseEntity.ok("User updated successfully.");
    }

    @GetMapping("/{userID}")
    public ResponseEntity<UserDto> findById(@PathVariable int userID) throws NotFoundException {
        UserDto userDto = userService.findById(userID);
        LOGGER.info("finding user with id: {}", userID);
        return ResponseEntity.ok(userDto);
    }

    @DeleteMapping("/{userID}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> deleteUser(@PathVariable int userID) throws NotFoundException {
        userService.deleteUser(userID);
        LOGGER.info("deleting user with id: {}", userID);
        return ResponseEntity.ok("User deleted successfully.");

    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<UserDto>> findAllUsers() throws NotFoundException {
       List<UserDto> users = userService.findAllUsers();
        LOGGER.info("finding all users in the system");
        return ResponseEntity.ok(users);
    }

    @GetMapping("username/{userName}")
    public ResponseEntity<UserDto> findByUsername(@PathVariable String userName) throws NotFoundException {
        UserDto user = userService.findByUsername(userName);
        LOGGER.info("finding user with username: {}", userName);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/crafts/{craftName}")
    public ResponseEntity<List<UserDto>> findUsersByCraft(@PathVariable String craftName) throws NotFoundException {
        List<UserDto> users = userService.findUsersByCraft(craftName);
        LOGGER.info("finding user with craft: {}", craftName);
        return ResponseEntity.ok(users);
    }
    @PostMapping("/leader")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> makeLeader(@RequestBody ProjectLeaderDto projectLeaderDto) throws NotFoundException {

        userService.makeLeader(projectLeaderDto);
        LOGGER.info("making {} a leader for project {}", projectLeaderDto.getUserName(), projectLeaderDto.getProjectName());
        return ResponseEntity.ok("the user is now a leader");
    }

}
