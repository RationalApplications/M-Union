package xyz.ratapp.munion

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem
import kotlinx.android.synthetic.main.activity_main.*
import xyz.ratapp.munion.views.*
import xyz.ratapp.munion.views.common.FragmentBase
import xyz.ratapp.munion.views.hypothec.HypothecFragment
import xyz.ratapp.munion.views.hypothec.HypothecRootFragment


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navigation.titleState = AHBottomNavigation.TitleState.ALWAYS_HIDE
        navigation.defaultBackgroundColor = this.resources.getColor(R.color.colorPrimaryDark)
        navigation.accentColor = this.resources.getColor(R.color.colorAccent)
        navigation.inactiveColor = this.resources.getColor(R.color.white)
        initNoAuth()
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
                    changeFragment(HypothecRootFragment())
                    return@OnTabSelectedListener true
                }

                3 -> {
                    changeFragment(ContactsFragment())
                    return@OnTabSelectedListener true
                }

                4 -> {
                    changeFragment(StatisticsFragment())
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

        navigation.setCurrentItem(0, false)


        navigation.setOnTabSelectedListener(listenerAuth)
    }
}

