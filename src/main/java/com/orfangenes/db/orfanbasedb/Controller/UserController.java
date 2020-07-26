package com.orfangenes.db.orfanbasedb.Controller;

import com.orfangenes.db.orfanbasedb.entity.User;
import com.orfangenes.db.orfanbasedb.service.UserService;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * @author Suresh Hewapathirana
 */
@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public @ResponseBody
    List<User> all(){
        return  userService.findAllUsers();
    }

    @GetMapping("/user/{id}")
    public EntityModel<User> one(@PathVariable Long id) {
        User user = userService.getUser(id);
        EntityModel<User> resource=new EntityModel<>(user);
        resource.add(
                linkTo(methodOn(this.getClass()).one(id)).withSelfRel(),
                linkTo(methodOn(this.getClass()).all()).withRel("users"));
        return resource;
    }

    @PostMapping("/user")
    public User save(@Valid @RequestBody User user) {
        return userService.saveUser(user);
    }

    @PutMapping("/user/{userId}")
    public User update(@PathVariable Long userId,
                          @Valid @RequestBody User userRequest) {
        return userService.updateUser(userId, userRequest);
    }

    @DeleteMapping("/user/{userId}")
    public ResponseEntity<?> delete(@PathVariable Long userId) {
        return userService.deleteUser(userId);
    }
}