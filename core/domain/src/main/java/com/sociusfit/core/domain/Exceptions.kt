package com.sociusfit.core.domain

sealed class DomainException(message: String) : Exception(message)

class NetworkException(message: String = "Network error") : DomainException(message)
class ValidationException(message: String) : DomainException(message)
class UnauthorizedException(message: String = "Unauthorized") : DomainException(message)
class ServerException(message: String = "Server error") : DomainException(message)
class NotFoundException(message: String = "Resource not found") : DomainException(message)