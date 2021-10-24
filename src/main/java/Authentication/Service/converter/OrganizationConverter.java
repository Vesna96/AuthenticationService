package Authentication.Service.converter;

import Authentication.Service.model.dto.request.CreateOrganizationRequest;
import Authentication.Service.model.entity.OrganizationEntity;
import Authentication.Service.service.OrganizationDetailsService;
import org.springframework.stereotype.Component;

@Component
public class OrganizationConverter {

    public OrganizationEntity toOrganizationEntity(OrganizationDetailsService organizationDetailsService) {
        return OrganizationEntity.builder()
                .name(organizationDetailsService.getName())
                .description(organizationDetailsService.getDescription())
                .build();
    }

    public OrganizationDetailsService toOrganizationDetailsService(CreateOrganizationRequest createOrganizationRequest){
        return OrganizationDetailsService.builder()
                .name(createOrganizationRequest.getName())
                .description(createOrganizationRequest.getDescription())
                .build();
    }


}
