package Authentication.Service.model.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Entity
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Setter
@Table(name = "users")
public class UserEntity extends AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "email", unique = true)
    private String email;

    @Column
    private String password;

    @Column
    private String name;

    @Column
    private String surname;

    @ManyToOne()
    @JoinColumn(name = "orgId")
    private OrganizationEntity organizationEntity;

    @Column(name = "is_admin")
    private int isAdmin;

}