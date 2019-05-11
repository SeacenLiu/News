package com.example.seacen.news.News;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.example.seacen.news.R;

public class NewsActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_activity);
        listView = findViewById(R.id.news_lv);

        listView.setAdapter(new NewsAdapter());
        listView.setOnItemClickListener(this);
    }

    /**
     * 点击事件 相当于 UITableView 中的 didSelect
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.i("indexPath.row", String.valueOf(position));
        // TODO: - 进入新闻详情

    }

    /**
     * ListView 的 Adapter 相当于 UITableViewDataSource
     */
    private class NewsAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return 10;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = new NewsCell(NewsActivity.this);
            }
            return convertView;
        }
    }
}

