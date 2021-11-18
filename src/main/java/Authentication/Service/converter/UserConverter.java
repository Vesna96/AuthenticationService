package Authentication.Service.converter;

import Authentication.Service.model.dto.request.GoogleRequestDTO;
import Authentication.Service.model.dto.request.SignupRequestDTO;
import Authentication.Service.model.entity.UserEntity;
import Authentication.Service.service.CustomUserDetailsImpl;
import Authentication.Service.service.impl.CustomUserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class UserConverter {

    public CustomUserDetails toCustomUserDetails(SignupRequestDTO dto) {
        return CustomUserDetailsImpl.builder()
                .password(dto.getPassword())
                .email(dto.getEmail())
                .surname(dto.getSurname())
                .name(dto.getName())
                .build();
    }

    public CustomUserDetails toCustomUserDetails(GoogleRequestDTO dto) {
        return CustomUserDetailsImpl.builder()
                .email(dto.getEmail())
                .surname(dto.getSurname())
                .name(dto.getName())
                .build();
    }

    public CustomUserDetailsImpl toUserDetails(UserEntity userEntity) {
        return CustomUserDetailsImpl.builder()
                .id(userEntity.getId())
                .orgId(userEntity.getOrganizationEntity() == null ? 0 : userEntity.getOrganizationEntity().getOrgId())
                .password(userEntity.getPassword())
                .email(userEntity.getEmail())
                .surname(userEntity.getSurname())
                .name(userEntity.getName())
                .build();
    }

    public UserEntity toUserEntity(CustomUserDetailsImpl customUserDetailsImpl) {
        return UserEntity.builder()
                .email(customUserDetailsImpl.getEmail())
                .password(customUserDetailsImpl.getPassword())
                .name(customUserDetailsImpl.getName())
                .surname(customUserDetailsImpl.getSurname())
                .build();
    }
}
