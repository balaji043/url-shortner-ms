package tech.balaji.urlshortener.utils;

import lombok.experimental.UtilityClass;
import org.springframework.security.access.AccessDeniedException;

import java.util.function.Supplier;

@UtilityClass
public class MessageConstants {

    public static final String AUTHENTICATION_ACTION_MESSAGE = "User %s successfully.";

    public static final String SIGNED_UP = "Signed Up successfully";
    public static final String LOGGED_IN = "Logged in successfully";
    public static final String LOGGED_OUT = "Logged out successfully";

    public static final String URL_GENERATED = "Url Generated Successfully";
    public static final String SUCCESS = "Success";
    public static final String EMAIL_ID_EXISTS = "Email ID exists";
    public static final String UNAUTHENTICATED_USER = "Unauthenticated User";


    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String SIGN_UP_URL = "/u/sign-up";
    public static final String LOGIN_URL = "/u/login";
    public static final String REDIRECT_URL = "/u/**";

    public static final  Supplier<AccessDeniedException> throwAccessDeniedException = () -> new AccessDeniedException(UNAUTHENTICATED_USER);
}
