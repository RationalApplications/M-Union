package xyz.ratapp.munion.ui.activities

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem
import kotlinx.android.synthetic.main.activity_main.*
import xyz.ratapp.munion.ui.fragments.common.FragmentBase
import xyz.ratapp.munion.ui.fragments.hypothec.HypothecRootFragment
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.net.ConnectivityManager
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.BitmapImageViewTarget
import xyz.ratapp.munion.R
import xyz.ratapp.munion.controllers.interfaces.DataCallback
import xyz.ratapp.munion.data.DataController
import xyz.ratapp.munion.data.pojo.Lead
import xyz.ratapp.munion.helpers.PreferencesHelper
import xyz.ratapp.munion.ui.activities.auth.AuthActivity
import xyz.ratapp.munion.ui.fragments.*


class MainActivity : AppCompatActivity() {
    companion object {
        private val PERMISSIONS_REQUEST = 91

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navigation.titleState = AHBottomNavigation.TitleState.ALWAYS_HIDE
        navigation.defaultBackgroundColor = this.resources.getColor(R.color.colorPrimaryDark)
        navigation.accentColor = this.resources.getColor(R.color.colorAccent)
        navigation.inactiveColor = this.resources.getColor(R.color.white)
        navigation.isBehaviorTranslationEnabled = false

        init()
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.title_news_feed)

        val permissions = ArrayList<String>()

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.CAMERA)
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }

        if (permissions.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, permissions.toTypedArray(), PERMISSIONS_REQUEST)
        }

    }

    override fun onStart() {
        super.onStart()
        val preferencesHelper = PreferencesHelper.getInstance(this)
        if(preferencesHelper.isAuthed) {
            setUserIcon()
        }
    }

    fun init() {
        val preferencesHelper = PreferencesHelper.getInstance(this)
        if(preferencesHelper.isAuthed) {
            val chatId = preferencesHelper.chatThreadEntityId
            initAuth(chatId)
        }
        else {
            initNoAuth()
        }
    }

    fun changeFragment(fragment: FragmentBase) {
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.main_container, fragment)
        ft.commit()
        supportActionBar?.title = fragment.getFragmentName(this)
    }

    private fun initNoAuth() {

        navigation.removeAllItems()
        changeFragment(VkFragment())

        val listenerNoAuth = AHBottomNavigation.OnTabSelectedListener { position, wasSelected ->
            when (position) {
                0 -> {
                    changeFragment(VkFragment())
                    return@OnTabSelectedListener true
                }

                1 -> {
                    changeFragment(AuthFragment())
                    return@OnTabSelectedListener true
                }

                2 -> {
                    changeFragment(HypothecRootFragment())
                    return@OnTabSelectedListener true
                }

                3 -> {
                    changeFragment(ContactsFragment())
                    return@OnTabSelectedListener true
                }
            }
            false
        }

        val itemNews = AHBottomNavigationItem(getString(R.string.title_news_feed), R.drawable.ic_news)
        val itemAuth = AHBottomNavigationItem(getString(R.string.title_account), R.drawable.ic_account_circle_white)
        val itemHypothec = AHBottomNavigationItem(getString(R.string.title_credit), R.drawable.ic_hypothec)
        val itemContact = AHBottomNavigationItem(getString(R.string.title_contacts), R.drawable.ic_contacts_white)

        navigation.addItem(itemNews)
        navigation.addItem(itemAuth)
        navigation.addItem(itemHypothec)
        navigation.addItem(itemContact)

        navigation.setOnTabSelectedListener(listenerNoAuth)
    }

    private fun initAuth(chatId: String) {
        setUserIcon()

        navigation.removeAllItems()
        changeFragment(VkFragment())

        val listenerAuth = AHBottomNavigation.OnTabSelectedListener { position, wasSelected ->
            when (position) {
                0 -> {
                    changeFragment(VkFragment())
                    return@OnTabSelectedListener true
                }

                1 -> {
                    val fragment = ChatFragment()
                    fragment.setupThread(chatId)
                    changeFragment(fragment)
                    return@OnTabSelectedListener true
                }

                2 -> {
                    changeFragment(StatisticsFragment())
                    return@OnTabSelectedListener true
                }

                3 -> {
                    changeFragment(HypothecRootFragment())
                    return@OnTabSelectedListener true
                }

                4 -> {
                    changeFragment(ContactsFragment())
                    return@OnTabSelectedListener true
                }
            }
            false
        }

        val itemNews = AHBottomNavigationItem(getString(R.string.title_news_feed), R.drawable.ic_news)
        val itemChat = AHBottomNavigationItem(getString(R.string.title_chat), R.drawable.ic_chat)
        val itemStatistic = AHBottomNavigationItem(getString(R.string.title_statistics), R.drawable.ic_statistic)
        val itemHypothec = AHBottomNavigationItem(getString(R.string.title_credit), R.drawable.ic_hypothec)
        val itemContact = AHBottomNavigationItem(getString(R.string.title_contacts), R.drawable.ic_contacts_white)


        navigation.addItem(itemNews)
        navigation.addItem(itemChat)
        navigation.addItem(itemStatistic)
        navigation.addItem(itemHypothec)
        navigation.addItem(itemContact)



        navigation.setCurrentItem(0, false)

        navigation.setOnTabSelectedListener(listenerAuth)
    }

    private fun setUserIcon() {
        DataController.getInstance(this).
                getUser(object: DataCallback<Lead> {
                    override fun onSuccess(user: Lead) {
                        Glide.with(this@MainActivity).
                                load(if (user.photoUri != null)
                                    user.photoUri
                                else
                                    "android.resource://xyz.ratapp.munion/drawable/icon_me").
                                asBitmap().centerCrop().
                                error(R.drawable.icon_me).
                                into(object : BitmapImageViewTarget(iv_bar) {
                                    override fun setResource(resource: Bitmap) {
                                        val circularBitmapDrawable =
                                                RoundedBitmapDrawableFactory.create(resources, resource)
                                        circularBitmapDrawable.isCircular = true
                                        iv_bar.setImageDrawable(circularBitmapDrawable)
                                    }
                                })
                        iv_bar.setOnClickListener({
                            val i = Intent(this@MainActivity,
                                    CabinetActivity::class.java)
                            startActivity(i)
                        })
                    }

                    override fun onFailed(thr: Throwable?) {

                    }
                })
    }

    fun getNavigation(): AHBottomNavigation {
        return navigation
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK &&
                requestCode == AuthActivity.REQUEST_AUTH_CODE &&
                data!!.extras.containsKey(AuthActivity.RESPONSE_EXTRA_CHAT_ENTITY_ID)) {
            val chatId = data.extras.getString(AuthActivity.RESPONSE_EXTRA_CHAT_ENTITY_ID, "")
            initAuth(chatId)
        }
    }

    //todo error fragment if not granted
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSIONS_REQUEST -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return
            }
        }
    }

    fun setNoConnection(){
        changeFragment(ErrorFragment.getInstance("No internet connection", "reload"))
        navigation.visibility = View.INVISIBLE
    }

    fun setHaveConnection(){
        changeFragment(VkFragment())
        navigation.visibility = View.VISIBLE
    }

    private fun isOnline(): Boolean {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.activeNetworkInfo
        return netInfo != null && netInfo.isConnectedOrConnecting
    }
}

