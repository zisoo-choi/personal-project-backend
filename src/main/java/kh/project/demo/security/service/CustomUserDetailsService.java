package kh.project.demo.security.service;

import kh.project.demo.library.member.entity.Member;
import kh.project.demo.library.member.repository.MemberRepository;
import kh.project.demo.security.dto.CustomUserDetail;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@RequiredArgsConstructor
@Service
@Log4j2
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByMemberId(username).orElseThrow();
        UserDetails userDetails = CustomUserDetail.builder()
                .username(username)
                .password(member.getMemberPw())
                .userNo(member.getMemberNumber())
                .authorities(Collections.singleton(new SimpleGrantedAuthority("ROLE_" + member.getMemberRole().name())))
                .isEnabled(true)
                .isCredentialsNonExpired(true)
                .isAccountNonLocked(true)
                .isAccountNonExpired(true)
                .build();

        return userDetails;
    }
}
