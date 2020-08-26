package com.eric.user.api;

import com.eric.user.api.db.UserRepository;
import com.eric.user.api.models.CreateUserRequest;
import com.eric.user.api.models.ResponseUser;
import com.eric.user.api.models.UserData;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;



    @PostMapping(value = "/users",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseUser createUser(@RequestBody CreateUserRequest request, HttpServletResponse response){
        var data =request.getData();
        System.out.println(request.toString()+"========================================================");
        if(data ==null || StringUtils.isEmpty(data.getUsername())
                || StringUtils.isEmpty(data.getPassword())
                ||  StringUtils.isEmpty(data.getType())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Request body is invalid");
        }

        var searchRes = userRepository.findByusername(data.getUsername());
        if (searchRes.isPresent()) {
            throw  new ResponseStatusException(HttpStatus.CONFLICT,"Username has been registered");
        }

        var savaData  = userRepository.save(data);
        savaData.setPassword(null);
        response.addHeader("Location","/api/user/v1/users/" + savaData.getId()  );
        return new ResponseUser(savaData);
    }

    @GetMapping(
            path = "/users",
            produces = MediaType.APPLICATION_JSON_VALUE)
    private List<UserData> getUsers(){
        return userRepository.findAll();
    }


}
