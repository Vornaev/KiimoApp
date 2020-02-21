package org.kiimo.me.main.modules

import dagger.Module
import dagger.Provides
import org.kiimo.me.main.repositories.ProfileRepository
import org.kiimo.me.main.viewmodels.ProfileViewModelFactory
import javax.inject.Singleton

@Module
class ProfileModule {

    @Provides
    @Singleton
    fun providesProfileViewModel(
        repository: ProfileRepository
    ): ProfileViewModelFactory = ProfileViewModelFactory(repository)


    @Provides
    @Singleton
    fun providesProfileRepositories(): ProfileRepository = ProfileRepository.getInstance()
}