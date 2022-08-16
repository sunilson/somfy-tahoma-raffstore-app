package at.sunilson.tahomaraffstorecontroller.authentication.domain

import at.sunilson.tahomaraffstorecontroller.base.UseCase

class LoginUseCase : UseCase<LoginUseCase.Params, Unit>() {

    data class Params(val userName: String, val password: String)

    override suspend fun doWork(params: Params) {
        TODO("Not yet implemented")
    }

}