package com.learn.rest.webservices.controller;

import com.learn.rest.webservices.dao.UserDaoService;
import com.learn.rest.webservices.exception.UserNotFoundException;
import com.learn.rest.webservices.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class UserResourceController {

    @Autowired
    private UserDaoService service;

    @GetMapping("/users")
    public List<User> retriveAllUsers() {
        return service.findAll();
    }

    @GetMapping("/users/hateoas")
    public ResponseEntity<Object> retriveAllUser() {
        List<User> users = service.findAll();

        for(final User user : users) {
            // user.add(linkTo(methodOn(UserResourceController.class).retriveUser(user.getId())).withSelfRel());
            user.add(linkTo(methodOn(UserResourceController.class).retriveUser(user.getId())).withSelfRel());
        }

        // return new CollectionModel<>(users);

        return new ResponseEntity<>(users, HttpStatus.OK);
    }


    @GetMapping("/users/{id}")
    public User retriveUser(@PathVariable int id) {
        User user = service.findOne(id);

        if (user == null){
            throw new UserNotFoundException("id " + id + " not found");
        }

        // HATEOAS

        return user;
    }

    @PostMapping("/users")
    public ResponseEntity<Object> saveUser(@Valid @RequestBody User user) {
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
