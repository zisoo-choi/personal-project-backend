package kh.project.demo.security.controller;

import io.jsonwebtoken.ExpiredJwtException;
import kh.project.demo.security.JWTUtil;
import kh.project.demo.security.dto.TokensDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Log4j2
@RestController
@RequestMapping
@RequiredArgsConstructor
public class SecurityController {
    private final JWTUtil jwtUtil;
    @PostMapping("/api/refresh")
    public TokensDTO refresh(@RequestBody TokensDTO tokensDTO) {
        String access = tokensDTO.getAccessToken();
        String refresh = tokensDTO.getRefreshToken();

        // access token 이 만료 되고,
        // refresh token 이 만료 되지 않은 경우
        // 토큰 갱신 한 결과 응답
        try {
            Map<String, Object> accessJwt = jwtUtil.validateToken(access);
            throw new IllegalArgumentException("만료되지 않은 access 토큰");
        } catch (ExpiredJwtException exp) {
        }

        Map<String, Object> refreshJwt = jwtUtil.validateToken(refresh);
        String memberId = (String) refreshJwt.get("memberId");

        String newAccess = jwtUtil.generateToken(Map.of("memberId", memberId), 1);
        String newRefresh = jwtUtil.generateToken(Map.of("memberId", memberId), 30);
        log.info("[Token Refresh]: {}", memberId);
        TokensDTO newTokensDTO = new TokensDTO(newAccess, newRefresh);

        return newTokensDTO;
    }
}
