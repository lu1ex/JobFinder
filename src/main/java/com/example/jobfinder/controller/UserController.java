package com.example.jobfinder.controller;

import com.example.jobfinder.entity.UserEntity;
import com.example.jobfinder.modelDTO.UserDTO;
import com.example.jobfinder.requestBodyModels.UserRequestBodyModel;
import com.example.jobfinder.services.MapperService;
import com.example.jobfinder.services.UserService;
import io.swagger.annotations.ApiModel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@ApiModel("User Controller")
public class UserController {
    private final UserService userService;
    private final MapperService mapperService;

    @PostMapping
    public ResponseEntity<UserDTO> create(@RequestBody UserRequestBodyModel userRequestBodyModel) {
        UserEntity userEntity = userService.createEntity(userRequestBodyModel);
        UserDTO userDTO = mapperService.mapObjectToOtherObject(userEntity, UserDTO.class);
        return new ResponseEntity<>(userDTO, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> get(@PathVariable String id) {
        UserEntity userEntity = userService.getEntityById(id);
        UserDTO userDTO = mapperService.mapObjectToOtherObject(userEntity, UserDTO.class);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Set<UserDTO>> getAll() {
        Set<UserDTO> userDTOSet = mapperService.mapSetToSetOfClass(userService.getAllEntities(), UserDTO.class);
        return new ResponseEntity<>(userDTOSet, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable String id) {
        userService.deleteEntityById(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> put(@PathVariable String id, @RequestBody UserRequestBodyModel userRequestBodyModel) {
        UserEntity userEntity = userService.putEntity(id, userRequestBodyModel);
        UserDTO userDTO = mapperService.mapObjectToOtherObject(userEntity, UserDTO.class);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserDTO> patch(
            @PathVariable String id, @RequestBody Map<Object, Object> fields) {
        UserEntity userEntity = userService.patchEntity(id, fields);
        UserDTO userDTO = mapperService.mapObjectToOtherObject(userEntity, UserDTO.class);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }
}
