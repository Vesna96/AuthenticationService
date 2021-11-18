package Authentication.Service.repository;

import Authentication.Service.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    UserEntity findByEmail(String email);
    UserEntity findById(long id);
}
