package xyz.ratapp.munion

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem
import kotlinx.android.synthetic.main.activity_main.*
import xyz.ratapp.munion.views.*
import xyz.ratapp.munion.views.common.FragmentBase
import xyz.ratapp.munion.views.hypothec.HypothecRootFragment
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import android.net.NetworkInfo
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager




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

        initNoAuth()
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

    fun changeFragment(fragment: FragmentBase) {
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.main_container, fragment)
        ft.commit()
        supportActionBar?.title = fragment.getFragmentName(this)
    }

    fun initNoAuth() {

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

    fun initAuth() {

        iv_bar.setImageDrawable(resources.getDrawable(R.drawable.icon_me))
        iv_bar.setOnClickListener(View.OnClickListener {
            val i = Intent(this, CabinetActivity::class.java)
            startActivity(i)
        })

        navigation.removeAllItems()
        changeFragment(VkFragment())

        val listenerAuth = AHBottomNavigation.OnTabSelectedListener { position, wasSelected ->
            when (position) {
                0 -> {
                    changeFragment(VkFragment())
                    return@OnTabSelectedListener true
                }

                1 -> {
                    changeFragment(ChatFragment())
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
        navigation.addItem(itemHypothec)
        navigation.addItem(itemContact)
        navigation.addItem(itemStatistic)



        navigation.setCurrentItem(0, false)

        navigation.setOnTabSelectedListener(listenerAuth)
    }

    fun getNavigation(): AHBottomNavigation {
        return navigation
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == -1) {
            initAuth()
        } else {
            Toast.makeText(applicationContext, "Can't authorize", Toast.LENGTH_LONG).show()
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

