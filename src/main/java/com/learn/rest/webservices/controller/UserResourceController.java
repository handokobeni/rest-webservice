package com.learn.rest.webservices.controller;

import com.learn.rest.webservices.dao.UserDaoService;
import com.learn.rest.webservices.exception.UserNotFoundException;
import com.learn.rest.webservices.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
public class UserResourceController {

    @Autowired
    private UserDaoService service;

    @GetMapping("/users")
    public List<User> retriveAllUsers() {
        return service.findAll();
    }

    @GetMapping("/users/{id}")
    public User retriveUser(@PathVariable int id) {
        User user = service.findOne(id);

        if (user == null){
            throw new UserNotFoundException("id " + id + " not found");
        }

        return user;
    }

    @PostMapping("/users")
    public ResponseEntity<Object> saveUser(@RequestBody User user) {
        User userSave = service.save(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(userSave.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/users/{id}")
    public User deleteUser(@PathVariable int id) {
        User user = service.deleteById(id);

        if (user == null){
            throw new UserNotFoundException("id " + id + " not found");
        }

        return user;
    }

}
