package com.example.domainLogin.domain.usecase

import com.example.core.dispatcher.BaseDispatcherProvider
import com.example.core.ext.isNetworkException
import com.example.domainLogin.domain.model.LoginInfo
import com.example.domainLogin.domain.repository.LoginRepository
import com.example.domainLogin.domain.usecase.LoginUseCase.Output
import com.example.domainLogin.domain.usecase.LoginUseCase.Output.Success
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LoginUseCaseImpl @Inject constructor(
    private val repository: LoginRepository,
    private val dispatcherProvider: BaseDispatcherProvider
) : LoginUseCase {
    override fun execute(input: LoginUseCase.Input): Flow<Output> {
        return repository.fetchToken(
            LoginInfo(userName = input.userName, password = input.password)
        ).map { token ->
            repository.saveToken(token = token)
            Success(token) as Output
        }.catch { exception ->
            if (exception.isNetworkException()) {
                emit(Output.NetworkError)
            } else {
                emit(Output.UnknownError(exception.message.orEmpty()))
            }
        }.flowOn(dispatcherProvider.io())
    }
}
