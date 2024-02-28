package com.sparta.hmpah.password;

import com.sparta.hmpah.dto.requestDto.PasswordRequest;
import com.sparta.hmpah.dto.responseDto.PasswordResponse;
import com.sparta.hmpah.entity.PasswordHistory;
import com.sparta.hmpah.entity.User;
import com.sparta.hmpah.repository.PasswordHistoryRepository;
import com.sparta.hmpah.repository.UserRepository;
import com.sparta.hmpah.service.PasswordService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class) // @Mock 사용을 위해 설정합니다.
public class PasswordServiceTest {
    @Mock
    PasswordHistoryRepository passwordHistoryRepository;
    @Mock
    UserRepository userRepository;

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    @Test
    @DisplayName("새로운 비밀번호와 비밀번호 확인이 다른경우")
    void PasswordUpdate1(){
        //given
        PasswordRequest passwordRequest = new PasswordRequest();
        passwordRequest.setOldPassword("jinchan1!");
        passwordRequest.setNewPassword("qweasdzxc1");
        passwordRequest.setCheckPassword("bigman111");
        User user = new User();
        PasswordService passwordService = new PasswordService(passwordHistoryRepository, userRepository, passwordEncoder);

        //when
        PasswordResponse passwordResponse = passwordService.updatePassword(user, passwordRequest);

        //then
        assertEquals("비밀번호와 비밀번호 확인이 다릅니다.",passwordResponse.getMsg());
    }
    @Test
    @DisplayName("현재 비밀번호와 새로운 비밀번호가 일치 하는 경우")
    void PasswordUpdate2(){
        //given
        PasswordRequest passwordRequest = new PasswordRequest();
        passwordRequest.setOldPassword("jinchan1!");
        passwordRequest.setNewPassword("jinchan1!");
        passwordRequest.setCheckPassword("jinchan1!");
        User user = new User();
        PasswordService passwordService = new PasswordService(passwordHistoryRepository, userRepository, passwordEncoder);

        //when
        PasswordResponse passwordResponse = passwordService.updatePassword(user, passwordRequest);

        //then
        assertEquals("기존 비밀번호와 일치합니다.",passwordResponse.getMsg());
    }
    @Test
    @DisplayName("현재 비밀번호 입력이 올바르지 않을 경우")
    void PasswordUpdate3(){
        //given
        PasswordRequest passwordRequest = new PasswordRequest();
        passwordRequest.setOldPassword("jinchan1!");
        passwordRequest.setNewPassword("aaaaaaaaa1@");
        passwordRequest.setCheckPassword("aaaaaaaaa1@");

        User user = new User();
        user.setId(1L);
        user.setPassword(passwordEncoder.encode("otherpassword!1"));

        given(userRepository.findById(1L)).willReturn(Optional.of(user));

        PasswordService passwordService = new PasswordService(passwordHistoryRepository, userRepository, passwordEncoder);
        //when
        PasswordResponse passwordResponse = passwordService.updatePassword(user, passwordRequest);
        //then
        assertEquals("현재 비밀번호가 올바르지 않습니다.",passwordResponse.getMsg());
    }
    @Test
    @DisplayName("과거 2회 이전에 사용 되었던 적이 있는 경우")
    void PasswordUpdate4(){
        //given
        PasswordRequest passwordRequest = new PasswordRequest();
        passwordRequest.setOldPassword("jinchan1!");
        passwordRequest.setNewPassword("aaaaaaaaa1@");
        passwordRequest.setCheckPassword("aaaaaaaaa1@");

        User user = new User();
        user.setId(1L);
        user.setPassword(passwordEncoder.encode("jinchan1!"));

        PasswordHistory passwordHistory1 = new PasswordHistory(user,"aaaaaaaaa4@",1);
        PasswordHistory passwordHistory2 = new PasswordHistory(user,"aaaaaaaaa3@",2);
        PasswordHistory passwordHistory3 = new PasswordHistory(user,"aaaaaaaaa2@",3);
        PasswordHistory passwordHistory4 = new PasswordHistory(user,"aaaaaaaaa1@",4);

        List<PasswordHistory> passwordHistories = new ArrayList<>();
        passwordHistories.add(passwordHistory1);
        passwordHistories.add(passwordHistory2);
        passwordHistories.add(passwordHistory3);
        passwordHistories.add(passwordHistory4);

        when(passwordHistoryRepository.findByUser(any(User.class))).thenReturn(passwordHistories);

        given(userRepository.findById(1L)).willReturn(Optional.of(user));

        PasswordService passwordService = new PasswordService(passwordHistoryRepository, userRepository, passwordEncoder);
        //when
        PasswordResponse passwordResponse = passwordService.updatePassword(user, passwordRequest);
        //then
        assertEquals("최근 사용한 비밀번호입니다. 다른 비밀번호를 사용하세요",passwordResponse.getMsg());
    }
    @Test
    @DisplayName("비밀번호 변경 성공-(과거 3번째 이상 일때도)")
    void PasswordUpdate5(){
        //given
        PasswordRequest passwordRequest = new PasswordRequest();
        passwordRequest.setOldPassword("jinchan1!");
        passwordRequest.setNewPassword("aaaaaaaaa1@");
        passwordRequest.setCheckPassword("aaaaaaaaa1@");

        User user = new User();
        user.setId(1L);
        user.setPassword(passwordEncoder.encode("jinchan1!"));

        PasswordHistory passwordHistory1 = new PasswordHistory(user,"aaaaaaaaa1@",1);
        PasswordHistory passwordHistory2 = new PasswordHistory(user,"aaaaaaaaa3@",2);
        PasswordHistory passwordHistory3 = new PasswordHistory(user,"aaaaaaaaa2@",3);
        PasswordHistory passwordHistory4 = new PasswordHistory(user,"aaaaaaaaa4@",4);

        List<PasswordHistory> passwordHistories = new ArrayList<>();
        passwordHistories.add(passwordHistory1);
        passwordHistories.add(passwordHistory2);
        passwordHistories.add(passwordHistory3);
        passwordHistories.add(passwordHistory4);

        when(passwordHistoryRepository.findByUser(any(User.class))).thenReturn(passwordHistories);

        given(userRepository.findById(1L)).willReturn(Optional.of(user));

        PasswordService passwordService = new PasswordService(passwordHistoryRepository, userRepository, passwordEncoder);
        //when
        PasswordResponse passwordResponse = passwordService.updatePassword(user, passwordRequest);
        //then
        assertEquals("비밀번호가 변경되었습니다.",passwordResponse.getMsg());
    }
}
