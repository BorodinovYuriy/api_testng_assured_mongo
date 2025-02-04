package org.example.aqa.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class UserCreateRequest {
    private String username;
    private String password;
    private String email;
    private String name;
    private String firstName;
    private String surname;
    private List<String> roles;
    private List<String> positions;
    private List<String> cities;
    private List<String> companies;
}
