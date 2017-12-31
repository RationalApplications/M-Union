/*
 * Created by Itzik Braun on 12/3/2015.
 * Copyright (c) 2015 deluge. All rights reserved.
 *
 * Last Modification at: 3/12/15 4:27 PM
 */

package xyz.ratapp.munionagent.ui.activities.main;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import co.chatsdk.core.Tab;
import co.chatsdk.ui.manager.InterfaceAdapter;
import co.chatsdk.ui.manager.InterfaceManager;
import xyz.ratapp.munionagent.ui.fragments.PrivateThreadsFragment;

/**
 * Created by itzik on 6/16/2014.
 */
public class PagerAdapterTabs extends FragmentStatePagerAdapter {

    protected List<Tab> tabs;

    public PagerAdapterTabs(FragmentManager fm) {
        super(fm);
        Tab chats = new Tab(co.chatsdk.ui.R.string.conversations,
                co.chatsdk.ui.R.drawable.ic_action_private,
                PrivateThreadsFragment.newInstance());

        tabs = new ArrayList<>();
        tabs.add(chats);
    }

    public List<Tab> getTabs() {
        return tabs;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabs.get(position).title;
    }

    @Override
    public int getCount() {
        return tabs.size();
    }

    @Override
    public Fragment getItem(int position) {
        return tabs.get(position).fragment;
    }

}
