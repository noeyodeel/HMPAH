package com.sparta.hmpah.post;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.hmpah.config.WebSecurityConfig;
import com.sparta.hmpah.controller.HomeController;
import com.sparta.hmpah.controller.PostController;
import com.sparta.hmpah.controller.UserController;
import com.sparta.hmpah.dto.requestDto.PostRequest;
import com.sparta.hmpah.entity.User;
import com.sparta.hmpah.entity.UserRoleEnum;
import com.sparta.hmpah.security.UserDetailsImpl;
import com.sparta.hmpah.service.KakaoService;
import com.sparta.hmpah.service.PostService;
import com.sparta.hmpah.service.UserService;
import java.security.Principal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

@WebMvcTest(
    controllers = {UserController.class, PostController.class, HomeController.class},
    excludeFilters = {
        @ComponentScan.Filter(
            type = FilterType.ASSIGNABLE_TYPE,
            classes = WebSecurityConfig.class
        )
    }
)
public class PostMvcTest {
  private MockMvc mvc;

  private Principal mockPrincipal;

  @Autowired
  private WebApplicationContext context;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  UserService userService;

  @MockBean
  PostService postService;

  @MockBean
  KakaoService kakaoService;

  @BeforeEach
  public void setup() {
    mvc = MockMvcBuilders.webAppContextSetup(context)
        .apply(springSecurity(new MockSpringSecurityFilter()))
        .build();
  }

  private void mockUserSetup() {
    String username = "tkdduq117";
    String password = "knight314!";
    String nickname = "스파르타";
    String email = "sollertia@sparta.com";
    UserRoleEnum role = UserRoleEnum.USER;
    User testUser = new User(username, password, nickname, email, role);
    UserDetailsImpl testUserDetails = new UserDetailsImpl(testUser);
    mockPrincipal = new UsernamePasswordAuthenticationToken(testUserDetails, "", testUserDetails.getAuthorities());
  }

  @Test
  @DisplayName("로그인 Page")
  void loginPageTest() throws Exception {
    // when - then
    mvc.perform(get("/users/login-page"))
        .andExpect(status().isOk())
        .andExpect(view().name("login"))
        .andDo(print());
  }

  @Test
  @DisplayName("회원 가입 요청 처리")
  void signupTest() throws Exception {
    // given
    MultiValueMap<String, String> signupRequestForm = new LinkedMultiValueMap<>();
    signupRequestForm.add("username", "tkdduq117");
    signupRequestForm.add("password", "knight314!");
    signupRequestForm.add("nickname", "스파르타");
    signupRequestForm.add("email", "sollertia@sparta.com");
    signupRequestForm.add("admin", "false");

    // when - then
    mvc.perform(post("/users/signup")
            .params(signupRequestForm)
        )
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name("redirect:/users/login-page"))
        .andDo(print());
  }

  @Test
  @DisplayName("게시글 작성")
  void createPostTest() throws Exception{
    //given
    this.mockUserSetup();
    String title = "title";
    String content = "content";
    String location = "HONGDAE";
    Integer maxCount = 5;
    PostRequest postRequest = new PostRequest(title, content, location, maxCount);

    String postInfo = objectMapper.writeValueAsString(postRequest);

    //when-then

    mvc.perform(post("/posts")
        .content(postInfo)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(
        MediaType.APPLICATION_JSON)
        .principal(mockPrincipal)
        )
        .andExpect(status().isOk())
        .andDo(print());
  }


}
