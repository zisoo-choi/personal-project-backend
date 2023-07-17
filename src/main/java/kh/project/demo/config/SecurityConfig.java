package kh.project.demo.config;

import kh.project.demo.security.JWTUtil;
import kh.project.demo.security.filter.JWTLoginFilter;
import kh.project.demo.security.filter.TokenCheckFilter;
import kh.project.demo.security.handler.JWTLoginFailHandler;
import kh.project.demo.security.handler.JWTLoginSuccessHandler;
import kh.project.demo.security.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.Arrays;
import java.util.List;

@Log4j2
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) // 어노테이션으로 시큐리티 기능 사용할 것인지
@RequiredArgsConstructor
@PropertySource("classpath:security.properties")
public class SecurityConfig {
    private final CustomUserDetailsService customUserDetailsService;
    private final JWTUtil jwtUtil;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {
        // 인코딩 세팅
        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        filter.setEncoding("UTF-8");
        filter.setForceEncoding(true);
        http.addFilterBefore(filter, CsrfFilter.class);

        log.info("[CustomSecurityConfig.filterChain]");
        http.csrf(AbstractHttpConfigurer::disable);

        // cors 설정 @CrossOrigin(origins = "*") 로 도 설정 가능
        http.cors(httpSecurityCorsConfigurer -> {
            httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource());
        });

        // URI 별 접근 권한 설정 @PreAuthorize 로 도 설정 가능
        http.authorizeHttpRequests((authorizeRequests) -> {
            authorizeRequests.requestMatchers("/library-member/sign-up", "/library-member/sign-in").anonymous();
//            authorizeRequests.requestMatchers("/book-list/register-book").hasAnyRole("MANGER");
            authorizeRequests.requestMatchers("/book-list/registration-date").hasAnyRole("NORMAL", "MANGER");
            authorizeRequests.anyRequest().permitAll(); // 모두 접근 가능
        });

        // AuthenticationManager 설정 - 반드시 필요
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());
        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();
        http.authenticationManager(authenticationManager);

        // JWT 로그인용 필터 등록
        JWTLoginFilter jwtLoginFilter = new JWTLoginFilter("/library-member/sign-in");
        jwtLoginFilter.setAuthenticationManager(authenticationManager); // AuthenticationManager 설정 - 반드시 필요
        jwtLoginFilter.setAuthenticationSuccessHandler(new JWTLoginSuccessHandler(jwtUtil)); // 성공 핸들러 등록
        jwtLoginFilter.setAuthenticationFailureHandler(new JWTLoginFailHandler()); // 실패 핸들러 등록
        http.addFilterBefore(jwtLoginFilter, UsernamePasswordAuthenticationFilter.class);

        // JWT 토큰 검증 필터 등록
        TokenCheckFilter tokenCheckFilter = new TokenCheckFilter(jwtUtil, customUserDetailsService);
        http.addFilterBefore(tokenCheckFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("*"));
        configuration.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
