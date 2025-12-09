package com.sociusfit.feature.auth.data.mapper

import com.sociusfit.core.domain.model.User
import com.sociusfit.core.network.dto.auth.UserDto
import com.sociusfit.core.storage.entity.UserEntity

/**
 * Mappers per conversione tra i layer
 *
 * DTO (Data Transfer Object) -> Domain Model
 * Domain Model -> Entity (Room Database)
 */

/**
 * Converte UserDto (network) in User (domain)
 *
 * NOTA: UserDto dal backend non contiene profilePhotoUrl e profileComplete
 * Questi campi verranno aggiunti in Sprint 2 con il profilo completo.
 * Per ora usiamo valori di default.
 */
fun UserDto.toUser(): User {
    return User(
        id = this.id,
        firstName = this.firstName,
        lastName = this.lastName,
        email = this.email,
        profilePhotoUrl = null, // Sarà popolato in Sprint 2 dopo profilo
        profileComplete = false, // Sarà aggiornato in Sprint 2
        createdAt = System.currentTimeMillis()
    )
}

/**
 * Converte User (domain) in UserEntity (database)
 *
 * NOTA: UserEntity attualmente ha solo i campi base.
 * profilePhotoUrl e profileComplete verranno aggiunti in ProfileEntity (Sprint 2).
 */
fun User.toUserEntity(): UserEntity {
    return UserEntity(
        id = this.id,
        firstName = this.firstName,
        lastName = this.lastName,
        email = this.email
    )
}

/**
 * Converte UserEntity (database) in User (domain)
 *
 * NOTA: I campi mancanti vengono popolati con valori di default.
 * In Sprint 2 questi verranno recuperati da ProfileEntity.
 */
fun UserEntity.toUser(): User {
    return User(
        id = this.id,
        firstName = this.firstName,
        lastName = this.lastName,
        email = this.email,
        profilePhotoUrl = null,
        profileComplete = false,
        createdAt = System.currentTimeMillis()
    )
}