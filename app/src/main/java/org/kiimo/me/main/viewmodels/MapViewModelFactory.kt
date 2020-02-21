package org.kiimo.me.main.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.kiimo.me.main.repositories.MapRepository

/**
 * Factory for creating a [MapViewModel] with a constructor that takes a [MapRepository].
 */
class MapViewModelFactory(
    private val mapRepository: MapRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) = MapViewModel(mapRepository) as T
}