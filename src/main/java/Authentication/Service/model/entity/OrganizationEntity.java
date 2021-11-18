package Authentication.Service.model.entity;

import lombok.*;
import javax.persistence.*;
import java.util.List;

@Entity
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Getter
@Table(name = "organizations")
public class OrganizationEntity extends AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long orgId;

    @Column(name = "organization_name", unique = true)
    private String name;

    @Column
    private String description;

    @OneToMany(mappedBy = "organizationEntity", cascade = {CascadeType.ALL})
    private List<UserEntity> users;


}