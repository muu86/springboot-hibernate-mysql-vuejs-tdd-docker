package com.mj.taskagile.web.apis;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.mj.taskagile.domain.application.UserService;
import com.mj.taskagile.domain.model.EmailAddressExistsException;
import com.mj.taskagile.domain.model.UsernameExistsException;
import com.mj.taskagile.utils.JsonUtils;
import com.mj.taskagile.web.payload.RegistrationPayload;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

// @SpringBootTest
// @AutoConfigureMockMvc
// @ActiveProfiles("home")
@WebMvcTest(RegistrationApiController.class)
// @ExtendWith(MockitoExtension.class)
public class RegistrationApiControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService serviceMock;

    @Test
    public void register_blankPayload_shoudFailAndReturn400() throws Exception {
        mvc.perform(post("/api/registrations"))
            .andExpect(status().is(400));
    }

    @Test
    public void register_existedUsername_shouldFailAndReturn400() throws Exception {
        RegistrationPayload payload = 
            new RegistrationPayload(
                "exist",
                "test@taskagile.com",
                "123456");

        doThrow(UsernameExistsException.class)
            .when(serviceMock)
            .register(payload.toCommand());

        mvc.perform(
            post("/api/registrations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtils.toJson(payload)))
        .andExpect(status().is(400))
        .andExpect(jsonPath("$.message").value("등록된 이름입니다"));
    }

    @Test
    public void register_existedEmailAddress_shouldFailAndReturn400() throws Exception {
        RegistrationPayload payload = 
            new RegistrationPayload(
                "test",
                "exist@taskagile.com",
                "123456");

        doThrow(EmailAddressExistsException.class)
            .when(serviceMock)
            .register(payload.toCommand());

        mvc.perform(
            post("/api/registrations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtils.toJson(payload)))
        .andExpect(status().is(400))
        .andExpect(jsonPath("$.message").value("등록된 이메일입니다"));
    }

    @Test
    public void register_validPayload_shouldSucceedAndReturn200() throws Exception {
        RegistrationPayload payload =
            new RegistrationPayload(
                "sunny",
                "sunny@taskagile.com",
                "123456"
            );

        doNothing()
            .when(serviceMock)
            .register(payload.toCommand());

        mvc.perform(
            post("/api/registrations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtils.toJson(payload)))
        .andExpect(status().is(201));
    }
}
