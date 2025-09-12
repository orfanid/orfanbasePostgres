package com.orfangenes.repo.ws.Controller;

import com.orfangenes.repo.ws.entity.User;
import com.orfangenes.repo.ws.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


/**
 * @author Suresh Hewapathirana
 */
@Validated
@RestController
@CrossOrigin
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public @ResponseBody
    List<User> all(){
        return  userService.findAllUsers();
    }

    @GetMapping("/user/{id}")
    public User one(@PathVariable Long id) {
        User user = userService.getUser(id);
        return user;
    }

    @GetMapping("/user")
    public User getUserByEmail(@RequestParam(value="email") String email) {
        User user = userService.getUserByEmail(email);
        return user;
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