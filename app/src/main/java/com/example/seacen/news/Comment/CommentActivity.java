package com.example.seacen.news.Comment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.transition.Transition;
import android.transition.TransitionSet;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.example.seacen.news.Account.UserModel;
import com.example.seacen.news.Comment.transition.ChangeColor;
import com.example.seacen.news.Comment.transition.ChangePosition;
import com.example.seacen.news.Comment.transition.CommentEnterTransition;
import com.example.seacen.news.Comment.transition.ShareElemEnterRevealTransition;
import com.example.seacen.news.Comment.transition.ShareElemReturnChangePosition;
import com.example.seacen.news.Comment.transition.ShareElemReturnRevealTransition;
import com.example.seacen.news.News.NewsModel;
import com.example.seacen.news.R;
import com.example.seacen.news.Utils.Network.SCNetworkHandler;
import com.example.seacen.news.Utils.Network.SCNetworkMethod;
import com.example.seacen.news.Utils.Network.SCNetworkPort;
import com.example.seacen.news.Utils.Network.SCNetworkTool;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CommentActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private static final String TAG = "CommentActivity";

    @BindView(R.id.comment_activity_bottom_send_bar)
    View bottomSendBar;
    @BindView(R.id.comment_activity_content)
    View commentContent;
    @BindView(R.id.comment_activity_body_lv)
    ListView listView;
    @BindView(R.id.comment_refreshLayout)
    RefreshLayout refreshLayout;

    List<CommentModel> models = new ArrayList<>();
    CommentAdapter adapter = new CommentAdapter();

    private Integer newsid;
    private Integer page = 1;
    private Integer size = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_activity);
        ButterKnife.bind(this);

        newsid = getIntent().getIntExtra("newsid", -1);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        setTransition();
        setupNavigation();
        setupRefresh();
    }

    private void setupRefresh() {
        // 下拉刷新
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                Map<String, Object> params = new HashMap<>();
                params.put("newsid", newsid);
                params.put("page", page);
                params.put("size", size);
                SCNetworkTool.shared().normalRequest(SCNetworkPort.Comment, SCNetworkMethod.GET, params, new SCNetworkHandler() {
                    @Override
                    public void successHandle(JSONObject jsonObject) {
                        int code = (int)jsonObject.get("status");
                        if (code == 200) {
                            String info = jsonObject.getString("msg");
                            JSONObject data = (JSONObject) jsonObject.get("data");
                            assert data != null;
                            JSONArray array = data.getJSONArray("content");
                            models = array.toJavaList(CommentModel.class);
                            adapter.notifyDataSetChanged();
                            refreshLayout.finishRefresh(0);
                        } else {
                            Toast toast = Toast.makeText(CommentActivity.this, "服务器错误", Toast.LENGTH_SHORT);
                            toast.show();
                            refreshLayout.finishRefresh(false);
                        }
                    }

                    @Override
                    public void errorHandle(Exception error) {
                        Log.i(TAG, error.toString());
                        Toast toast = Toast.makeText(CommentActivity.this, error.toString(), Toast.LENGTH_SHORT);
                        toast.show();
                        refreshLayout.finishRefresh(false);//传入false表示刷新失败
                    }
                });
            }
        });
        // 上拉加载
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page += 1;
                Map<String, Object> params = new HashMap<>();
                params.put("newsid", newsid);
                params.put("page", page);
                params.put("size", size);
                SCNetworkTool.shared().normalRequest(SCNetworkPort.Comment, SCNetworkMethod.GET, params, new SCNetworkHandler() {
                    @Override
                    public void successHandle(JSONObject jsonObject) {
                        int code = (int)jsonObject.get("status");
                        if (code == 200) {
                            String info = jsonObject.getString("msg");
                            JSONObject data = (JSONObject) jsonObject.get("data");
                            assert data != null;
                            JSONArray array = data.getJSONArray("content");
                            List<CommentModel> news = array.toJavaList(CommentModel.class);
                            if (news.isEmpty()) {
                                refreshLayout.finishLoadMoreWithNoMoreData();
                            } else {
                                for (CommentModel model: news) {
                                    models.add(model);
                                }
                                adapter.notifyDataSetChanged();
                                refreshLayout.finishLoadMore();
                            }
                        } else {
                            Toast toast = Toast.makeText(CommentActivity.this, "服务器错误", Toast.LENGTH_SHORT);
                            toast.show();
                            refreshLayout.finishRefresh(false);
                        }
                    }

                    @Override
                    public void errorHandle(Exception error) {
                        Log.i(TAG, error.toString());
                        Toast toast = Toast.makeText(CommentActivity.this, error.toString(), Toast.LENGTH_SHORT);
                        toast.show();
                        refreshLayout.finishRefresh(false);//传入false表示刷新失败
                    }
                });
            }
        });
    }

    public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
        Log.i("indexPath.row", String.valueOf(position));
    }

    private void setupNavigation() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("评论区");
        actionBar.setDisplayHomeAsUpEnabled(true);
