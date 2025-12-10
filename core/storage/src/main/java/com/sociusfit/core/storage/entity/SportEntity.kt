package com.sociusfit.core.storage.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity Room per gli sport disponibili
 */
@Entity(tableName = "sports")
data class SportEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val createdAt: Long
)