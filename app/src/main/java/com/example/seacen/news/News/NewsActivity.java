package com.example.seacen.news.News;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.seacen.news.R;
import com.example.seacen.news.Utils.Network.SCNetworkHandler;
import com.example.seacen.news.Utils.Network.SCNetworkMethod;
import com.example.seacen.news.Utils.Network.SCNetworkPort;
import com.example.seacen.news.Utils.Network.SCNetworkTool;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    static String TAG = "NewsActivity";

    ListView listView;
    List<NewsModel> models = new ArrayList<>();
    NewsAdapter adapter = new NewsAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_activity);
        listView = findViewById(R.id.news_lv);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        testLoadNews();
    }

    private void testLoadNews() {
        SCNetworkTool.shared().normalEequest(SCNetworkPort.IndexNews, SCNetworkMethod.GET, null, new SCNetworkHandler() {
            @Override
            public void successHandle(String bodyStr) {
                Log.i(TAG, bodyStr);
                JSONObject response = JSONObject.parseObject(bodyStr);
                int code = (int)response.get("status");
                String info = response.get("msg").toString();
                JSONArray array = (JSONArray) response.get("data");
                List<NewsModel> newss = array.toJavaList(NewsModel.class);
                models = newss;
                adapter.notifyDataSetChanged();
            }

            @Override
            public void errorHandle(Exception error) {
                Log.i(TAG, error.toString());
                Toast toast = Toast.makeText(NewsActivity.this, error.toString(), Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    /**
     * 点击事件 相当于 UITableView 中的 didSelect
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
        Log.i("indexPath.row", String.valueOf(position));
        // FIXME: - 多次点击多次执行
        // TODO: - 进入新闻详情
        // 测试进入详细
        Intent intent = new Intent(NewsActivity.this, NewsDetailActivity.class);
        startActivity(intent);
    }

    /**
     * ListView 的 Adapter 相当于 UITableViewDataSource
     */
    private class NewsAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return models.size();
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
            // config view
            NewsCell cell = (NewsCell)convertView;
            NewsModel model = models.get(position);
            cell.titleTv.setText(model.getTitle());
            cell.detailTv.setText(model.getIntro());

            return cell;
        }
    }
}

