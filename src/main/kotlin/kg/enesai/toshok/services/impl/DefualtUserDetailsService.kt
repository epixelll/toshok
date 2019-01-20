package kg.enesai.toshok.services.impl

import kg.enesai.toshok.services.AccountService
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class DefualtUserDetailsService(
        private val passwordEncoder: PasswordEncoder,
        private val accountService: AccountService
) : UserDetailsService{
    override fun loadUserByUsername(username: String): UserDetails? {
        return if(username == "test")
        User.builder()
                .username("test")
                .password(passwordEncoder.encode("test"))
                .roles("test")
                .build()
        else null
    }
}