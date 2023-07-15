package kh.project.demo.security.filter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kh.project.demo.security.JWTUtil;
import kh.project.demo.security.exception.AccessTokenException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;
@Log4j2
@RequiredArgsConstructor
public class TokenCheckFilter extends OncePerRequestFilter {
    private final JWTUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String method = request.getMethod();
        String uri = request.getRequestURI();
        log.info("TokenCheck Filter......: [{}]: {}", method, uri);

        if (HttpMethod.OPTIONS.matches(method) || jwtUtil.isTokenCheckFilterExcludeUris(uri)) {
            log.info("TokenCheck Filter: pass");
            filterChain.doFilter(request, response);
            return;
        }

        try {
            Map<String, Object> tokenPayload = validateAccessToken(request);

            // jwt 에 담겨있는 memberId 확인
            String memberId = (String) tokenPayload.get("memberId");

            // 존재하는 사용자인지 확인
            UserDetails userDetails = userDetailsService.loadUserByUsername(memberId);

            // 비밀번호 없는 Authentication 으로 바꿔서 전달
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            filterChain.doFilter(request, response);
        } catch (AccessTokenException e) {
            e.sendResponseError(response);
        }
    }
    private Map<String, Object> validateAccessToken(HttpServletRequest request) throws AccessTokenException {
        String tokenStr = getJwtToken(request);
        try {
            Map<String, Object> values = jwtUtil.validateToken(tokenStr);

            return values;
        } catch (MalformedJwtException malformedJwtException) {
            log.error("MalformedJwtException----------------------");
            throw new AccessTokenException(AccessTokenException.TOKEN_ERROR.MALFORM);
        } catch (SignatureException signatureException) {
            log.error("SignatureException----------------------");
            throw new AccessTokenException(AccessTokenException.TOKEN_ERROR.BADSIGN);
        } catch (ExpiredJwtException expiredJwtException) {
            log.error("ExpiredJwtException----------------------");
            throw new AccessTokenException(AccessTokenException.TOKEN_ERROR.EXPIRED);
        }
    }
    private static String getJwtToken(HttpServletRequest request) {
        // 헤더에 정상적으로 토큰이 있는지 확인
        String headerStr = request.getHeader("Authorization");
        if (headerStr == null || headerStr.length() < 8) {
            throw new AccessTokenException(AccessTokenException.TOKEN_ERROR.UNACCEPT);
        }

        // 헤더에 Bearer 접두어 있는지 확인
        String tokenType = headerStr.substring(0, 6);
        if (!"Bearer".equals(tokenType)) {
            throw new AccessTokenException(AccessTokenException.TOKEN_ERROR.BADTYPE);
        }
        String tokenStr = headerStr.substring(7);

        return tokenStr;
    }
}
