package com.sparta.hmpah.service;

import com.sparta.hmpah.dto.requestDto.PasswordRequest;
import com.sparta.hmpah.dto.responseDto.PasswordResponse;
import com.sparta.hmpah.entity.PasswordHistory;
import com.sparta.hmpah.entity.User;
import com.sparta.hmpah.repository.PasswordHistoryRepository;
import com.sparta.hmpah.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PasswordService {
    private final PasswordHistoryRepository passwordHistoryRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    //전략에 따라 상태코드를 보내도 되고 에러메시지를 보내도 되고 -> 일단 에러메시지만
    @Transactional
    public PasswordResponse updatePassword(User user, PasswordRequest passwordRequest) {

        //비밀번호와 비밀번호확인이 다른 경우
        if(!passwordRequest.getNewPassword().equals(passwordRequest.getCheckPassword())){
            return new PasswordResponse("비밀번호와 비밀번호 확인이 다릅니다.");
        }
        //이전 비밀번호랑 같을 때
        if(passwordRequest.getOldPassword().equals(passwordRequest.getNewPassword())){
            return new PasswordResponse("기존 비밀번호와 일치합니다.");
        }
        User findUser = userRepository.findById(user.getId()).orElseThrow();
        //기존 비밀번호가 다를 경우
        if(!passwordEncoder.matches(passwordRequest.getOldPassword(),findUser.getPassword())){
            return new PasswordResponse("현재 비밀번호가 올바르지 않습니다.");
        }
        //비밀번호가 과거 2회 이전에 사용이 되었던 적이 있는 경우
        List<PasswordHistory> passwordHistories = passwordHistoryRepository.findByUser(findUser);
        if(passwordHistories.size() <= 2){
            for (PasswordHistory passwordHistory : passwordHistories) {
                if(passwordRequest.getNewPassword().equals(passwordHistory.getPassword())){
                    return new PasswordResponse("최근 사용한 비밀번호입니다. 다른 비밀번호를 사용하세요");
                }
            }
        }
        else {//stream으로 리팩토링해야 할듯
            for(int i = passwordHistories.size()-1 ; i>passwordHistories.size()-3 ; i--){
                if(passwordHistories.get(i).getPassword().equals(passwordRequest.getNewPassword())){
                    return new PasswordResponse("최근 사용한 비밀번호입니다. 다른 비밀번호를 사용하세요");
                }
            }
        }

        //비밀번호 변경
        String newPassword = passwordEncoder.encode(passwordRequest.getNewPassword());
        findUser.updatePassword(newPassword);
        userRepository.save(findUser);
        passwordHistoryRepository.save(new PasswordHistory(user, passwordRequest.getOldPassword(), passwordHistories.size()));
        return new PasswordResponse("비밀번호가 변경되었습니다.");
    }

}
