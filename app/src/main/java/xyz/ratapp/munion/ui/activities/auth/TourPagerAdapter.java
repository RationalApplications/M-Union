package xyz.ratapp.munion.ui.activities.auth;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import xyz.ratapp.munion.ui.fragments.tour.ChatTourFragment;
import xyz.ratapp.munion.ui.fragments.tour.CardTourFragment;
import xyz.ratapp.munion.ui.fragments.tour.FriendTourFragment;
import xyz.ratapp.munion.ui.fragments.tour.MunionTourFragment;
import xyz.ratapp.munion.ui.fragments.tour.SubmitTourFragment;

/**
 * Created by timtim on 07/01/2018.
 */

public class TourPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments;

    public TourPagerAdapter(FragmentManager fm) {
        super(fm);

        fragments = new ArrayList<>(5);
        fragments.add(new MunionTourFragment());
        fragments.add(new ChatTourFragment());
        fragments.add(new CardTourFragment());
        fragments.add(new FriendTourFragment());
        fragments.add(new SubmitTourFragment());
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
