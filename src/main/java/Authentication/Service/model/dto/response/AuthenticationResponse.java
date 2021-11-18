package Authentication.Service.model.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuthenticationResponse {

    private String access_token;
    private String refresh_token;
    private final String token_type = "Bearer";

}
