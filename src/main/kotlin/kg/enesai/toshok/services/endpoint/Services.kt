package kg.enesai.toshok.services.endpoint

import kg.enesai.toshok.dtos.AccountDto
import kg.enesai.toshok.dtos.RegisterForm
import org.springframework.core.io.InputStreamSource
import org.springframework.web.multipart.MultipartFile

interface RegistrationEndpointService {
    fun register(form: RegisterForm): AccountDto
    fun import(file: InputStreamSource)
}