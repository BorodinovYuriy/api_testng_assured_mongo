package org.example.aqa.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthResponse {

    private String token;

    @JsonProperty("refresh_token")
    private String refreshToken;

    private User user;

    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class User {
        @JsonProperty("_id")
        private String id;
        private List<String> teams;
        private String username;
        private String email;
        private List<String> roles;
        private List<String> positions;
        @JsonProperty("plain_password")
        private String plainPassword;
        private List<String> cities;
        private List<String> companies;
        @JsonProperty("work_history")
        private List<WorkHistory> workHistory;
        @JsonProperty("edu_history")
        private List<EducationHistory> eduHistory;
        @JsonProperty("cd")
        private String cd;
        private String password;
        private String name;

        @JsonProperty("first_name")
        private String firstName;

        @JsonProperty("surname")
        private String surname;

        @Data
        @NoArgsConstructor
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class WorkHistory {
            // Поля если есть
        }

        @Data
        @NoArgsConstructor
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class EducationHistory {
            // Поля если есть
        }
    }
}

