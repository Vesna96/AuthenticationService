package Authentication.Service.repository;

import Authentication.Service.model.entity.OrganizationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizationRepository extends JpaRepository<OrganizationEntity, Integer> {
    OrganizationEntity findByName(String name);
}
