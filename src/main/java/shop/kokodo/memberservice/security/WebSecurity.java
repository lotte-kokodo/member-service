package shop.kokodo.memberservice.security;

import shop.kokodo.memberservice.service.MemberService;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter{
    private MemberService memberService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private Environment env;


    public WebSecurity(Environment env, MemberService memberService, BCryptPasswordEncoder bCryptPasswordEncoder){
        this.env = env;
        this.memberService = memberService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    // Security 설정 로직
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().httpBasic().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().authorizeRequests().antMatchers("/**").permitAll()
                .anyRequest().authenticated()
                .and().addFilter(getAuthenticationFilter());

        //클릭재킹 끄기
        http.headers().frameOptions().disable();
    }

    // 인증필터 적용 로직
    private AuthenticationFilter getAuthenticationFilter() throws Exception{
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(authenticationManager(), memberService, env);

        return authenticationFilter;
    }

    // 인코드된 password가 정확한지 비교 로직
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(memberService).passwordEncoder(bCryptPasswordEncoder);
    }
}
