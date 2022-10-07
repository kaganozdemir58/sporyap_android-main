package com.sporyap.sporyap.data.network.response.account.profile_infos

data class Result(
    val birthDate: String,
    val corporate: Corporate,
    val email: String,
    val emailVerified: Boolean,
    val fullName: String,
    val genderTypeName: String,
    val name: String,
    val phone: String,
    val smsVerified: Boolean,
    val surName: String,
    val trainerSports: List<TrainerSport>,
    val userLocations: List<UserLocation>,
    val userName: String,
    val usersSports: List<UsersSport>,
    val userTypeName : String,
    val userTypeId : Int
)