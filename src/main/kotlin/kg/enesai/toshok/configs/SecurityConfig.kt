package kg.enesai.toshok.configs

import kg.enesai.toshok.services.security.CustomUserDetailsService
import org.springframework.context.annotation.Bean
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.multipart.support.MultipartFilter
import javax.servlet.ServletContext

@EnableWebSecurity
class SecurityConfig(
        private val userDetailsService: CustomUserDetailsService
) : WebSecurityConfigurerAdapter() {

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    override fun configure(http: HttpSecurity) {
        http
                .csrf().disable()
                .authorizeRequests()
                    .antMatchers("/css/**", "/checks/**", "/js/**", "/images/**", "/fonts/**", "/lib/**").permitAll()
                    .antMatchers("/", "/home", "/welcome", "/login", "/registration/**", "/registration/upload-csv").permitAll()
                    .antMatchers("/account/list").hasAuthority("ACCOUNT_VIEW")
                    .antMatchers("/account/approve").hasAuthority("ACCOUNT_APPROVE")
                    .antMatchers("/user/list").hasAuthority("USER_VIEW")
                    .antMatchers("/account/gift-needed").hasAuthority("GIFT_VIEW")
                    .anyRequest().authenticated()
                .and()
                    .formLogin()
                    .loginPage("/login")
                    .defaultSuccessUrl("/dashboard", true)
                    .permitAll()
                .and()
                    .logout()
                    .permitAll()

    }

    override fun configure(builder: AuthenticationManagerBuilder) {
        builder.authenticationProvider(authenticationProvider())
    }

    @Bean
    fun authenticationProvider(): DaoAuthenticationProvider {
        val authProvider = DaoAuthenticationProvider()
        authProvider.setUserDetailsService(userDetailsService)
        authProvider.setPasswordEncoder(passwordEncoder())
        return authProvider
    }
}