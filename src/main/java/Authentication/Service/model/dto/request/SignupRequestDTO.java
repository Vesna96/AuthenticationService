package Authentication.Service.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class SignupRequestDTO {

    @NotBlank
    @Email(message = "Email should be valid!")
    private String email;

    @Size(min = 7, max = 32, message = "Password must be between 7 and 32 characters long!")
    @NotBlank
    private String password;

    @Pattern(regexp = "\\p{L}+", message = "First Name can only contains letters!")
    @NotBlank
    private String name;

    @Pattern(regexp = "\\p{L}+", message = "Last Name can only contains letters!")
    @NotBlank
    private String surname;

}