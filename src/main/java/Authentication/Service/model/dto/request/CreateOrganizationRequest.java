package Authentication.Service.model.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateOrganizationRequest {
    private String name;
    private String description;
}
