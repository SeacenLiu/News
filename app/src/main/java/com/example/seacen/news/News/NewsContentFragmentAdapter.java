package com.example.seacen.news.News;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class NewsContentFragmentAdapter extends FragmentPagerAdapter {
    private List<String> names;

    public NewsContentFragmentAdapter(FragmentManager fm) {
        super(fm);
        this.names = new ArrayList<>();
    }

    /**
     * 数据列表
     *
     * @param datas
     */
    public void setList(List<String> datas) {
        this.names.clear();
        this.names.addAll(datas);
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        NewsContentFragment fragment = new NewsContentFragment();
        Bundle bundle = new Bundle();
        bundle.putString("name", names.get(position));
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return names.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String plateName = names.get(position);
        if (plateName == null) {
            plateName = "";
        } else if (plateName.length() > names.size()) {
            plateName = plateName.substring(0, names.size()) + "...";
        }
        return plateName;
    }
}
