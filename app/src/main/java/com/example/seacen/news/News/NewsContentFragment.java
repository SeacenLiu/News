package com.example.seacen.news.News;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.example.seacen.news.R;
import com.example.seacen.news.Utils.Network.SCNetworkHandler;
import com.example.seacen.news.Utils.Network.SCNetworkMethod;
import com.example.seacen.news.Utils.Network.SCNetworkPort;
import com.example.seacen.news.Utils.Network.SCNetworkTool;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

public class NewsContentFragment extends Fragment implements AdapterView.OnItemClickListener {
    static String TAG = "NewsContentFragment";

    private String name;
    ListView listView;
    RefreshLayout refreshLayout;
    List<NewsModel> models = new ArrayList<>();
    NewsContentFragment.NewsAdapter adapter = new NewsContentFragment.NewsAdapter();
    private int index = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        name = bundle.getString("name");
        if (name == null) {
            name = "参数非法";
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.news_content_fragment, container, false);

        // 初始化
        setup(view);

        return view;
    }

    private void setup(View view) {
        listView = view.findViewById(R.id.news_lv);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        refreshLayout = (RefreshLayout)view.findViewById(R.id.news_refreshLayout);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                index = 0;
                SCNetworkTool.shared().loadClassifyNews(name, new SCNetworkHandler() {
                    @Override
                    public void successHandle(String bodyStr) {
                        Log.i(TAG, bodyStr);
                        // FIXME: - 不安全！！！
                        JSONObject response = JSONObject.parseObject(bodyStr);
                        int code = (int)response.get("status");
                        String info = response.get("msg").toString();
                        JSONArray array = (JSONArray) response.get("data");
                        List<NewsModel> newss = array.toJavaList(NewsModel.class);
                        models = newss;
                        adapter.notifyDataSetChanged();
                        refreshlayout.finishRefresh(0);
                    }

                    @Override
                    public void errorHandle(Exception error) {
                        Log.i(TAG, error.toString());
                        Toast toast = Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT);
                        toast.show();
                        refreshlayout.finishRefresh(false);//传入false表示刷新失败
                    }
                });
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                // TODO: - 加载下一页
                refreshlayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败
            }
        });
    }

    // 这个是加载首页的！！！
    private void refreshNews() {
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
                Toast toast = Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT);
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
        Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
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
                convertView = new NewsCell(getActivity());
            }
            // config view
            NewsCell cell = (NewsCell)convertView;
            NewsModel model = models.get(position);
            cell.titleTv.setText(model.getTitle());
            cell.detailTv.setText(model.getIntro());
            Glide.with(cell).load(model.img).placeholder(R.drawable.img_placeholder).into(cell.coverImg);
            cell.sourceTv.setText(model.getSource());
            cell.readTv.setText("阅读量："+model.getReadnum());
            cell.commentTv.setText("阅读量："+model.getCommentnum());
            // FIXME: - 时间需要做处理
//            cell.timeTv.setText(model.getTime());
            return cell;
        }
    }

}
