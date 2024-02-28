package com.sparta.hmpah.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Column
    private String profile;

    @Column
    private String nickname;

    @Column
    @Enumerated(value = EnumType.STRING)
    private UserGenderEnum gender;

    @Column
    private Integer age;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    private Long kakaoId;
    private boolean isExistUser = false;

    public void updateInfo(String nickname,String profile,UserGenderEnum gender,Integer age){
        this.nickname = nickname;
        this.profile = profile;
        this.gender = gender;
        this.age = age;
    }

    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }

    public User(String username, String password, String nickname, String email,
        UserRoleEnum role) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.email = email;
        this.role = role;
    }


    public User( String password, String email, UserRoleEnum role, Long kakaoId) {
        this.password = password;
        this.email = email;
        this.role = role;
        this.kakaoId = kakaoId;
    }

    public User kakaoIdUpdate(Long kakaoId) {
        this.kakaoId = kakaoId;
        return this;
    }

    public User nicknameUpdate(String nickname) {
        this.nickname = nickname;
        return this;
    }

}
