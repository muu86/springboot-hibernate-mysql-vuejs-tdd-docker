package com.mj.taskagile.web.apis.authenticate;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class AuthenticationFilterTests {
    
    @MockBean
    private AuthenticationManager authenticationManagerMock;

    @Test
    public void attemptAuthentication_empytyRequestBody_shoudFail() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/api/authentications");
        AuthenticationFilter filter = new AuthenticationFilter();
              
        assertThrows(InsufficientAuthenticationException.class, () -> {
            filter.setAuthenticationManager(authenticationManagerMock);
            filter.attemptAuthentication(request, new MockHttpServletResponse());
        });
    }

    @Test
    public void attemptAuthentication_invalidJsonStringRequestBody_shouldFail() 
        throws IOException {

        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/api/authentications");
        request.setContent("username=testusername&password=TestPassword!".getBytes());
        AuthenticationFilter filter = new AuthenticationFilter();
        
        assertThrows(InsufficientAuthenticationException.class, () -> {
            filter.setAuthenticationManager(authenticationManagerMock);
            filter.attemptAuthentication(request, new MockHttpServletResponse());
        });
    }

    @Test
    public void attemptAuthentication_validJsonStringRequestBody_shouldSucceed()
           throws Exception {

        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/api/authentications");
        request.setContent("{\"username\": \"testusername\", \"password\": \"TestPassword!\"}".getBytes());
        AuthenticationFilter filter = new AuthenticationFilter();

        filter.setAuthenticationManager(authenticationManagerMock);
        filter.attemptAuthentication(request, new MockHttpServletResponse());
        UsernamePasswordAuthenticationToken token =
            new UsernamePasswordAuthenticationToken("testusername", "TestPassword!");

        verify(authenticationManagerMock).authenticate(token);
    }
}
