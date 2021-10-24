package Authentication.Service.service;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class OrganizationDetailsService {
    private int id;
    private String name;
    private String description;

}
