package Authentication.Service.service;

import Authentication.Service.converter.UserConverter;
import Authentication.Service.exception.EmailNotFoundException;
import Authentication.Service.exception.UserAlreadyExistsException;
import Authentication.Service.exception.UserAlreadyInOrganizationException;
import Authentication.Service.model.entity.OrganizationEntity;
import Authentication.Service.model.entity.UserEntity;
import Authentication.Service.repository.UserRepository;
import Authentication.Service.service.impl.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    private final UserConverter userConverter;


    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(username);
        if (userEntity != null)
            return userConverter.toUserDetails(userEntity);
        else
            throw new UsernameNotFoundException("User with email " + username + " not found!");
    }

    @SneakyThrows
    public CustomUserDetails loadUserByEmail(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity != null)
            return userConverter.toUserDetails(userEntity);
        else
            throw EmailNotFoundException.builder()
                    .message("User with email " + email + " not found!")
                    .build();
    }

    @SneakyThrows
    public void signUpUser(CustomUserDetails user) {
        UserEntity userEntity = userRepository.findByEmail(user.getEmail());
        if (userEntity == null) {
            userRepository.save(userConverter.toUserEntity((Authentication.Service.service.CustomUserDetailsImpl) user));
        } else {
            throw UserAlreadyExistsException.builder()
                    .message("User with email " + userEntity.getEmail() + " already exists.")
                    .build();
        }
    }

    public boolean updateUsersOrganization(long user_id, long org_id) {
        UserEntity userEntity = userRepository.findById(user_id);
        if (userEntity.getOrganizationEntity() == null) {
            userEntity.setIsAdmin(1);
            userEntity.setOrganizationEntity(OrganizationEntity.builder().orgId(org_id).build());
            return true;
        }
        return false;
    }

    public boolean checkUserIsAdmin(long user_id, long org_id) {
        UserEntity userEntity = userRepository.findById(user_id);
        if (userEntity.getOrganizationEntity().getOrgId() == org_id && userEntity.getIsAdmin() == 1) {
            return true;
        }
        return false;
    }

    @SneakyThrows
    public void addUserIfExists(String email, long org_id) {
        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity != null && userEntity.getOrganizationEntity() == null) {
            userEntity.setOrganizationEntity(OrganizationEntity.builder().orgId(org_id).build());
        } else if (userEntity == null) {
            throw EmailNotFoundException.builder()
                    .message("You cannot add a user " + email + " to the organization because he was not found.")
                    .build();
        } else {
            throw UserAlreadyInOrganizationException.builder()
                    .message("You cannot add a user " + email + " to the organization because he is already in an organization.")
                    .build();
        }
    }
}