//        actionBar.hide();
    }

    private void setTransition() {
        // 底部输入框的进入动画
        getWindow().setEnterTransition(new CommentEnterTransition(this, bottomSendBar));
        // 入场动画
        getWindow().setSharedElementEnterTransition(buildShareElemEnterSet());
        // 退场动画
        getWindow().setSharedElementReturnTransition(buildShareElemReturnSet());
    }

    /**
     * 进入动画
     * @return
     */
    private TransitionSet buildShareElemEnterSet() {
        TransitionSet transitionSet = new TransitionSet();

        Transition revealTransition = new ShareElemEnterRevealTransition(commentContent);
        transitionSet.addTransition(revealTransition);
        revealTransition.addTarget(R.id.comment_activity_content);
        revealTransition.setInterpolator(new FastOutSlowInInterpolator());
        revealTransition.setDuration(300);

        Transition changePos = new ChangePosition();
        changePos.setDuration(300);
        changePos.addTarget(R.id.comment_activity_content);
        transitionSet.addTransition(changePos);

        ChangeColor changeColor = new ChangeColor(getResources().getColor(R.color.colorPrimary), getResources().getColor(R.color.white));
        changeColor.addTarget(R.id.comment_activity_content);
        changeColor.setDuration(350);
        transitionSet.addTransition(changeColor);

        transitionSet.setDuration(900);

        return transitionSet;
    }

    /**
     * 返回动画
     * @return
     */
    private TransitionSet buildShareElemReturnSet() {
        TransitionSet transitionSet = new TransitionSet();

        Transition revealTransition = new ShareElemReturnRevealTransition(commentContent);
        revealTransition.addTarget(R.id.comment_activity_content);
        revealTransition.setDuration(300);
        transitionSet.addTransition(revealTransition);

        // x+y-
        Transition changePos = new ShareElemReturnChangePosition(12, 219);
//        Transition changePos = new ShareElemReturnChangePosition(R.dimen.animate_offset_x, R.dimen.animate_offset_y);
        changePos.setDuration(300);
        changePos.addTarget(R.id.comment_activity_content);
        transitionSet.addTransition(changePos);

        ChangeColor changeColor = new ChangeColor(getResources().getColor(R.color.white), getResources().getColor(R.color.colorPrimary));
        changeColor.addTarget(R.id.comment_activity_content);
        changeColor.setDuration(350);
        transitionSet.addTransition(changeColor);

        transitionSet.setDuration(900);

        return transitionSet;
    }

    // 有这个方法才可以跳回前一个界面
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private class CommentAdapter extends BaseAdapter {
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
                convertView = new CommentCell(CommentActivity.this);
            }
            // config view
            CommentModel model = models.get(position);
            CommentCell cell = (CommentCell)convertView;
            UserModel user = model.getUser();
            CommentModel.Detail detail = model.detail;
            cell.usernameTv.setText(user.name);
            String build = String.valueOf(position) + "楼";
            cell.buildTv.setText(build);
            cell.contentTv.setText(detail.content);
            String like = "点赞：" + String.valueOf(detail.likesnum);
            cell.likeTv.setText(like);
            cell.timeTv.setText(detail.time);
            // FIXME: - 时间需要做处理
//            cell.timeTv.setText(model.getTime());
            return cell;
        }
    }
}
