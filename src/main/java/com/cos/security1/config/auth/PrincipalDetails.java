package com.cos.security1.config.auth;


//시큐리티가 /login 주소 요청이 오면 낚아채서 로그인을 진행시킨다.
//로그인을 진행이 완료가 되면 session을 만들어준다. (Security ContextHolder)
//오브젝트=>Authentication 객체여야 들어갈수있음
// Authentication 안에 User 정보가 있어야됨.
// User오브젝트에타입 => UserDetails 타입객체

// Security Session => Authentication => UserDetails(PrincipalDetails)

import com.cos.security1.model.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Getter
public class PrincipalDetails implements UserDetails, OAuth2User {
    private User user; //콤포지션
    private Map<String,Object> attributes;
    //일반 로그인
    public PrincipalDetails(User user) {
        this.user = user;
    }
    // OAuth로그인
    public PrincipalDetails(User user,Map<String,Object> attributes) {
        this.user = user;
        this.attributes=attributes;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    // 해당 User의 권한을 리턴하는 곳!!
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collet = new ArrayList<>();
        collet.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return user.getRole();
            }
        });
        return collet;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        // 우리 사이트에서 1년동안 회원이 로그인을 안하면 휴먼 계정으로 하기로함.
        // user.getLoginDate(); 이 날짜를 들고와서 현재 시간 - 로긴 시간으로해서 =>1년 초과하면 return false를 하면 휴먼계정 전환됨
        return true;
    }

    @Override
    public String getName() {
        return null;
    }
}
