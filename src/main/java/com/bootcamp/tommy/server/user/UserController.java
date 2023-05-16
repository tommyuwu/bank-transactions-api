/*package com.bootcamp.tommy.server.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/findall")
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @PostMapping("/add")
    public EndpointResponse addUser(@RequestBody EndpointRequest<User> request) {
        var response = new EndpointResponse<User>();
        response.setMessage(request.getMessage());
        return response;
    }

    @DeleteMapping("/delete/{userID}")
    public void deleteUser(@PathVariable Long userID) {
        if(!(userRepository.existsById(userID))) {
            throw new IllegalStateException("user doesnt exist");
        } else {
            userRepository.deleteById(userID);
        }
    }
}*/

