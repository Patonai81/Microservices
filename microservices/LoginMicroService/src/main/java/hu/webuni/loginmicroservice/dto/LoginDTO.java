package hu.webuni.loginmicroservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginDTO {

    private String userName;
    private String password;

}
