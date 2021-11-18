package Authentication.Service.constants;

public class AuthServiceEndpoints {

    public static final String API_BASE = "/api/v1";
    public static final String LOGIN_V1 = API_BASE + "/login";
    public static final String GOOGLE_LOGIN_V1 = API_BASE + "/googleLogin";
    public static final String REGISTER_V1 = API_BASE + "/register";
    public static final String GOOGLE_REGISTER_V1 = API_BASE + "/googleRegister";
    public static final String USER_INFO_V1 = API_BASE + "/userInfo";
    public static final String REFRESH_TOKEN_V1 = API_BASE + "/refresh-token";
    public static final String CREATE_ORGANIZATION_V1 = API_BASE + "/organization";
    public static final String ADD_USER_V1 = API_BASE + "/user";

    private AuthServiceEndpoints() {

    }

}
