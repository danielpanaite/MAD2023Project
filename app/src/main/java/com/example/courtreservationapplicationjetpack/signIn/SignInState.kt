package com.example.courtreservationapplicationjetpack.signIn

data class SignInState(
    val isSignInSuccessful: Boolean = false,
    val signInError: String? = null
)
