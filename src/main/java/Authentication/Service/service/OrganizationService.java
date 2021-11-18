package Authentication.Service.service;

import Authentication.Service.converter.OrganizationConverter;
import Authentication.Service.exception.OrganizationAlreadyExistsException;
import Authentication.Service.model.entity.OrganizationEntity;
import Authentication.Service.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class OrganizationService {

    @Autowired
    private final OrganizationRepository organizationRepository;
    private final OrganizationConverter organizationConverter;

    @SneakyThrows
    public long saveOrganization(Authentication.Service.service.OrganizationDetailsService organizationDetailsService) {
        OrganizationEntity organizationEntity = organizationRepository.findByName(organizationDetailsService.getName());
        if (organizationEntity == null) {
            organizationEntity = organizationConverter.toOrganizationEntity(organizationDetailsService);
            organizationRepository.save(organizationEntity);
            return organizationEntity.getOrgId();
        } else {
            throw OrganizationAlreadyExistsException.builder()
                    .message("Organization with name " + organizationEntity.getName() + " already exists.")
                    .build();
        }
    }

}
