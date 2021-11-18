package Authentication.Service.model.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ValidationResponse {
    private long id;
    private long org_id;
    private String email;
    private String name;
    private String surname;

}