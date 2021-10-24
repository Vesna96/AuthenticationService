package Authentication.Service.model.dto.request;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
@Builder
public class AuthenticationRequest {
    @NotBlank(message="Email cannot be left blank!")
    private String email;

    @NotBlank(message="Password cannot be left blank!")
    private String password;

}