package com.jd.smartcloudmobilesdk.demo.control;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jd.smartcloudmobilesdk.demo.BaseActivity;
import com.jd.smartcloudmobilesdk.demo.R;
import com.jd.smartcloudmobilesdk.demo.control.model.DeviceModel;
import com.jd.smartcloudmobilesdk.devicecontrol.DeviceControlManager;
import com.jd.smartcloudmobilesdk.devicecontrol.model.DevDetailModel;
import com.jd.smartcloudmobilesdk.net.ResponseCallback;
import com.jd.smartcloudmobilesdk.utils.CommonUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 设备列表
 * Created by yangchangan on 2017/3/17.
 */
public class DeviceListActivity extends BaseActivity implements View.OnClickListener,
        AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout mSwipeLayout;
    private ListView mListView;

    private DeviceListAdapter mListAdapter;
    private List<DevDetailModel> mAdapterList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);
        initView();
    }

    private void initView() {
        findViewById(R.id.iv_left).setOnClickListener(this);
        TextView titleView = (TextView) findViewById(R.id.tv_title);
        titleView.setText(getString(R.string.device_list));
        titleView.setOnClickListener(this);

        mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.layout_swipe_refresh);
        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setColorSchemeResources(android.R.color.holo_red_light);

        mListView = (ListView) findViewById(R.id.lv_device);
        mListAdapter = new DeviceListAdapter(this);
        mListView.setAdapter(mListAdapter);
        mListView.setOnItemClickListener(this);

        mLogView = (TextView) findViewById(R.id.tv_log);
        mLogScrollView = (ScrollView) findViewById(R.id.sv_log);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getDeviceList();
    }

    private void getDeviceList() {
        DeviceControlManager.getDeviceList(new ResponseCallback() {

            @Override
            public void onStart() {
                super.onStart();
                showLoadingDialog();
            }

            @Override
            public void onSuccess(String response) {
                if (CommonUtil.isSuccessWithToast(mContext, response)) {
                    try {

                        // 临时添加测试log
                        setLogText(response);

                        String result = new JSONObject(response).getString("result");
                        ArrayList<DeviceModel> models = new Gson().fromJson(result,
                                new TypeToken<ArrayList<DeviceModel>>() {
                                }.getType());

                        setAdapterList(models);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(String response) {
            }

            @Override
            public void onFinish() {
                super.onFinish();
                dismissLoadingDialog();

                mSwipeLayout.setRefreshing(false);
            }

        });
    }

    private void setAdapterList(List<DeviceModel> models) {
        if (mAdapterList == null) {
            mAdapterList = new ArrayList<>();
        } else if (!mAdapterList.isEmpty()) {
            mAdapterList.clear();
        }

        for (DeviceModel model : models) {
            if (model.getList() != null) {
                mAdapterList.addAll(model.getList());
            }
        }

        if (mListAdapter != null) {
            mListAdapter.setList(mAdapterList);
        }

    }

    @Override
    public void onRefresh() {
        getDeviceList();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_left:

                this.finish();
                break;
            case R.id.tv_title:

                setLogViewVisibility();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        DevDetailModel model = (DevDetailModel) parent.getItemAtPosition(position);
        Intent intent = new Intent(DeviceListActivity.this, HtmlDetailActivity.class);
        intent.putExtra("feed_id", model.getFeed_id());
        startActivity(intent);
    }
}
