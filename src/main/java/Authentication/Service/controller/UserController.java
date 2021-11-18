package Authentication.Service.controller;

import Authentication.Service.constants.ApiResponsesConstants;
import Authentication.Service.constants.AuthServiceEndpoints;
import Authentication.Service.converter.OrganizationConverter;
import Authentication.Service.converter.UserConverter;
import Authentication.Service.model.dto.request.*;
import Authentication.Service.model.dto.response.AuthenticationResponse;
import Authentication.Service.model.dto.response.GenericDataResponse;
import Authentication.Service.model.dto.response.ResponseMessage;
import Authentication.Service.model.dto.response.ValidationResponse;
import Authentication.Service.service.CustomUserDetailsImpl;
import Authentication.Service.service.OrganizationService;
import Authentication.Service.service.UserDetailsServiceImpl;
import Authentication.Service.util.JwtUtil;
import com.google.common.hash.Hashing;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.nio.charset.StandardCharsets;

@RestController
@Validated
@RequiredArgsConstructor
public class UserController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;
    private final OrganizationService organizationService;
    private final JwtUtil jwtTokenUtil;
    private final UserConverter userConverter;
    private final OrganizationConverter organizationConverter;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    @ApiResponses({
            @ApiResponse(code = 200, message = ApiResponsesConstants.API_MSG_200),
            @ApiResponse(code = 400, message = ApiResponsesConstants.API_MSG_400),
            @ApiResponse(code = 403, message = ApiResponsesConstants.API_MSG_403),
            @ApiResponse(code = 404, message = ApiResponsesConstants.API_MSG_404)
    })
    @PostMapping(path = AuthServiceEndpoints.LOGIN_V1)
    public ResponseEntity<?> login(@Valid @RequestBody AuthenticationRequest authenticationRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword()));
        CustomUserDetailsImpl customUserDetailsImpl = (CustomUserDetailsImpl) userDetailsService.loadUserByEmail(authenticationRequest.getEmail());
        AuthenticationResponse data = AuthenticationResponse.builder()
                .access_token(jwtTokenUtil.generateToken(customUserDetailsImpl))
                .refresh_token(jwtTokenUtil.generateRefreshToken(customUserDetailsImpl))
                .build();
        return new ResponseEntity<>(GenericDataResponse.builder()
                .message("The user has successfully logged in.")
                .status(HttpStatus.OK.value())
                .data(data)
                .build(), HttpStatus.OK);
    }

    @ApiResponses({
            @ApiResponse(code = 200, message = ApiResponsesConstants.API_MSG_200),
            @ApiResponse(code = 400, message = ApiResponsesConstants.API_MSG_400),
            @ApiResponse(code = 409, message = ApiResponsesConstants.API_MSG_409)
    })
    @PostMapping(path = AuthServiceEndpoints.GOOGLE_LOGIN_V1)
    public ResponseEntity<?> googleLogin(@Valid @RequestBody GoogleRequestDTO googleRequest) {
        CustomUserDetailsImpl customUserDetailsImpl = (CustomUserDetailsImpl) userDetailsService.loadUserByEmail(googleRequest.getEmail());
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(googleRequest.getEmail(), customUserDetailsImpl.getEmail()));
        AuthenticationResponse data = AuthenticationResponse.builder()
                .access_token(jwtTokenUtil.generateToken(customUserDetailsImpl))
                .refresh_token(jwtTokenUtil.generateRefreshToken(customUserDetailsImpl))
                .build();
        return new ResponseEntity<>(GenericDataResponse.builder()
                .message("The user has successfully logged in.")
                .status(HttpStatus.OK.value())
                .data(data)
                .build(), HttpStatus.OK);
    }

    @ApiResponses({
            @ApiResponse(code = 200, message = ApiResponsesConstants.API_MSG_200),
            @ApiResponse(code = 400, message = ApiResponsesConstants.API_MSG_400),
            @ApiResponse(code = 409, message = ApiResponsesConstants.API_MSG_409)
    })
    @PostMapping(path = AuthServiceEndpoints.REGISTER_V1)
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequestDTO signupRequestDTO) {
        String encodedPassword = bCryptPasswordEncoder.encode(signupRequestDTO.getPassword());
        signupRequestDTO.setPassword(encodedPassword);
        userDetailsService.signUpUser(userConverter.toCustomUserDetails(signupRequestDTO));
        return ResponseEntity.status(HttpStatus.OK).body(ResponseMessage.builder()
                .responseMessage("User successfully added.")
                .status(HttpStatus.OK.value())
                .build());
    }

    @ApiResponses({
            @ApiResponse(code = 200, message = ApiResponsesConstants.API_MSG_200),
            @ApiResponse(code = 400, message = ApiResponsesConstants.API_MSG_400),
            @ApiResponse(code = 409, message = ApiResponsesConstants.API_MSG_409)
    })
    @PostMapping(path = AuthServiceEndpoints.GOOGLE_REGISTER_V1)
    public ResponseEntity<?> googleRegister(@Valid @RequestBody GoogleRequestDTO googleRequest) {
        CustomUserDetailsImpl customUserDetailsImpl = (CustomUserDetailsImpl) userConverter.toCustomUserDetails(googleRequest);
        String generatePassword = Hashing.sha256()
                .hashString(googleRequest.getEmail(), StandardCharsets.UTF_8)
                .toString();
        String encodedPassword = bCryptPasswordEncoder.encode(googleRequest.getEmail());
        customUserDetailsImpl.setPassword(encodedPassword);
        userDetailsService.signUpUser(customUserDetailsImpl);
        customUserDetailsImpl = (CustomUserDetailsImpl) userDetailsService.loadUserByEmail(googleRequest.getEmail());
        AuthenticationResponse data = AuthenticationResponse.builder()
                .access_token(jwtTokenUtil.generateToken(customUserDetailsImpl))
                .refresh_token(jwtTokenUtil.generateRefreshToken(customUserDetailsImpl))
                .build();
        return new ResponseEntity<>(GenericDataResponse.builder()
                .message("User successfully added.")
                .status(HttpStatus.OK.value())
                .data(data)
                .build(), HttpStatus.OK);

    }

    @ApiResponses({
            @ApiResponse(code = 200, message = ApiResponsesConstants.API_MSG_200),
            @ApiResponse(code = 400, message = ApiResponsesConstants.API_MSG_400),
            @ApiResponse(code = 401, message = ApiResponsesConstants.API_MSG_401)
    })
    @GetMapping(path = AuthServiceEndpoints.USER_INFO_V1)
    public ResponseEntity<?> userInfo(@RequestHeader("Authorization") String token) {

        String jwt = token.substring(7);
        if (jwtTokenUtil.isValidToken(jwt)) {
            String email = jwtTokenUtil.extractEmail(jwt);
            CustomUserDetailsImpl customUserDetailsImpl = (CustomUserDetailsImpl) userDetailsService.loadUserByEmail(email);
            ValidationResponse response = ValidationResponse.builder()
                    .id(customUserDetailsImpl.getId())
                    .org_id(customUserDetailsImpl.getOrgId())
                    .email(email)
                    .name(customUserDetailsImpl.getName())
                    .surname(customUserDetailsImpl.getSurname())
                    .build();
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResponseMessage.builder()
                .responseMessage("Token expired.")
                .status(HttpStatus.UNAUTHORIZED.value())
                .build());
    }

    @ApiResponses({
            @ApiResponse(code = 200, message = ApiResponsesConstants.API_MSG_200),
            @ApiResponse(code = 400, message = ApiResponsesConstants.API_MSG_400),
            @ApiResponse(code = 401, message = ApiResponsesConstants.API_MSG_401)
    })
    @PostMapping(path = AuthServiceEndpoints.REFRESH_TOKEN_V1)
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {

        jwtTokenUtil.isValidToken(refreshTokenRequest.getRefreshToken());
        CustomUserDetailsImpl customUserDetailsImpl = (CustomUserDetailsImpl) userDetailsService
                .loadUserByEmail(jwtTokenUtil.extractEmail(refreshTokenRequest.getRefreshToken()));
        AuthenticationResponse data = AuthenticationResponse.builder()
                .access_token(jwtTokenUtil.generateToken(customUserDetailsImpl))
                .refresh_token(jwtTokenUtil.generateRefreshToken(customUserDetailsImpl))
                .build();
        return new ResponseEntity<>(GenericDataResponse.builder()
                .message("Token refreshed successfully")
                .status(HttpStatus.OK.value())
                .data(data)
                .build(), HttpStatus.OK);
    }

    @ApiResponses({
            @ApiResponse(code = 200, message = ApiResponsesConstants.API_MSG_200),
            @ApiResponse(code = 400, message = ApiResponsesConstants.API_MSG_400),
            @ApiResponse(code = 403, message = ApiResponsesConstants.API_MSG_403),
            @ApiResponse(code = 409, message = ApiResponsesConstants.API_MSG_409)
    })
    @PostMapping(path = AuthServiceEndpoints.CREATE_ORGANIZATION_V1)
    public ResponseEntity<?> createOrganization(@RequestBody CreateOrganizationRequest createOrganizationRequest,
                                                @RequestHeader("Authorization") String token) {
        String jwt = token.substring(7);
        jwtTokenUtil.isValidToken(jwt);
        long userId = jwtTokenUtil.extractUserId(jwt);
        long orgId = organizationService.saveOrganization(organizationConverter.toOrganizationDetailsService(createOrganizationRequest));
        if (userDetailsService.updateUsersOrganization(userId, orgId)) {
            AuthenticationResponse data = jwtTokenUtil.generateAuthenticationResponse(jwt, userDetailsService);
            return new ResponseEntity<>(GenericDataResponse.builder()
                    .message("Organization with the name " + createOrganizationRequest.getName() + " created successfully.")
                    .status(HttpStatus.OK.value())
                    .data(data)
                    .build(), HttpStatus.OK);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ResponseMessage.builder()
                .responseMessage("The user has already created an organization.")
                .status(HttpStatus.FORBIDDEN.value())
                .build());
    }

    @ApiResponses({
            @ApiResponse(code = 200, message = ApiResponsesConstants.API_MSG_200),
            @ApiResponse(code = 400, message = ApiResponsesConstants.API_MSG_400),
            @ApiResponse(code = 403, message = ApiResponsesConstants.API_MSG_403)
    })
    @PostMapping(path = AuthServiceEndpoints.ADD_USER_V1)
    public ResponseEntity<?> addUser(@RequestBody AddUserInOrganizationRequest addUserInOrganizationRequest,
                                     @RequestHeader("Authorization") String token) {
        String jwt = token.substring(7);
        jwtTokenUtil.isValidToken(jwt);
        int userId = jwtTokenUtil.extractUserId(jwt);
        int orgId = jwtTokenUtil.extractOrganizationId(jwt);
        if (userDetailsService.checkUserIsAdmin(userId, orgId)) {
            userDetailsService.addUserIfExists(addUserInOrganizationRequest.getEmail(), orgId);
            return ResponseEntity.status(HttpStatus.OK).body(ResponseMessage.builder()
                    .responseMessage("The user with the email  " + addUserInOrganizationRequest.getEmail() + " was successfully added to the organization.")
                    .status(HttpStatus.OK.value())
                    .build());
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ResponseMessage.builder()
                .responseMessage("The user is not the admin of the organization.")
                .status(HttpStatus.FORBIDDEN.value())
                .build());
    }

}