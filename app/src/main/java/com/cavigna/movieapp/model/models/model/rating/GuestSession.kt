package com.cavigna.movieapp.model.models.model.rating


import com.google.gson.annotations.SerializedName

data class GuestSession(
    var success: Boolean = false,
    @SerializedName("guest_session_id")
    var guestSessionId: String = "",
    @SerializedName("expires_at")
    var expiresAt: String = ""
)