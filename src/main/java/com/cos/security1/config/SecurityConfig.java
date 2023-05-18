package com.cos.security1.config;


import com.cos.security1.config.oauth.PrincipalOauth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration //메모리에 등재시켜야됨
@EnableWebSecurity //활성화를하면 스프링 시큐리티 필터가 스프링 필터체인에 등록이 된다.
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig  {
    @Autowired
    private PrincipalOauth2UserService principalOauth2UserService;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.csrf().disable();
        http.authorizeRequests().antMatchers("/user/**").authenticated()
                .antMatchers("/manager/**").access("hasRole('ROLE_ADMIN')or hasRole('ROLE_MANAGER')")
                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
                .anyRequest().permitAll()
                .and()
                .formLogin()
                .loginPage("/loginForm")
                .loginProcessingUrl("/login") // /login주소가 호출이 되면 시큐리티가 낚아채서 대신 로그인을 진행중.
                .defaultSuccessUrl("/")
                .and()
                .oauth2Login()
                .loginPage("/loginForm") //구글 로그인이 완료된뒤 후처리가 필요함. 1.코드받기(인증받음) 2.엑세스토큰받기(권한)
                // 3. 사용자프로필 정보를 가져옴  4.그 정보를 토대로 회원가입을 자동으로 진행시키기도함
                // 4.2 (이메일,전화번호,이름,아이디) 쇼핑몰로 치면 집주소도 필요, 백화점몰 -> (vip등급,일반등급)
                // 현재 여기서는 엑세스토큰_사용자프로필 정보를 받음)
                .userInfoEndpoint()
                .userService(principalOauth2UserService);
        return http.build();
    }
    @Bean //비밀번호 보안을 위한 객체를 만들어줌
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
