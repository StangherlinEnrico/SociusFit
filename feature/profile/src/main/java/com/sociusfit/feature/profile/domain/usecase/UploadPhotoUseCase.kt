package com.sociusfit.feature.profile.domain.usecase

import com.sociusfit.core.domain.Result
import com.sociusfit.feature.profile.domain.repository.ProfileRepository
import java.io.File

/**
 * Use case per caricare una foto profilo
 */
class UploadPhotoUseCase(
    private val profileRepository: ProfileRepository
) {

    companion object {
        private const val MAX_FILE_SIZE_MB = 5
        private const val MAX_FILE_SIZE_BYTES = MAX_FILE_SIZE_MB * 1024 * 1024
    }

    suspend operator fun invoke(photoFile: File): Result<String> {
        // Validazione
        if (!photoFile.exists()) {
            return Result.Error(Exception("Il file non esiste"))
        }

        if (photoFile.length() > MAX_FILE_SIZE_BYTES) {
            return Result.Error(Exception("La foto non può superare ${MAX_FILE_SIZE_MB}MB"))
        }

        val extension = photoFile.extension.lowercase()
        if (extension !in listOf("jpg", "jpeg", "png", "webp")) {
            return Result.Error(Exception("Formato non supportato. Usa JPG, PNG o WEBP"))
        }

        return profileRepository.uploadPhoto(photoFile)
    }
}