package com.test.work.controller;

import com.test.work.api.UserApi;
import com.test.work.email.EmailNotificationService;
import com.test.work.email.NotificationStatus;
import com.test.work.exception.ErrorResponseException;
import com.test.work.model.PageUser;
import com.test.work.model.User;
import com.test.work.model.UserCreateRequest;
import com.test.work.model.UserUpdateRequest;
import com.test.work.exception.ExceptionService;
import com.test.work.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController implements UserApi {

    private final UserService service;

    private final ExceptionService exceptionService;

    private final EmailNotificationService emailNotificationService;


    @Override
    public ResponseEntity<User> createUser(UserCreateRequest userCreateRequest) {
        User user;
        try {
            user = service.createUser(userCreateRequest);
            log.info("create user {}", user);
            emailNotificationService.sendNotification(user, NotificationStatus.CREATE);
        } catch (ErrorResponseException e) {
            throw exceptionService.generateResultCodeException(e);
        }
        return ResponseEntity.ok(user);
    }

    @Override
    public ResponseEntity<PageUser> searchUsers(
            LocalDate dateOfBirth, String phone, String name, String email, Integer page, Integer size) {
        return ResponseEntity.ok(service.findUsers(dateOfBirth, phone, name, email, page, size));
    }


    @Override
    public ResponseEntity<User> updateUser(Long userId, UserUpdateRequest userUpdateRequest) {
        User user;
        try {
            user = service.updateUser(userId, userUpdateRequest);
            log.info("update user {}", user);
        } catch (ErrorResponseException e) {
            throw exceptionService.generateResultCodeException(e);
        }
        return ResponseEntity.ok(user);
    }
}