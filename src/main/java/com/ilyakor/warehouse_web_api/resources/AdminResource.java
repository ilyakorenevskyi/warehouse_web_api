package com.ilyakor.warehouse_web_api.resources;

import com.ilyakor.warehouse_web_api.entities.User;
import com.ilyakor.warehouse_web_api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@PreAuthorize("hasAuthority('ADMIN')")
@RequestMapping("admin")
public class AdminResource {
    @Autowired
    private UserRepository repo;

    @GetMapping("/users")
    public List<User> getAllUsers(){
        List<User> users = (ArrayList<User>)repo.findAll();
        return users;
    }

    @GetMapping("/users/{id}")
    public User getUserById(@PathVariable int id){
        User user = repo.findById(id).orElse(null);
        return user;
    }

    @PostMapping("/users")
    public User createUser(@RequestBody User user){
        user.setPassword(BCrypt.hashpw(user.getPassword(),BCrypt.gensalt(10)));
        repo.save(user);
        return user;
    }

    @PutMapping("/users/{id}")
    public User updateUser(@RequestBody User user, @PathVariable int id){
        user.setId(id);
        repo.save(user);
        return user;
    }

    @DeleteMapping("/user/{id}")
    public void deleteUser(@PathVariable int id){
        repo.deleteById(id);
    }

}
