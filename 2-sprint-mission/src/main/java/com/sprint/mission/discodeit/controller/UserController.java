package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.dto.UserStatusDto;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserStatusService userStatusService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<UserDto.Response> create(
            @RequestBody UserDto.CreateRequest request) {
        UserDto.Response response = userService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<UserDto.Response> update(
            @PathVariable UUID id,
            @RequestBody UserDto.UpdateRequest request) {
        UserDto.Response response = userService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(
            @PathVariable UUID id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<UserDto.Response>> findAll() {
        List<UserDto.Response> responseList = userService.findAll();
        return ResponseEntity.ok(responseList);
    }

    @RequestMapping(value = "/{id}/status", method = RequestMethod.PATCH)
    public ResponseEntity<UserStatusDto.Response> updateUserStatus(
            @PathVariable UUID id) {

        UserStatusDto.Response response = userStatusService.updateByUserId(id);
        return ResponseEntity.ok(response);
    }
}
