package com.example.authservice.interfaces.rest;


import com.example.authservice.application.user.ListUsersHandler;
import com.example.authservice.domain.user.User;
import com.example.authservice.interfaces.rest.dto.user.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    private final ListUsersHandler listUsersHandler;

    public UserController(ListUsersHandler listUsersHandler) {
        this.listUsersHandler = listUsersHandler;
    }

    @GetMapping
    public ResponseEntity<Page<UserResponse>> list(Pageable pageable) {
        Page<UserResponse> page = listUsersHandler.handle(pageable);

        return ResponseEntity.ok(page);
    }
}
