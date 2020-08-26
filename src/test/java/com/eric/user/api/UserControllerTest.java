package com.eric.user.api;

import com.eric.user.api.db.UserRepository;
import com.eric.user.api.models.CreateUserRequest;
import com.eric.user.api.models.UserData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserRepository userRepository;
    @Test
    public void  createUser_200() throws Exception {
        var mockUserData = new UserData();
        mockUserData.setId(1L);
        mockUserData.setUsername("jack");
        mockUserData.setPassword("123");
        mockUserData.setType("admin");
        Mockito.when(userRepository.save(any())).thenReturn(mockUserData);
        var userDataInRequest = new UserData();
        userDataInRequest.setUsername("eric");
        userDataInRequest.setPassword("123");
        userDataInRequest.setType("admin");
        var request = new CreateUserRequest(userDataInRequest);

        var objectMapper = new ObjectMapper();
        var requestStr =objectMapper.writeValueAsString(request);
        mockMvc.perform(post("/users")
                .content(requestStr)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(header().string("Location","/api/user/v1/users/1"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.password").isEmpty());

    }
private String asString(Object object) throws JsonProcessingException {
            var objectMapper = new ObjectMapper();
         return   objectMapper.writeValueAsString(object);

}

    @Test
    public void create_user400 () throws Exception{
        var userDataInRequest = new UserData();
        userDataInRequest.setUsername("eric");
        userDataInRequest.setPassword("123");
        var request = asString(new CreateUserRequest(userDataInRequest));

        mockMvc.perform(post("/users")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason("Request body is invalid"));
    }

    @Test
    public void create_user409 () throws Exception{
        var user = new UserData();
        Mockito.when(userRepository.findByusername("eric")).thenReturn(Optional.of(user));
        var userDataInRequest = new UserData();
        userDataInRequest.setUsername("eric");
        userDataInRequest.setPassword("123");
        userDataInRequest.setType("admin");
        var request = asString(new CreateUserRequest(userDataInRequest));

        mockMvc.perform(post("/users")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(status().reason("Username has been registered"));
    }
}
