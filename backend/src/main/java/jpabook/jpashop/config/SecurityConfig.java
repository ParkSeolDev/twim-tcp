package jpabook.jpashop.config;

import jpabook.jpashop.api.service.UserService;
import jpabook.jpashop.common.auth.CustomAuthenticationFilter;
import jpabook.jpashop.common.auth.JwtAuthenticationFilter;
import jpabook.jpashop.common.auth.TwimUserDetailService;
import jpabook.jpashop.common.exception.handler.LoginFailureHandler;
import jpabook.jpashop.common.exception.handler.LoginSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 인증(authentication) 와 인가(authorization) 처리를 위한 스프링 시큐리티 설정 정의.
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final TwimUserDetailService twimUserDetailService;

    private final UserService userService;
    private final LoginSuccessHandler authenticationSuccessHandler;
    // 인증 실패 핸들러
    private final LoginFailureHandler authenticationFailureHandler;

    private final PasswordEncoderConfig passwordEncoder;

    // Password 인코딩 방식에 BCrypt 암호화 방식 사용
    // @Bean
    // public static PasswordEncoder passwordEncoder() {
    //     return new BCryptPasswordEncoder();
    // }

    // DAO 기반으로 Authentication Provider를 생성
    // BCrypt Password Encoder와 UserDetailService 구현체를 설정
    @Bean
    DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder.passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(this.twimUserDetailService);
        return daoAuthenticationProvider;
    }

    // DAO 기반의 Authentication Provider가 적용되도록 설정
    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 토큰 기반 인증이므로 세션 사용 하지않음
                .and()
                .addFilter(new JwtAuthenticationFilter(authenticationManager(), userService)) //HTTP 요청에 JWT 토큰 인증 필터를 거치도록 필터를 추가
                .authorizeRequests()
                .antMatchers("/", "/api/v1/users/new_sha256", "/api/v1/users").permitAll()
                .antMatchers("/api/v1/users/me").authenticated()       //인증이 필요한 URL과 필요하지 않은 URL에 대하여 설정
//                .antMatchers("/api/v1/order").authenticated()
//                .antMatchers("/api/v1/order/order-list").authenticated()
                .anyRequest().permitAll()
                .and().cors();
    }

    @Bean
    public CustomAuthenticationFilter authenticationFilter() throws Exception {
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManager());
        // 필터 URL 설정
        customAuthenticationFilter.setFilterProcessesUrl("/api/v1/order");
        customAuthenticationFilter.setFilterProcessesUrl("/api/v1/order/order-list");
        // 인증 성공 핸들러
        customAuthenticationFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
        // 인증 실패 핸들러
        customAuthenticationFilter.setAuthenticationFailureHandler(authenticationFailureHandler);
        // BeanFactory에 의해 모든 property가 설정되고 난 뒤 실행
        customAuthenticationFilter.afterPropertiesSet();
        return customAuthenticationFilter;
    }
}
