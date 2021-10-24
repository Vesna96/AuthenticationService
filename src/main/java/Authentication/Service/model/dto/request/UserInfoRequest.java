package Authentication.Service.model.dto.request;


import lombok.*;

@Getter
@Builder
public class UserInfoRequest {
    private String jwt;
}
