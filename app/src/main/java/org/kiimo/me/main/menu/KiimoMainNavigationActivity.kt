package org.kiimo.me.main.menu

import android.content.Intent
import android.os.Bundle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_kiimo_main_navigation.*
import kotlinx.android.synthetic.main.activity_kiimo_main_navigation.view.*
import kotlinx.android.synthetic.main.app_bar_kiimo_main_navigation.*
import kotlinx.android.synthetic.main.layout_menu_item.view.*
import kotlinx.android.synthetic.main.nav_header_kiimo_main_navigation.*
import kotlinx.android.synthetic.main.nav_header_kiimo_main_navigation.view.*
import org.kiimo.me.R
import org.kiimo.me.app.BaseActivity
import org.kiimo.me.app.di.BaseViewFeatureModule
import org.kiimo.me.main.FragmentTags
import org.kiimo.me.main.account.ChangeAccountTypeDialog
import org.kiimo.me.main.fragments.MenuMyDeliveriesFragment
import org.kiimo.me.main.fragments.MenuPaymentTypeFragment
import org.kiimo.me.main.fragments.ProfileFragment
import org.kiimo.me.main.menu.di.DaggerMainMenuComponent
import org.kiimo.me.main.menu.di.MainManiActivityModule
import org.kiimo.me.main.menu.mainViewModel.MainMenuViewModel
import org.kiimo.me.main.sender.fragment.SenderMapFragment
import org.kiimo.me.models.payment.PreferredPaymentUser
import org.kiimo.me.register.WelcomeActivity
import org.kiimo.me.services.MessageEvent
import org.kiimo.me.util.NetworkResponseStatus
import org.kiimo.me.util.PreferenceUtils
import org.kiimo.me.util.RxBus
import javax.inject.Inject

open class KiimoMainNavigationActivity : BaseActivity() {

    @Inject
    lateinit var viewModel: MainMenuViewModel

    val composositeContainer = CompositeDisposable()

    override fun getLayoutId(): Int {
        return R.layout.activity_kiimo_main_navigation
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setMenuItems()
        injectComponent()
        observeLiveData()

        viewModel.getUser()
        viewModel.putDeviceToken()
        viewModel.savePreferredPaymentType(PreferredPaymentUser())

    }

    open fun handlePayload(newIntent: Intent) {}


    protected fun putDeliveryType() {
        viewModel.putDeliveryType()
    }

    fun observeLiveData() {
        viewModel.userProfileLiveData.observe(
            this, Observer {
                if (it.status == NetworkResponseStatus.SUCCESS) {
                    PreferenceUtils.saveUserProfile(this, it)

                    val username = "${it.firstName}  ${it.lastName}"
                    nav_view?.getHeaderView(0)?.userNameHeader?.text = username

                    if (it.photo.isNotEmpty()) {

                        Glide.with(this).load(it.photo).override(300, 0)
                            .into(nav_view?.getHeaderView(0)?.imageView!!)

                        Glide.with(this).load(it.photo).override(300, 0).centerCrop()
                            .into(imageViewProfileDrawer)

                    }
                }
            }
        )

        viewModel.userProfileFieldsUpdateLiveData.observe(this,
            Observer {
                viewModel.getUser()
            })
    }


    protected fun injectComponent() {
        val component = DaggerMainMenuComponent.builder().appComponent(getAppComponent())
            .mainManiActivityModule(MainManiActivityModule(this))
            .baseViewFeatureModule(BaseViewFeatureModule(this))
            .build()

        component.inject(this)
    }

    protected open fun setMenuItems() {

        setupMenuProfileFragment()
        setupDeliveriesFragment()
        setupPaymentMenuFragment()
        setupHelpAndFaqMenuFragment()

        imageViewProfileDrawer.setOnClickListener {
            drawer_layout.openDrawer(GravityCompat.START)
        }
    }

    protected open fun setupHelpAndFaqMenuFragment() {
        menuItemHelp.textViewMenuItem.text = getString(R.string.help_menu_item)
        menuItemHelp.imgMenuItemImage.setImageDrawable(getDrawable(R.drawable.ic_menu_help))

        menuItemFaq.textViewMenuItem.text = getString(R.string.faqs_menu_item)
        menuItemFaq.imgMenuItemImage.setImageDrawable(getDrawable(R.drawable.ic_menu_questions))

    }

    fun closeMenuDrawer() {
        drawer_layout.closeDrawer(GravityCompat.START)
    }

    protected open fun setupPaymentMenuFragment() {

        menuItemMyPayment.textViewMenuItem.text = getString(R.string.payment_menu_item)
        menuItemMyPayment.imgMenuItemImage.setImageDrawable(getDrawable(R.drawable.ic_menu_payment))
        menuItemMyPayment.setOnClickListener {
            openMenuFragment(
                MenuPaymentTypeFragment::class.java,
                FragmentTags.MENU_PAYMENT_TYPE
            )
            closeMenuDrawer()
        }

    }

    protected open fun setupDeliveriesFragment() {
        menuItemMyDelivery.textViewMenuItem.text = getString(R.string.my_delivers_menu_item)
        menuItemMyDelivery.imgMenuItemImage.setImageDrawable(getDrawable(R.drawable.ic_menu_my_deliveries))

        menuItemMyDelivery.setOnClickListener {
            openMenuFragment(
                MenuMyDeliveriesFragment::class.java,
                FragmentTags.MENU_PAYMENT_TYPE
            )
            closeMenuDrawer()
        }
    }

    protected open fun setupMenuProfileFragment() {
        menuItemMyAccount.textViewMenuItem.text = getString(R.string.my_account_menu_item)
        menuItemMyAccount.imgMenuItemImage.setImageDrawable(getDrawable(R.drawable.ic_menu_my_account))
        menuItemMyAccount.setOnClickListener {
            openMenuFragment(ProfileFragment::class.java, FragmentTags.MENU_PROFILE)
            drawer_layout.closeDrawer(GravityCompat.START)
        }
    }

    fun onOpenDrawerClicked() {
        drawer_layout.openDrawer(GravityCompat.START)
    }

    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {

            if (supportFragmentManager.backStackEntryCount == 1) {

                val lastFrag = supportFragmentManager.fragments.last()
//                if(lastFrag is SenderMapFragment){
//                    lastFrag.shouldResetFragment()
//                }
                moveTaskToBack(true)
            } else {
                super.onBackPressed()
            }
        }
    }


    fun logout() {
        PreferenceUtils.logout(this)
        val intent = Intent(this, WelcomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onDestroy() {
        composositeContainer.dispose()
        super.onDestroy()
    }
}
