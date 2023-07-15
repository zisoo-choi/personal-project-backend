package kh.project.demo.security.filter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.json.GsonJsonParser;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;
@Log4j2
public class JWTLoginFilter extends AbstractAuthenticationProcessingFilter {
    public JWTLoginFilter(String defaultFilterProcessesUrl) {
        super(defaultFilterProcessesUrl);
    }
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException {
        log.info("JWTLoginFilter................................");
        // POST 요청으로 로그인 처리 하기 때문에 같은 URL 에 POST 가 아닌 경우 예외 발생
        if (!HttpMethod.POST.matches(request.getMethod())) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }
        // request 에 있는 HTTP Body 에서 username 과 password 가져옴.
        Map<String, Object> parseJsonMap = parseJsonMap(request);
        String username = (String) parseJsonMap.get("memberId");
        String password = (String) parseJsonMap.get("memberPw");

        // Spring Security 에서 인증 정보를 담는 Authentication 를 구현한 UsernamePasswordAuthenticationToken 생성
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);

        // AuthenticationManager 에게 인증 할 Authentication 을 전달.
        Authentication authenticate = super.getAuthenticationManager().authenticate(authRequest);

        return authenticate;
    }
    private Map<String, Object> parseJsonMap(HttpServletRequest request) throws IOException {
        String body = request.getReader().lines().collect(Collectors.joining());
        GsonJsonParser gsonJsonParser = new GsonJsonParser();

        return gsonJsonParser.parseMap(body);
    }
}
