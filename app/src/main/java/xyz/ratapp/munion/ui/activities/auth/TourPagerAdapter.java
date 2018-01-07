package xyz.ratapp.munion.ui.activities.auth;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import xyz.ratapp.munion.ui.fragments.tour.AppFragment;
import xyz.ratapp.munion.ui.fragments.tour.CardFragment;
import xyz.ratapp.munion.ui.fragments.tour.FriendFragment;
import xyz.ratapp.munion.ui.fragments.tour.MunionFragment;
import xyz.ratapp.munion.ui.fragments.tour.SubmitFragment;

/**
 * Created by timtim on 07/01/2018.
 */

public class TourPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments;

    public TourPagerAdapter(FragmentManager fm) {
        super(fm);

        fragments = new ArrayList<>(5);
        fragments.add(new MunionFragment());
        fragments.add(new AppFragment());
        fragments.add(new CardFragment());
        fragments.add(new FriendFragment());
        fragments.add(new SubmitFragment());
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
