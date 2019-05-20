package com.example.seacen.news.Comment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Network;
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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.seacen.news.Account.Account;
import com.example.seacen.news.Account.LoginActivity;
import com.example.seacen.news.Account.UserModel;
import com.example.seacen.news.Comment.transition.ChangeColor;
import com.example.seacen.news.Comment.transition.ChangePosition;
import com.example.seacen.news.Comment.transition.CommentEnterTransition;
import com.example.seacen.news.Comment.transition.ShareElemEnterRevealTransition;
import com.example.seacen.news.Comment.transition.ShareElemReturnChangePosition;
import com.example.seacen.news.Comment.transition.ShareElemReturnRevealTransition;
import com.example.seacen.news.R;
import com.example.seacen.news.Utils.Network.SCNetworkHandler;
import com.example.seacen.news.Utils.Network.SCNetworkMethod;
import com.example.seacen.news.Utils.Network.SCNetworkPort;
import com.example.seacen.news.Utils.Network.SCNetworkTool;
import com.example.seacen.news.Utils.TimeUtil;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
    @BindView(R.id.comment_activity_send_btn)
    TextView sendBtn;
    @BindView(R.id.comment_activity_edit_et)
    EditText editText;

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

        refreshLayout.autoRefresh();
    }

    @OnClick(R.id.comment_activity_send_btn)
    void sendBtnClick() {
        if (!loginCheck()) {
            return;
        }
        String text = editText.getText().toString().trim();
        if (text.isEmpty()) {
            // 提示用户输入留言
            Toast toast = Toast.makeText(CommentActivity.this, "请输入评论内容", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        // 发送评论（禁用发送按钮）
        sendBtn.setEnabled(false);
        Map<String, Object> params = new HashMap<>();
        params.put("newsid", newsid);
        params.put("content", text);
        SCNetworkTool.shared().normalRequest(SCNetworkPort.AddComment, SCNetworkMethod.POST, params, new SCNetworkHandler() {
            @Override
            public void successHandle(JSONObject jsonObject) {
                int code = jsonObject.getInteger("status");
                if (code == 200) {
                    // 评论成功
                    editText.setText("");
                    Toast toast = Toast.makeText(CommentActivity.this, "评论成功", Toast.LENGTH_SHORT);
                    toast.show();
                    // 刷新评论
                    CommentModel commentModel = jsonObject.getObject("data", CommentModel.class);
                    models.add(0, commentModel);
                    adapter.notifyDataSetChanged();
                } else {
                    // 评论失败 -> 没有登录
                    Toast toast = Toast.makeText(CommentActivity.this, "评论失败", Toast.LENGTH_SHORT);
                    toast.show();
                }
                sendBtn.setEnabled(true);
            }

            @Override
            public void errorHandle(Exception error) {
                sendBtn.setEnabled(true);
                Toast toast = Toast.makeText(CommentActivity.this, error.toString(), Toast.LENGTH_SHORT);
                toast.show();
            }
        });
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
                            JSONArray array = data.getJSONArray("content");
                            models = array.toJavaList(CommentModel.class);
                            if (models.isEmpty()) {
                                Toast toast = Toast.makeText(CommentActivity.this, "暂时没有评论，赶快评论吧！", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                            adapter.notifyDataSetChanged();
                            refreshLayout.finishRefresh();
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

    /**
     * 判断是否登录，未登录就跳转登录界面
     * @return
     */
    private boolean loginCheck() {
        if (!Account.shared().isLogin()) {
            // TODO: - 跳转到登录界面
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            return false;
        }
        return true;
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
        public boolean isEnabled(int position) {
            return false;
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
            cell.contentTv.setText(detail.content);
            String like = String.valueOf(detail.likesnum);
            cell.likeTv.setText(like);
            cell.timeTv.setText(detail.time);
            cell.timeTv.setText(TimeUtil.getTimeFormatText(detail.getTime()));
            cell.setLike(detail.isLiked);

            cell.delegate = new CommentCell.Delegate() {
                @Override
                public void didLike(CommentCell cell) {
                    // 判断是否登录
                    if (!loginCheck()) {
                        return;
                    }

                    cell.likeIv.setEnabled(false);
                    Map<String, Object> param = new HashMap<>();
                    param.put("id", detail.id);
                    SCNetworkTool.shared().normalRequest(SCNetworkPort.LikesComment, SCNetworkMethod.GET, param, new SCNetworkHandler() {
                        @Override
                        public void successHandle(JSONObject jsonObject) {
                            cell.likeIv.setEnabled(true);
                            int code = jsonObject.getInteger("status");
                            if (code == 200) {
                                refreshLike(position, true);
                                Toast toast = Toast.makeText(CommentActivity.this, "点赞成功", Toast.LENGTH_SHORT);
                                toast.show();
                            } else {
                                Toast toast = Toast.makeText(CommentActivity.this, String.valueOf(code), Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        }

                        @Override
                        public void errorHandle(Exception error) {
                            cell.likeIv.setEnabled(true);
                            Toast toast = Toast.makeText(CommentActivity.this, "点赞失败，请稍后重试", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    });
                }

                @Override
                public void didCancellike(CommentCell cell) {
                    // 判断是否登录
                    if (!loginCheck()) {
                        return;
                    }

                    Map<String, Object> param = new HashMap<>();
                    param.put("id", detail.id);
                    SCNetworkTool.shared().normalRequest(SCNetworkPort.Cancellike, SCNetworkMethod.GET, param, new SCNetworkHandler() {
                        @Override
                        public void successHandle(JSONObject jsonObject) {
                            cell.likeIv.setEnabled(true);
                            int code = jsonObject.getInteger("status");
                            if (code == 200) {
                                refreshLike(position, false);
                                Toast toast = Toast.makeText(CommentActivity.this, "取消点赞成功", Toast.LENGTH_SHORT);
                                toast.show();
                            } else {
                                Toast toast = Toast.makeText(CommentActivity.this, String.valueOf(code), Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        }

                        @Override
                        public void errorHandle(Exception error) {
                            cell.likeIv.setEnabled(true);
                            Toast toast = Toast.makeText(CommentActivity.this, "取消点赞，请稍后重试", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    });
                }

                @Override
                public  void didMore(CommentCell cell) {
                    // 捕获当前模型的评论 user 来使用
                    // 判断该评论是否是自己
                    String [] item;
                    if (Account.shared().isMe(user.id)) {
                        item = new String[]{"删除"};
                    } else {
                        item = new String[]{"举报"};
                    }
                    AlertDialog.Builder builder = new AlertDialog.Builder(CommentActivity.this);
                    builder.setTitle("更多操作");
                    builder.setItems(item, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (item[which].equals("删除")) {
                                // 执行删除评论操作
                                // TODO: - 进行网络请求
                                Map<String, Object> param = new HashMap<>();
                                param.put("id", detail.id);
                                SCNetworkTool.shared().normalRequest(SCNetworkPort.DeleteComent, SCNetworkMethod.DELETE, param, new SCNetworkHandler() {
                                    @Override
                                    public void successHandle(JSONObject jsonObject) {
                                        int code = jsonObject.getInteger("status");
                                        if (code == 200) {
                                            Toast.makeText(CommentActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                                            models.remove(position);
                                            adapter.notifyDataSetChanged();
                                        } else {
                                            Toast.makeText(CommentActivity.this, "删除失败", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void errorHandle(Exception error) {
                                        Toast.makeText(CommentActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else if (item[which].equals("举报")) {
                                // 执行举报操作
                                Toast.makeText(CommentActivity.this, "已举报", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            };

            return cell;
        }
    }

    private void refreshLike(int position, Boolean like) {
        CommentModel.Detail detail = models.get(position).getDetail();
        detail.setLiked(like);
        detail.likesnum += like ? 1 : -1;
        adapter.notifyDataSetChanged();
    }
}
