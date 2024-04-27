package com.commerce.memberservice.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.commerce.memberservice.filter.auth.MemberDetailService;
import com.commerce.memberservice.jwt.JwtTokenInfo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

// 서블릿 필터링에 JWT 필터를 추가함으로써 JWT 토큰 여부를 확인하여 사용자의 정보를 추출한다.
// 성공 여부에따라 앞서 만든 JWT 정보 클래스를 통하여 토큰을 생성한다.

@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

	private final JwtTokenInfo jwtTokenInfo;
	private final MemberDetailService memberDetailService;
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {

		final String getHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		final String bearerToken = "Bearer ";
		String jwtToken = null;
		try {
			// 토큰이 없는 상태일경우
			if (getHeader == null || !getHeader.contains(bearerToken)) {
				filterChain.doFilter(request, response);
				return;
			} else {
				// 토큰이 있는 상태임으로 토큰을 추출하여 사용자 아이디를 administrater filter 로 전송하여
				// 데이터가 있는지 확인한다. 있을 경우에는 서비스를 이용 할 수 있도록 통과시키고 없는 경우는 취소시킨다.

				jwtToken = getHeader.split(" ")[1].trim();

				if (jwtTokenInfo.isValidToken(jwtToken)) {
					System.out.println("토큰 만료됨");
					filterChain.doFilter(request, response);
					return;
				}

				String userLoginId = jwtTokenInfo.extractLoginId(jwtToken);

				//여기서 데이터베이스에있는지 체크
				//데이터 체크가 완료됐으면 다음 UsernameToken 을 통하여 인증을 거친다.
				//AbstractAuthenticationToken은 Authentication을 상속받는다는 것을 알 수 있다.
				//즉, UsernamePasswordAuthenticationToken은 추후 인증이 끝나고 SecurityContextHolder.getContext()에 등록될 Authentication 객체이다.

				UserDetails memberDetails = memberDetailService.loadUserByUsername(userLoginId);
				UsernamePasswordAuthenticationToken authenticationToken
					= new UsernamePasswordAuthenticationToken(memberDetails, null, memberDetails.getAuthorities());

				authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authenticationToken);
				filterChain.doFilter(request, response);
			}
		} catch (RuntimeException e) {
			log.error("헤더를 가지고 오지 못했습니다.", e.toString());
			filterChain.doFilter(request, response);
			return;
		}

	}
}
