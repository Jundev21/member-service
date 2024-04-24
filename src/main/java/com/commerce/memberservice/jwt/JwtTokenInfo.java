package com.commerce.memberservice.jwt;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

//jwt 에 대한정보들 - jwt 유효기간, jwt 정보 추출,
@Component
public class JwtTokenInfo {

	@Value("${jwt.secret.key}")
	private String secretKey;

	@Value("${jwt.token.expired-time}")
	private Long expiredMax;


	// 토큰 생성하기 위한 데이터들
	// jwt 구성요소
	// xxxxx.xxxxx.xxxxx
	// header, payload, signature 3부분으로 나눠진다.
	// header 영역에는 토큰 타입과, 키를 암호화할 알고리즘 종류
	// payload 에는 토큰에 담을 내용(유효기간, 데이터 등등) 이다. Json 형태 key:value 형태이고 claim 이라 부르며 여러개를 담을 수 있다.
	// signature 는 header 에서 사용한 알고리즘을 사용하여 payload 들을 암호화한다.
	public String generateToken(String loginId){
		Claims claims = Jwts.claims();
		claims.put("loginId", loginId);
		return Jwts.builder()
			.setClaims(claims)
			.setIssuedAt(new Date(System.currentTimeMillis()))
			.setExpiration(new Date(System.currentTimeMillis() + expiredMax))
			.signWith(getKey(secretKey), SignatureAlgorithm.HS256)
			.compact();
	}

	public String extractLoginId(String jwt){
		return extractClaim(jwt).get("loginId", String.class);
	}

	public boolean isValidToken(String jwt){
		Date expriedDate = extractClaim(jwt).getExpiration();
		return expriedDate.before(new Date());
	}

	//key 값 생성
	public Key getKey(String key){
		byte [] keyBytes = key.getBytes(StandardCharsets.UTF_8);
		return Keys.hmacShaKeyFor(keyBytes);
	}

	//JWT는 Claim를 JSON형태로 표현
	public Claims extractClaim(String jwt){
		return Jwts.parserBuilder()
			.setSigningKey(getKey(secretKey))
			.build()
			.parseClaimsJws(jwt)
			.getBody();
	}
}
