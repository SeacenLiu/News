package com.example.seacen.news.News;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class NewsContentFragmentAdapter extends FragmentPagerAdapter {
    private List<NewsClassifyModel> classifyModels;

    public NewsContentFragmentAdapter(FragmentManager fm) {
        super(fm);
        this.classifyModels = new ArrayList<>();
    }

    /**
     * 数据列表
     *
     * @param datas
     */
    public void setList(List<NewsClassifyModel> datas) {
        this.classifyModels.clear();
        this.classifyModels.addAll(datas);
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        NewsContentFragment fragment = new NewsContentFragment();
        NewsClassifyModel model = classifyModels.get(position);
        Bundle bundle = new Bundle();
        bundle.putString("name", model.name);
        bundle.putInt("classifyid", model.id);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return classifyModels.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String plateName = classifyModels.get(position).name;
        if (plateName == null) {
            plateName = "";
        }
//        else if (plateName.length() > 0) {
//            plateName = plateName.substring(0, classifyModels.size()) + "...";
//        }
        return plateName;
    }
}
