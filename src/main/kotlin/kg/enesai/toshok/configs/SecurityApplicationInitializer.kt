package kg.enesai.toshok.configs

import org.springframework.web.multipart.support.MultipartFilter
import javax.servlet.ServletContext
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer


class SecurityApplicationInitializer : AbstractSecurityWebApplicationInitializer() {

    override fun beforeSpringSecurityFilterChain(servletContext: ServletContext?) {
        insertFilters(servletContext, MultipartFilter())
    }
}