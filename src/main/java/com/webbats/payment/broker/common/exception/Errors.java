package com.webbats.payment.broker.common.exception;

import com.webbats.payment.broker.common.response.RequestError;
import org.springframework.http.HttpStatus;


public interface Errors {
    RequestError ERROR_PROCESSING = new RequestError("Error occurred while processing your request.", HttpStatus.CONFLICT, 2000L);
    RequestError ERROR_NO_RECORD = new RequestError("No record found.", HttpStatus.NOT_FOUND, 2007L);
    RequestError ERROR_INVALID_CREDENTIALS = new RequestError("Invalid credentials.", HttpStatus.UNAUTHORIZED, 2009L);
    RequestError ERROR_INACTIVE_USER = new RequestError("User is inactive.", HttpStatus.FORBIDDEN, 2015L);
    RequestError ERROR_COUNTRY_NOT_SUPPORTED = new RequestError("User country not allowed", HttpStatus.CONFLICT, 2016L);
    RequestError EMAIL_ALREADY_EXIST = new RequestError("Email already exist.", HttpStatus.BAD_REQUEST, 2031L);
    RequestError PHONE_VALIDATION = new RequestError("Phone number does not match the requirements: Start with (+) and only digits allowed.", HttpStatus.BAD_REQUEST, 2032L);
    RequestError PASSWORD_VALIDATION = new RequestError("Password does not match the requirements: minimum 8 and maximum 16 characters, include at at least one capital letter, one small letter, one digit, and one special character.", HttpStatus.BAD_REQUEST, 3002L);
    RequestError ERROR_SESSION_EXPIRED = new RequestError("Session Expired!", HttpStatus.UNAUTHORIZED, 2048L);
    RequestError ROLE_NOT_EXIST = new RequestError("Role does not exist.", HttpStatus.BAD_REQUEST, 2049L);
    RequestError LANGUAGE_NOT_EXIST = new RequestError("Language does not exist.", HttpStatus.BAD_REQUEST, 2050L);
    RequestError CANNOT_UPDATE_OWN_DETAILS = new RequestError("User cannot update own details", HttpStatus.BAD_REQUEST, 2051L);


}
