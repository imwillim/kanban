package com.project.kanban.user;

import com.project.kanban.util.RequestBodyError;
import com.project.kanban.util.RequestBodyException;
import com.project.kanban.util.SuccessfulResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/v1")
@Slf4j
public class UserController {
    private final UserFacadeService userFacadeService;

    public UserController(UserFacadeService userFacadeService){
        this.userFacadeService = userFacadeService;
    }

    @GetMapping(path = "/users/{userId}")
    public ResponseEntity<Object> getUser(@PathVariable("userId") long userId){
        Optional<UserDTO> user = userFacadeService.getUserProcess(userId);
        log.info("User is returned.");
        return ResponseEntity.ok(
                new SuccessfulResponse(HttpStatus.OK.value(),
                        "User is returned",
                        user));
    }

    @PostMapping(path = "/signup")
    public ResponseEntity<Object> createUser(@Valid @RequestBody UserRequest requestBody,
                                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder message = RequestBodyError.returnRequiredFields(bindingResult);
            throw new RequestBodyException.BadRequestBody(message);
        }

        Optional<UserDTO> user = userFacadeService.createUserProcess(requestBody);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(user.get().getId()).toUri();
        log.info("Created a new User.");
        return ResponseEntity.created(uri).body(new SuccessfulResponse(HttpStatus.CREATED.value(),
                "Signup new user.",
                user));
    }

    @PutMapping(path = "/users/{userId}")
    public ResponseEntity<Object> updateUser(@PathVariable("userId") long userId,
                                             @Valid @RequestBody UserRequest requestBody,
                                             BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            StringBuilder message = RequestBodyError.returnRequiredFields(bindingResult);
            throw new RequestBodyException.BadRequestBody(message);
        }

        Optional<UserDTO> user = userFacadeService.updateUserProcess(userId, requestBody);
        log.info("User is updated.");
        return ResponseEntity.accepted().body(new SuccessfulResponse(HttpStatus.ACCEPTED.value(),
                "Update a user.",
                user));
    }

    @DeleteMapping(path = "/users/{userId}")
    public ResponseEntity<Object> deleteUser(@PathVariable("userId") long userId) {
        userFacadeService.deleteUserProcess(userId);
        log.info("User is deleted.");
        return new ResponseEntity<>(
                new SuccessfulResponse(HttpStatus.NO_CONTENT.value(),
                        "Delete a user",
                        null), HttpStatus.NO_CONTENT);
    }


}
