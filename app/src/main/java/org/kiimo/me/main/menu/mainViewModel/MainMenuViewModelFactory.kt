package org.kiimo.me.main.menu.mainViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@Suppress("UNCHECKED_CAST")
class MainMenuViewModelFactory(var repository: MainViewModelRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainMenuViewModel(repository) as T
    }
}