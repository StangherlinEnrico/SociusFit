package com.sociusfit.core.network.dto.profile

import com.google.gson.annotations.SerializedName

/**
 * Request DTO per aggiungere uno sport con livello
 * Corrisponde al backend AddSportRequest
 *
 * IMPORTANTE: Il backend si aspetta un NUMERO per il level, non una stringa!
 * Backend enum: Beginner=1, Intermediate=2, Advanced=3
 */
data class SportLevelRequest(
    @SerializedName("sportId")
    val sportId: String,

    @SerializedName("level")
    val level: Int // 1=Beginner, 2=Intermediate, 3=Advanced
)
