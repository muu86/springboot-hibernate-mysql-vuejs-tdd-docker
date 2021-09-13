package com.mj.taskagile.web.payload;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RegistrationPayloadTest {

    private Validator validator;

    @BeforeEach
    public void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void validate_blankPayload_shouldFail() {
        RegistrationPayload payload = new RegistrationPayload();

        Set<ConstraintViolation<RegistrationPayload>> violations = validator.validate(payload);

        assertEquals(3, violations.size());
    }

    @Test
    public void validete_payloadWithInvalideEmail_shouldFail() {
        RegistrationPayload payload = 
            new RegistrationPayload(
                "username",
                "BadEmailAddress",
                "123456"
            );

        Set<ConstraintViolation<RegistrationPayload>> violations = validator.validate(payload);

        assertEquals(1, violations.size());
    }

    @Test
    public void validate_payloadWithEmailAddressLongerThan100_shouldFail() {
        int maxLocalPartLength = 64;
        String localPart = RandomStringUtils.random(maxLocalPartLength, true, true);
        int usedLength = maxLocalPartLength + "@".length() + ".com".length();
        String domain = RandomStringUtils.random(101 - usedLength, true, true);
        String emailAddress = localPart + "@" + domain + ".com";
        // System.out.println(emailAddress.length());
        RegistrationPayload payload =
            new RegistrationPayload(
                "usernam",
                emailAddress,
                "password"
            );

        Set<ConstraintViolation<RegistrationPayload>> violations = validator.validate(payload);

        assertEquals(1, violations.size());
    }

    @Test
    public void validate_payloadWithUsernameShorterThan2_shouldFail() {
        String usernameTooShort = RandomStringUtils.random(1);
        RegistrationPayload payload = 
            new RegistrationPayload(
                usernameTooShort,
                "sunny@taskagile.com",
                "121313"
            );

        Set<ConstraintViolation<RegistrationPayload>> violations = validator.validate(payload);
        
        assertEquals(1, violations.size());
    }
  
    @Test
    public void validate_payloadWithUsernameLongerThan50_shouldFail() {
        String usernameTooLong = RandomStringUtils.random(51);
        RegistrationPayload payload = 
            new RegistrationPayload(
                usernameTooLong,
                "sunny@taskagile.com",
                "123456"
            );
    
        Set<ConstraintViolation<RegistrationPayload>> violations = validator.validate(payload);
        
        assertEquals(1, violations.size());
    }
  
    @Test
    public void validate_payloadWithPasswordShorterThan6_shouldFail() {
        String passwordTooShort = RandomStringUtils.random(5);
        RegistrationPayload payload = 
            new RegistrationPayload(
                "username",
                "sunny@taskagile.com",
                passwordTooShort
            );
  
      Set<ConstraintViolation<RegistrationPayload>> violations = validator.validate(payload);
      
      assertEquals(1, violations.size());
    }
  
    @Test
    public void validate_payloadWithPasswordLongerThan30_shouldFail() {
        String passwordTooLong = RandomStringUtils.random(31);
        RegistrationPayload payload = 
            new RegistrationPayload(
                "username",
                "sunny@taskagile.com",
                passwordTooLong
            );
  
      Set<ConstraintViolation<RegistrationPayload>> violations = validator.validate(payload);
      
      assertEquals(1, violations.size());
    }
}
