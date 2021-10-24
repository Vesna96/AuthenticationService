package Authentication.Service.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class GoogleRequestDTO {
    private String name;
    private String surname;
    private String email;
}
