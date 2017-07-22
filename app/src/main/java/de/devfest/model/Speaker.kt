package de.devfest.model

data class Speaker(
        val id: String,
        val name: String,
        val title: String,
        val photoUrl: String,
        val bio: String,
        val company: String?,
        val companyLogo: String?,
        val country: String,
        val socials: List<Social>?,
        val tags: List<Tag>?)