package com.sporyap.sporyap.data.network.response.event.categories

data class Result(
    val category: String,
    val defaultSelection: Boolean,
    val id: Int,
    val maximumMemberCount: Int,
    val maximumPlayerCount: Int,
    val minimumPlayerCount: Int,
    val minimumTeamMemberCount: Int,
    val sportCategoriesTypeId: Int
)