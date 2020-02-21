package org.kiimo.me.main.repositories

class ProfileRepository {

    companion object {

        // For Singleton instantiation
        @Volatile
        private var instance: ProfileRepository? = null

        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: ProfileRepository().also { instance = it }
            }
    }
}