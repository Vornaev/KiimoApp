package org.kiimo.me.models

data class FirebasePayload(val delivery: String, val type: String, val createdAt: String)

data class SenderFirebasePayload(val delivery: String, val type: String, val userRating: String)