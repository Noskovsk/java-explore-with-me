package ru.practicum.exploreit.user;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exploreit.user.model.User;
import ru.practicum.exploreit.user.service.UserService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "admin/users")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<User> listUsers(@RequestParam(required = false) ArrayList<Long> ids) {
        return userService.listUsers(ids);
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
    }
}
