package kg.enesai.toshok.services.endpoint

import kg.enesai.toshok.dtos.AccountDto
import kg.enesai.toshok.dtos.RegisterForm

interface RegistrationEndpointService {
    fun register(form: RegisterForm): AccountDto
}