package com.serv.oeste.presentation.controllers;

import com.serv.oeste.application.dtos.reponses.UserResponse;
import com.serv.oeste.application.dtos.requests.PageFilterRequest;
import com.serv.oeste.application.dtos.requests.UserRegisterRequest;
import com.serv.oeste.application.dtos.requests.UserUpdateRequest;
import com.serv.oeste.application.services.UserService;
import com.serv.oeste.domain.valueObjects.PageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<PageResponse<UserResponse>> findAll(@ModelAttribute PageFilterRequest pageFilter) {
        return ResponseEntity.ok(userService.findAll(pageFilter.toPageFilter()));
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody UserRegisterRequest registerRequest) {
        userService.register(registerRequest);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @PutMapping
    public ResponseEntity<Void> update(@Valid @RequestBody UserUpdateRequest userUpdateRequest) {
        userService.update(userUpdateRequest);

        return ResponseEntity
                .ok()
                .build();
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestParam("username") String username) {
        userService.delete(username);

        return ResponseEntity
                .ok()
                .build();
    }
}
