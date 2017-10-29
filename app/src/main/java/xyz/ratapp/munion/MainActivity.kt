package xyz.ratapp.munion

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navigation.titleState = AHBottomNavigation.TitleState.ALWAYS_HIDE
        initNoAuth()
    }

    fun changeFragment(fragment: Fragment, title : String) {
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.main_container, fragment)
        ft.commit()
        supportActionBar?.title = title
    }

    fun initNoAuth() {
        navigation.removeAllItems()
        changeFragment(VkFragment(), getString(R.string.title_news_feed))

        val listenerNoAuth = AHBottomNavigation.OnTabSelectedListener { position, wasSelected ->
            when (position) {
                0 -> {
                    changeFragment(VkFragment(), getString(R.string.title_news_feed))
                    return@OnTabSelectedListener true
                }

                1 -> {
                    changeFragment(AuthFragment(), getString(R.string.title_account))
                    return@OnTabSelectedListener true
                }

                2 -> {
                    changeFragment(HypothecFragment(), getString(R.string.title_credit))
                    return@OnTabSelectedListener true
                }

                3 -> {
                    changeFragment(ContactsFragment(), getString(R.string.title_contacts))
                    return@OnTabSelectedListener true
                }
            }
            false
        }

        val itemNews = AHBottomNavigationItem(getString(R.string.title_news_feed), R.drawable.ic_news)
        val itemAuth = AHBottomNavigationItem(getString(R.string.title_account), R.drawable.ic_account_circle)
        val itemHypothec = AHBottomNavigationItem(getString(R.string.title_credit), R.drawable.ic_hypothec)
        val itemContact = AHBottomNavigationItem(getString(R.string.title_contacts), R.drawable.ic_contacts)


        navigation.addItem(itemNews)
        navigation.addItem(itemAuth)
        navigation.addItem(itemHypothec)
        navigation.addItem(itemContact)


        navigation.setOnTabSelectedListener(listenerNoAuth)
    }

    fun initAuth() {
        navigation.removeAllItems()
        changeFragment(VkFragment(), getString(R.string.title_news_feed))

        val listenerAuth = AHBottomNavigation.OnTabSelectedListener { position, wasSelected ->
            when (position) {
                0 -> {
                    changeFragment(VkFragment(), getString(R.string.title_news_feed))
                    return@OnTabSelectedListener true
                }

                1 -> {
                    changeFragment(ChatFragment(), getString(R.string.title_chat))
                    return@OnTabSelectedListener true
                }

                2 -> {
                    changeFragment(HypothecFragment(), getString(R.string.title_credit))
                    return@OnTabSelectedListener true
                }

                3 -> {
                    changeFragment(ContactsFragment(), getString(R.string.title_contacts))
                    return@OnTabSelectedListener true
                }

                4 -> {
                    changeFragment(ContactsFragment(), getString(R.string.title_contacts))
                    return@OnTabSelectedListener true
                }
            }
            false
        }

        val itemNews = AHBottomNavigationItem(getString(R.string.title_news_feed), R.drawable.ic_news)
        val itemChat = AHBottomNavigationItem(getString(R.string.title_chat), R.drawable.ic_chat)
        val itemHypothec = AHBottomNavigationItem(getString(R.string.title_credit), R.drawable.ic_hypothec)
        val itemContact = AHBottomNavigationItem(getString(R.string.title_contacts), R.drawable.ic_contacts)
        val itemStatistic = AHBottomNavigationItem(getString(R.string.title_statistics), R.drawable.ic_statistic)


        navigation.addItem(itemNews)
        navigation.addItem(itemChat)
        navigation.addItem(itemHypothec)
        navigation.addItem(itemContact)
        navigation.addItem(itemStatistic)


        navigation.setOnTabSelectedListener(listenerAuth)
    }
}

