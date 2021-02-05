package com.joonsang.example.SecurityExam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.*;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class SecurityExamApplication {

	public static void main(String[] args) {
		SpringApplication.run(SecurityExamApplication.class, args);
	}

	/**
	 * 패스워드 암호화 처리
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		// Spring Security 5 이전, NoOp 전략 [deprecated]
//		return NoOpPasswordEncoder.getInstance();

		// Spring Security 5 이후, bcrypt 전략
//		return PasswordEncoderFactories.createDelegatingPasswordEncoder();

		// Custom
		String idForEncode = "bcrypt";
		Map encoders = new HashMap<>();
		encoders.put(idForEncode, new BCryptPasswordEncoder());
		encoders.put("noop", NoOpPasswordEncoder.getInstance());
		encoders.put("pbkdf2", new Pbkdf2PasswordEncoder());
		encoders.put("scrypt", new SCryptPasswordEncoder());
		encoders.put("sha256", new StandardPasswordEncoder());

		PasswordEncoder passwordEncoder = new DelegatingPasswordEncoder(idForEncode, encoders);
		return passwordEncoder;
	}
}
