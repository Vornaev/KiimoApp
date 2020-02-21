package org.kiimo.me.main.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.kiimo.me.main.repositories.ProfileRepository

/**
 * Factory for creating a [ProfileViewModel] with a constructor that takes a [ProfileRepository].
 */
class ProfileViewModelFactory(
    private val repository: ProfileRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        ProfileViewModel(repository) as T
}