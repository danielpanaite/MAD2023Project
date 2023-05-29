package com.example.courtreservationapplicationjetpack.signIn

data class SignInResult(
    val data: UserData?, // what contains the user data, the profile picture, id...
    val errorMessage: String? // error message in case anything goes wrong, which is a nullable string
)


data class UserData(
    val userId: String, //unique identifier that comes from firebase
    val username: String?, //
    val profilePictureUrl: String? //nullable string since not every user can have a profile picture
)