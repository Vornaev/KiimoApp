package org.kiimo.me.register

import android.os.Bundle
import org.kiimo.me.app.BaseActivity
import org.kiimo.me.app.di.BaseViewFeatureModule
import org.kiimo.me.register.di.DaggerRegisterAccountUserComponent
import org.kiimo.me.register.di.RegistrationServiceActivityModule
import org.kiimo.me.register.service.RegisterAccountViewModel
import javax.inject.Inject


abstract class BaseRegistrationServicesActivity : BaseActivity() {

    @Inject
    lateinit var viewModel: RegisterAccountViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initDependencyContainer()
    }

    private fun initDependencyContainer() {
        val component = DaggerRegisterAccountUserComponent.builder()
            .appComponent(getAppComponent())
            .registrationServiceActivityModule(RegistrationServiceActivityModule(this))
            .baseViewFeatureModule(BaseViewFeatureModule(this))
            .build()

        component.injectRegistrationService(this)
    }
}