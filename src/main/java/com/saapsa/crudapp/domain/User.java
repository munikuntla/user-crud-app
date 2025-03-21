package com.saapsa.crudapp.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import com.saapsa.crudapp.api.model.UserRequest;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    private Long id;
    
    @NotBlank(message = "Name cannot be blank")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;
    
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email should be valid")
    private String email;
    
    @NotNull(message = "Age cannot be null")
    @Min(value = 0, message = "Age should not be less than 0")
    @Max(value = 120, message = "Age should not be greater than 120")
    private Integer age;
    
    // Conversion methods between entity and API models
    public static User fromUserRequest(UserRequest userRequest) {
        return User.builder()
                .name(userRequest.getName())
                .email(userRequest.getEmail())
                .age(userRequest.getAge())
                .build();
    }
    
    public com.saapsa.crudapp.api.model.User toApiUser() {
        com.saapsa.crudapp.api.model.User apiUser = new com.saapsa.crudapp.api.model.User();
        apiUser.setId(this.id);
        apiUser.setName(this.name);
        apiUser.setEmail(this.email);
        apiUser.setAge(this.age);
        return apiUser;
    }
}
