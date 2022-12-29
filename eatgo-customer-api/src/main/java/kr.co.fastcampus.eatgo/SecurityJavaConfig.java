package kr.co.fastcampus.eatgo;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

import javax.servlet.Filter;

@Configuration
@EnableWebSecurity
public class SecurityJavaConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        Filter filter = new JwtAuthenticationFilter(
//                authenticationManager(), jwtUtil());

        http
                .cors().disable()
                .csrf().disable()
                .formLogin().disable();
               // .headers().frameOptions().disable()
               // .and()
                //.addFilter(filter)
               // .sessionManagement()
               // .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }


}
