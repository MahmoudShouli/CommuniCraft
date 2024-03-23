package com.aswe.communicraft.controllers;

import com.aswe.communicraft.exceptions.NotFoundException;
import com.aswe.communicraft.models.dto.UserDto;
import com.aswe.communicraft.services.AuthService;
import com.aswe.communicraft.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/{id}")
    public ResponseEntity<?> updateInformation(@PathVariable int id, @RequestBody UserDto userDto) throws NotFoundException {
        userService.update(userDto , id);
        return ResponseEntity.ok("User updated successfully!");
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable int id) throws NotFoundException {
        UserDto userDto = userService.findById(id);
        return ResponseEntity.ok(userDto);
    }

}
