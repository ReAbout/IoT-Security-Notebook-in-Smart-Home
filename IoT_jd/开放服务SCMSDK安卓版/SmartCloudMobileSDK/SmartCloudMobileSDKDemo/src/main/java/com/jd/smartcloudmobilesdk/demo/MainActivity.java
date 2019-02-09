package com.jd.smartcloudmobilesdk.demo;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.jd.smartcloudmobilesdk.demo.adapter.ArrayListAdapter;
import com.jd.smartcloudmobilesdk.demo.authorize.AuthorizeActivity;
import com.jd.smartcloudmobilesdk.demo.business.card.CardListActivity;
import com.jd.smartcloudmobilesdk.demo.business.category.CategoryActivity;
import com.jd.smartcloudmobilesdk.demo.config.ConfigNetActivity;
import com.jd.smartcloudmobilesdk.demo.control.DeviceListActivity;
import com.jd.smartcloudmobilesdk.demo.ifttt.SceneListActivity;
import com.jd.smartcloudmobilesdk.demo.openapi.OpenApiActivity;
import com.jd.smartcloudmobilesdk.demo.scene.SceneListUI;
import com.jd.smartcloudmobilesdk.init.AppManager;
import com.jd.smartcloudmobilesdk.init.JDSmartSDK;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private MainListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.iv_left).setVisibility(View.GONE);
        TextView titleView = (TextView) findViewById(R.id.tv_title);
        String title = getResources().getString(R.string.app_name);
        titleView.setText(String.format("%s  v%s", title, JDSmartSDK.getInstance().getVersion()));

        ListView listView = (ListView) findViewById(R.id.list_view);

        mAdapter = new MainListAdapter(this);
        mAdapter.setList(getList());
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Map<String, Object> map = mAdapter.getItem(position);
        Class<?> className = (Class<?>) map.get("className");
        if (className == AuthorizeActivity.class) {
            startActivity(AuthorizeActivity.class);
        } else {
            if (isAuthorize()) {
                startActivity(className);
            } else {
                startActivity(AuthorizeActivity.class);
            }
        }
    }

    private boolean isAuthorize() {
        return !TextUtils.isEmpty(AppManager.getInstance().getAccessToken());
    }

    private class MainListAdapter extends ArrayListAdapter<Map<String, Object>> {

        MainListAdapter(Context context) {
            super(context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            final ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(mContext, R.layout.item_main_list, null);
                holder = new ViewHolder();
                holder.nameView = findViewById(convertView, R.id.tv_name);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Map<String, Object> map = getItem(position);
            holder.nameView.setText((String) map.get("name"));
            return convertView;
        }

        class ViewHolder {
            TextView nameView;
        }
    }

    private List<Map<String, Object>> getList() {
        List<Map<String, Object>> list = new ArrayList<>();

        Map<String, Object> map = new HashMap<>();
        map.put("name", "设备入网");
        map.put("className", ConfigNetActivity.class);
        list.add(map);

        map = new HashMap<>();
        map.put("name", "设备控制");
        map.put("className", DeviceListActivity.class);
        list.add(map);

        map = new HashMap<>();
        map.put("name", "智能场景");
        map.put("className", SceneListUI.class);
        list.add(map);

        map = new HashMap<>();
        map.put("name", "IFTTT");
        map.put("className", SceneListActivity.class);
        list.add(map);

        map = new HashMap<>();
        map.put("name", "开放API");
        map.put("className", OpenApiActivity.class);
        list.add(map);

        map = new HashMap<>();
        map.put("name", "卡片列表");
        map.put("className", CardListActivity.class);
        list.add(map);

        map = new HashMap<>();
        map.put("name", "三级类目");
        map.put("className", CategoryActivity.class);
        list.add(map);

        map = new HashMap<>();
        map.put("name", "授权登录");
        map.put("className", AuthorizeActivity.class);
        list.add(map);

        return list;
    }
}
