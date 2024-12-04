package fr.cyril.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

	@Bean
	public InMemoryUserDetailsManager inMemoryUserDetailsManager(PasswordEncoder passwordEncoder) {
		
		String encodedPasswordString = passwordEncoder.encode("1234");
		
		return new InMemoryUserDetailsManager(
				User.withUsername("user1").password(encodedPasswordString).roles("USER").build(),
				User.withUsername("user2").password(passwordEncoder.encode("6789")).roles("USER").build(),
				User.withUsername("admin").password(encodedPasswordString).roles("USER", "ADMIN").build()

		);
	}
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		return httpSecurity
				.formLogin(Customizer.withDefaults())
				//.authorizeHttpRequests(req->req.requestMatchers("/deletePatient/**", "/savePatient/**", "/editPatient/**").hasRole("ADMIN"))	
				//.authorizeHttpRequests(req->req.requestMatchers("/admin/**").hasRole("ADMIN"))	
				.exceptionHandling(r -> r.accessDeniedPage("/notAuthorized"))
				.authorizeHttpRequests(req->req.anyRequest().authenticated())							
				.build();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder( ) {
		return new BCryptPasswordEncoder();
	}
	
}
