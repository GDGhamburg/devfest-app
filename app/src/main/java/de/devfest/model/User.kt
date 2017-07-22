package de.devfest.model

data class User(
        val userId: String,
        val email: String?,
        val photoUrl: String? = null,
        val displayName: String?,
        val admin: Boolean = false
)