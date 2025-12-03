package com.sociusfit.core.domain

abstract class UseCase<in P, out R> {
    suspend operator fun invoke(params: P): Result<R> = try {
        Result.Success(execute(params))
    } catch (e: Exception) {
        Result.Error(e)
    }

    protected abstract suspend fun execute(params: P): R
}

abstract class NoParamsUseCase<out R> {
    suspend operator fun invoke(): Result<R> = try {
        Result.Success(execute())
    } catch (e: Exception) {
        Result.Error(e)
    }

    protected abstract suspend fun execute(): R
}