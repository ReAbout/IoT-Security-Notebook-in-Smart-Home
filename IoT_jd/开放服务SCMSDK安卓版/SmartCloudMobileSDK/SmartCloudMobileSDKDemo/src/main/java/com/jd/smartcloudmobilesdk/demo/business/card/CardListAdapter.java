package com.jd.smartcloudmobilesdk.demo.business.card;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jd.smartcloudmobilesdk.business.BusinessManager;
import com.jd.smartcloudmobilesdk.demo.R;
import com.jd.smartcloudmobilesdk.demo.adapter.ArrayListAdapter;
import com.jd.smartcloudmobilesdk.demo.business.card.model.CardCell;
import com.jd.smartcloudmobilesdk.demo.business.card.model.CardDesc;
import com.jd.smartcloudmobilesdk.demo.business.card.model.Stream;
import com.jd.smartcloudmobilesdk.demo.utils.DisplayUtils;
import com.jd.smartcloudmobilesdk.net.ResponseCallback;
import com.jd.smartcloudmobilesdk.utils.CommonUtil;
import com.jd.smartcloudmobilesdk.utils.JLog;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by yangchangan on 2018/5/2.
 */
public class CardListAdapter extends ArrayListAdapter<CardCell> implements View.OnClickListener {
    private static final String TAG = "CardListAdapter";

    // 布局类型数
    private static final int CARD_TYPE1 = 1;
    private static final int CARD_TYPE2 = 2;
    private static final int CARD_TYPE3 = 3;
    private static final int CARD_TYPE101 = 101;
    private final int mCardTypeCount = 110;

    private Map<String, Integer> mCardFlag;

    public CardListAdapter(Context context) {
        super(context);
    }

    public Map<String, Integer> getCardFlag() {
        return mCardFlag;
    }

    public void setCardFlag(Map<String, Integer> cardFlag) {
        if (cardFlag == null) {
            mCardFlag = new HashMap<>();
        } else {
            mCardFlag = cardFlag;
        }
    }

    @Override
    public int getViewTypeCount() {
        return mCardTypeCount;
    }

    @Override
    public int getItemViewType(int position) {
        if (mList != null) {
            return mList.get(position).getCard_type();
        }
        return CARD_TYPE1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        int nViewType = getItemViewType(position);

        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_home_card, null);

            if (nViewType == CARD_TYPE101) {

                holder = new ViewHolder101(convertView);
            } else if (nViewType == CARD_TYPE1) {

                holder = new ViewHolder(convertView);
            } else if (nViewType == CARD_TYPE2) {

                holder = new ViewHolder2(convertView);
            } else if (nViewType == CARD_TYPE3) {

                holder = new ViewHolder3(convertView);
            } else {

                holder = new ViewHolder(convertView);
            }

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (nViewType == CARD_TYPE101) {
            ((ViewHolder101) holder).setViewHolder101(position);
        } else {

            // 设备卡片
            final CardCell card = getItem(position);
            holder.nameView.setText(card.getCard_name());
            List<String> imgUrl = card.getC_img_url();
            if (imgUrl != null && !imgUrl.isEmpty()) {
                ImageLoader.getInstance().displayImage(imgUrl.get(0), holder.iconView);
            }

            String[] stream_type_list = card.getStream_type_list();
            if (stream_type_list != null && stream_type_list.length > 0) {

                holder.statusView.setVisibility(View.VISIBLE);
                holder.statusView.setImageResource(R.mipmap.icon_status_ble);

                holder.describeView.setText("蓝牙设备");
                holder.describeView.setVisibility(View.VISIBLE);
                if (holder.typeLayout != null) {
                    holder.typeLayout.setVisibility(View.GONE);
                }
                holder.controlView.setVisibility(View.GONE);
            } else if (!("1".equals(card.getStatus()) || "1".equals(card.getLan_status()))) {
                holder.statusView.setVisibility(View.VISIBLE);
                holder.statusView.setImageResource(R.mipmap.icon_status_lost);

                holder.describeView.setText("设备掉线");
                holder.describeView.setVisibility(View.VISIBLE);
                holder.controlView.setVisibility(View.GONE);

                if (holder.typeLayout != null) {
                    holder.typeLayout.setVisibility(View.GONE);
                }
            } else {

                String descText = getDescribeText(card);
                if (TextUtils.isEmpty(descText)) {
                    holder.describeView.setVisibility(View.GONE);
                } else {
                    holder.describeView.setVisibility(View.VISIBLE);
                    holder.describeView.setText(descText);
                }

                List<CardDesc> cardControl = card.getCard_control();
                int cid = card.getCid();
                if (cardControl == null || cardControl.isEmpty()) {
                    holder.statusView.setVisibility(View.GONE);
                    holder.controlView.setVisibility(View.GONE);
                    if (cid != 105003) {
                        holder.describeView.setVisibility(View.GONE);
                    }
                } else {

                    String stream_id = cardControl.get(0).getStream_id();
                    String currentValue = getCurrentValue(card, stream_id);
                    if ("1".equals(currentValue)) {
                        holder.statusView.setVisibility(View.VISIBLE);
                        holder.statusView.setImageResource(R.mipmap.icon_status_on);
                    } else {
                        holder.statusView.setVisibility(View.VISIBLE);
                        holder.statusView.setImageResource(R.mipmap.icon_status_off);
                        if (cid != 105003) {
                            holder.describeView.setVisibility(View.GONE);
                        }
                    }
                    holder.controlView.setVisibility(View.VISIBLE);
                    holder.controlView.setTag(position);
                    holder.controlView.setOnClickListener(this);
                }

                if (nViewType == CARD_TYPE2) {
                    ((ViewHolder2) holder).setViewHolder2(position);
                } else if (nViewType == CARD_TYPE3) {
                    ((ViewHolder3) holder).setViewHolder3(position);
                }
            }
        }

        return convertView;
    }

    private String getCurrentValue(CardCell card, String stream_id) {
        List<Stream> snapshotList = card.getSnapshot();
        for (Stream stream : snapshotList) {
            if (stream.getStream_id().equals(stream_id)) {
                return stream.getCurrent_value();
            }
        }
        return "0";
    }

    private String getDescribeText(CardCell card) {
        StringBuffer buffer = new StringBuffer();

        List<Stream> snapshotList = card.getSnapshot();
        if (snapshotList == null || snapshotList.isEmpty()) {
            return buffer.toString();
        }

        List<CardDesc> descList = card.getCard_desc();
        if (descList == null || descList.isEmpty()) {
            return buffer.toString();
        }

        int size = descList.size();
        for (int i = 0; i < size; i++) {
            for (Stream stream : snapshotList) {
                if (stream.getStream_id().equals(descList.get(i).getStream_id())) {
                    if (descList.get(i).getOptions() == null) {

                        if (!TextUtils.isEmpty(stream.getCurrent_value())) {
                            buffer.append(descList.get(i).getStream_text());
                            buffer.append("：");
                            buffer.append(stream.getCurrent_value());

                            if (!TextUtils.isEmpty(descList.get(i).getUnit())) {
                                buffer.append(descList.get(i).getUnit());
                            }
                        }
                    } else {

                        for (Map<String, String> map : descList.get(i).getOptions()) {
                            if (map != null && map.containsKey(stream.getCurrent_value())) {

                                if (!TextUtils.isEmpty(map.get(stream.getCurrent_value()))) {
                                    buffer.append(descList.get(i).getStream_text());
                                    buffer.append("：");
                                    buffer.append(map.get(stream.getCurrent_value()));

                                    if (!TextUtils.isEmpty(descList.get(i).getUnit())) {
                                        buffer.append(descList.get(i).getUnit());
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (i != size - 1 && buffer.length() > 0) {
                buffer.append(" | ");
            }
        }

        String descText = buffer.toString();
        if (descText.endsWith(" | ")) {
            descText = descText.substring(0, descText.lastIndexOf(" | "));
        }

        return descText.trim();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_control:

                doControlItem(v, 0);
                break;
            case R.id.iv_control1:

                doControlItem(v, 1);
                break;
            case R.id.iv_control2:

                doControlItem(v, 2);
                break;
            case R.id.iv_control3:

                doControlItem(v, 3);
                break;
            case R.id.btn_execute:

                doControlItem(v, 101);
                break;
        }
    }

    private void doControlItem(View v, int mark) {
        int position = (int) v.getTag();
        if (position < 0 || position > mList.size()) {
            return;
        }

        controlCard(position, mark);
    }

    /**
     * 卡片控制设备或场景
     */
    private void controlCard(final int position, final int mark) {
        Map<String, Object> map = getControlMap(position, mark);
        BusinessManager.controlCard(map, new ResponseCallback() {
            @Override
            public void onStart() {
                ((CardListActivity) mContext).showLoadingDialog();
            }

            @Override
            public void onSuccess(String response) {
                JLog.e(TAG, "controlCard: response = " + response);
                if (CommonUtil.isSuccessWithToast(mContext, response)) {
                    try {
                        JSONObject responseObject = new JSONObject(response);
                        JSONObject resultObject = responseObject.optJSONObject("result");
                        if (resultObject == null) {
                            return;
                        }
                        String snapshot = resultObject.getString("snapshot");
                        List<Stream> curSnapshots = new Gson().fromJson(snapshot, new TypeToken<List<Stream>>() {
                        }.getType());

                        updateSnapshotList(position, curSnapshots);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(String response) {
                showToast("网络错误，请检查您的网络");
            }

            @Override
            public void onFinish() {
                ((CardListActivity) mContext).dismissLoadingDialog();
            }
        });
    }

    private Map<String, Object> getControlMap(final int position, final int mark) {
        CardCell card = getItem(position);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("cardId", card.getCard_id());
        map.put("cardType", card.getCard_type());

        if (card.getCard_type() == CARD_TYPE101) {
            map.put("relativeId", card.getScene_id());
        } else {
            map.put("relativeId", card.getFeed_id());
        }

        if (mark == 101) {
            List<CardDesc> controlList = new ArrayList<>();
            map.put("command", new Gson().toJson(controlList));
        } else if (mark == 0) {
            List<CardDesc> controlList = new ArrayList<>();
            controlList.add(card.getCard_control().get(mark));
            map.put("command", new Gson().toJson(controlList));
        } else if (mark > 0) {
            List<CardDesc> controlList = new ArrayList<>();
            controlList.add(card.getAdd_card_control().get(mark - 1));
            map.put("command", new Gson().toJson(controlList));
        }

        return map;
    }

    /**
     * 更新设备快照
     */
    public void updateSnapshotList(int position, List<Stream> curSnapshots) {
        List<Stream> preSnapshots = getItem(position).getSnapshot();
        if (preSnapshots == null || curSnapshots == null) {
            return;
        }

        for (Stream curStream : curSnapshots) {
            for (Stream preStream : preSnapshots) {
                if (preStream.getStream_id().equals(curStream.getStream_id())) {
                    preStream.setCurrent_value(curStream.getCurrent_value());
                }
            }
        }

        this.notifyDataSetChanged();
    }

    private void showToast(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
        }
    }

    public class ViewHolder {
        ImageView iconView;
        TextView nameView;
        ImageView statusView;
        TextView describeView;

        ImageView arrowView;
        ImageView controlView;

        // 副卡根视图
        View typeLayout;

        ViewHolder() {
        }

        ViewHolder(View convertView) {
            ViewStub viewStub = findViewById(convertView, R.id.vs_card_type);
            View view = viewStub.inflate();

            this.iconView = findViewById(view, R.id.iv_icon);
            this.nameView = findViewById(view, R.id.tv_name);
            this.statusView = findViewById(view, R.id.iv_status);
            this.describeView = findViewById(view, R.id.tv_describe);
            this.arrowView = findViewById(view, R.id.iv_arrow);
            this.controlView = findViewById(view, R.id.iv_control);
        }
    }

    public class ViewHolder2 extends ViewHolder {

        // 状态类
        View descLayout1;
        TextView descView11;
        TextView descView12;

        View descLayout2;
        TextView descView21;
        TextView descView22;

        View descLayout3;
        TextView descView31;
        TextView descView32;

        ViewHolder2(View convertView) {
            super(convertView);

            ViewStub viewStub2 = findViewById(convertView, R.id.vs_card_type2);
            View view = viewStub2.inflate();

            this.typeLayout = findViewById(view, R.id.layout_card_type2);

            this.descLayout1 = findViewById(view, R.id.layout_desc1);
            this.descView11 = findViewById(view, R.id.tv_desc11);
            this.descView12 = findViewById(view, R.id.tv_desc12);

            this.descLayout2 = findViewById(view, R.id.layout_desc2);
            this.descView21 = findViewById(view, R.id.tv_desc21);
            this.descView22 = findViewById(view, R.id.tv_desc22);

            this.descLayout3 = findViewById(view, R.id.layout_desc3);
            this.descView31 = findViewById(view, R.id.tv_desc31);
            this.descView32 = findViewById(view, R.id.tv_desc32);
        }

        private void setViewHolder2(int position) {

            // 标准化参数有问题，临时隐藏副卡
            boolean isShowViewHolder2 = false;
            if (!isShowViewHolder2) {
                this.typeLayout.setVisibility(View.GONE);
                return;
            }

            CardCell card = getItem(position);
            if (card == null) {
                return;
            }

            List<Stream> snapshotList = card.getSnapshot();
            List<CardDesc> addDescList = card.getAdd_card_desc();
            if (snapshotList == null || snapshotList.isEmpty()) {
                this.typeLayout.setVisibility(View.GONE);
            } else if (addDescList == null || addDescList.isEmpty()) {
                this.typeLayout.setVisibility(View.GONE);
            } else if (addDescList.size() == 1) {
                this.typeLayout.setVisibility(View.VISIBLE);

                this.descLayout1.setVisibility(View.VISIBLE);
                this.descLayout2.setVisibility(View.GONE);
                this.descLayout3.setVisibility(View.GONE);

                setDescView11Text(card);
                this.descView12.setText(addDescList.get(0).getStream_text());

            } else if (addDescList.size() == 2) {
                this.typeLayout.setVisibility(View.VISIBLE);

                this.descLayout1.setVisibility(View.VISIBLE);
                this.descLayout2.setVisibility(View.VISIBLE);
                this.descLayout3.setVisibility(View.GONE);

                setDescView11Text(card);
                this.descView12.setText(addDescList.get(0).getStream_text());

                setDescView21Text(card);
                this.descView22.setText(addDescList.get(1).getStream_text());

            } else if (addDescList.size() >= 3) {
                this.typeLayout.setVisibility(View.VISIBLE);

                this.descLayout1.setVisibility(View.VISIBLE);
                this.descLayout2.setVisibility(View.VISIBLE);
                this.descLayout3.setVisibility(View.VISIBLE);

                setDescView11Text(card);
                this.descView12.setText(addDescList.get(0).getStream_text());

                setDescView21Text(card);
                this.descView22.setText(addDescList.get(1).getStream_text());

                setDescView31Text(card);
                this.descView32.setText(addDescList.get(2).getStream_text());
            }
        }

        private void setDescView11Text(CardCell card) {
            String descText1 = getAddDescribeText(card, 0);
            if (TextUtils.isEmpty(descText1)) {
                this.descView11.setText("－－");
                this.descView11.setTextColor(0xFF9B9B9B);
            } else {
                this.descView11.setText(descText1);
                this.descView11.setTextColor(0xFFFF5C5C);
            }
        }

        private void setDescView21Text(CardCell card) {
            String descText2 = getAddDescribeText(card, 1);
            if (TextUtils.isEmpty(descText2)) {
                this.descView21.setText("－－");
                this.descView21.setTextColor(0xFF9B9B9B);
            } else {
                this.descView21.setText(descText2);
                this.descView21.setTextColor(0xFFFF5C5C);
            }
        }

        private void setDescView31Text(CardCell card) {
            String descText3 = getAddDescribeText(card, 2);
            if (TextUtils.isEmpty(descText3)) {
                this.descView31.setText("－－");
                this.descView31.setTextColor(0xFF9B9B9B);
            } else {
                this.descView31.setText(descText3);
                this.descView31.setTextColor(0xFFFF5C5C);
            }
        }

        private String getAddDescribeText(CardCell card, int position) {
            StringBuffer buffer = new StringBuffer();

            List<Stream> snapshotList = card.getSnapshot();
            if (snapshotList == null || snapshotList.isEmpty()) {
                return buffer.toString();
            }

            List<CardDesc> addDescList = card.getAdd_card_desc();
            if (addDescList == null || addDescList.isEmpty()) {
                return buffer.toString();
            }

            String stream_id = addDescList.get(position).getStream_id();
            List<Map<String, String>> options = addDescList.get(position).getOptions();
            for (Stream stream : snapshotList) {
                if (stream != null && stream.getStream_id().equals(stream_id)) {
                    if (options == null || options.isEmpty()) {
                        if (!TextUtils.isEmpty(stream.getCurrent_value())) {
                            buffer.append(stream.getCurrent_value());

                            if (!TextUtils.isEmpty(addDescList.get(position).getUnit())) {
                                buffer.append(addDescList.get(position).getUnit());
                            }
                        }
                    } else {
                        for (Map<String, String> map : options) {
                            if (map != null && map.containsKey(stream.getCurrent_value())) {
                                if (!TextUtils.isEmpty(map.get(stream.getCurrent_value()))) {
                                    buffer.append(map.get(stream.getCurrent_value()));

                                    if (!TextUtils.isEmpty(addDescList.get(position).getUnit())) {
                                        buffer.append(addDescList.get(position).getUnit());
                                    }
                                }
                            }
                        }
                    }
                }
            }

            return buffer.toString();
        }
    }

    private class ViewHolder3 extends ViewHolder {

        // 控制类
        ImageView controlView1;
        ImageView controlView2;
        ImageView controlView3;

        ViewHolder3(View convertView) {
            super(convertView);

            ViewStub viewStub3 = findViewById(convertView, R.id.vs_card_type3);
            View view = viewStub3.inflate();

            this.typeLayout = findViewById(view, R.id.layout_card_type3);

            this.controlView1 = findViewById(view, R.id.iv_control1);
            this.controlView2 = findViewById(view, R.id.iv_control2);
            this.controlView3 = findViewById(view, R.id.iv_control3);
        }

        private void setViewHolder3(int position) {
            CardCell card = getItem(position);

            List<CardDesc> addControlList = card.getAdd_card_control();
            if (addControlList == null || addControlList.isEmpty()) {
                this.typeLayout.setVisibility(View.GONE);
            } else if (addControlList.size() == 1) {
                this.typeLayout.setVisibility(View.VISIBLE);

                this.controlView1.setVisibility(View.VISIBLE);
                this.controlView1.setTag(position);
                this.controlView1.setOnClickListener(CardListAdapter.this);
                this.controlView1.setImageResource(getImageResource(addControlList, 0));

                this.controlView2.setVisibility(View.GONE);

                this.controlView3.setVisibility(View.GONE);
            } else if (addControlList.size() == 2) {
                this.typeLayout.setVisibility(View.VISIBLE);

                this.controlView1.setVisibility(View.VISIBLE);
                this.controlView1.setTag(position);
                this.controlView1.setOnClickListener(CardListAdapter.this);
                this.controlView1.setImageResource(getImageResource(addControlList, 0));

                this.controlView2.setVisibility(View.VISIBLE);
                this.controlView2.setTag(position);
                this.controlView2.setOnClickListener(CardListAdapter.this);
                this.controlView2.setImageResource(getImageResource(addControlList, 1));

                this.controlView3.setVisibility(View.GONE);
            } else if (addControlList.size() >= 3) {
                this.typeLayout.setVisibility(View.VISIBLE);

                this.controlView1.setVisibility(View.VISIBLE);
                this.controlView1.setTag(position);
                this.controlView1.setOnClickListener(CardListAdapter.this);
                this.controlView1.setImageResource(getImageResource(addControlList, 0));

                this.controlView2.setVisibility(View.VISIBLE);
                this.controlView2.setTag(position);
                this.controlView2.setOnClickListener(CardListAdapter.this);
                this.controlView2.setImageResource(getImageResource(addControlList, 1));

                this.controlView3.setVisibility(View.VISIBLE);
                this.controlView3.setTag(position);
                this.controlView3.setOnClickListener(CardListAdapter.this);
                this.controlView3.setImageResource(getImageResource(addControlList, 2));
            }
        }

        private int getImageResource(List<CardDesc> addControlList, int position) {
            String stream_text = addControlList.get(position).getStream_text();
            if (TextUtils.isEmpty(stream_text)) {
                return R.drawable.btn_open_selector;
            } else if (stream_text.contains("开")) {
                return R.drawable.btn_open_selector;
            } else if (stream_text.contains("关")) {
                return R.drawable.btn_close_selector;
            } else if (stream_text.contains("暂停")) {
                return R.drawable.btn_pause_selector;
            } else if (stream_text.contains("降")) {
                return R.drawable.btn_down_selector;
            } else if (stream_text.contains("升")) {
                return R.drawable.btn_up_selector;
            }

            return R.drawable.btn_open_selector;
        }
    }

    private class ViewHolder101 extends ViewHolder {
        Button executeBtn;
        TextView timeExpress;
        LinearLayout containerLayout;
        HorizontalScrollView containerScrollView;

        ViewHolder101(View convertView) {
            super();

            ViewStub viewStub = findViewById(convertView, R.id.vs_card_type101);
            View view = viewStub.inflate();

            this.typeLayout = findViewById(view, R.id.layout_card_type101);

            this.iconView = findViewById(view, R.id.iv_icon);
            this.nameView = findViewById(view, R.id.tv_name);

            this.executeBtn = findViewById(view, R.id.btn_execute);
            this.containerLayout = findViewById(view, R.id.layout_container);
            this.containerScrollView = findViewById(view, R.id.hsv_container);

            this.timeExpress = findViewById(view, R.id.tv_time_express);
        }

        private void setViewHolder101(int position) {
            CardCell card = getItem(position);

            this.nameView.setText(card.getCard_name());
            this.executeBtn.setTag(position);
            this.executeBtn.setOnClickListener(CardListAdapter.this);
            addImages(this.containerLayout, card.getC_img_url());

            int cardFlagValue = getCardFlagValue(card.getScene_id());
            if (cardFlagValue == 2) {
                this.typeLayout.setBackgroundResource(R.mipmap.bg_scene_normal2);
            } else {
                this.typeLayout.setBackgroundResource(R.mipmap.bg_scene_normal);
            }

            // 场景定时表达式
            if (TextUtils.isEmpty(card.getNext_exe_time())) {
                this.timeExpress.setVisibility(View.GONE);
            } else {
                this.timeExpress.setVisibility(View.VISIBLE);

                String timeExpress = getTimeExpress(card.getNext_exe_time(), card.getNext_exe_time_express());
                this.timeExpress.setText(timeExpress);
            }
        }

        private void addImages(LinearLayout layout, List<String> images) {
            if (images == null || images.isEmpty()) {
                return;
            }

            if (layout.getChildCount() > 0) {
                layout.removeAllViews();
            }

            int size = images.size();
            int width = DisplayUtils.dip2px(mContext, 42);
            for (int i = 0; i < size; i++) {
                ImageView imageView = new ImageView(mContext);
                imageView.setLayoutParams(new ViewGroup.LayoutParams(width, width));
                imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                ImageLoader.getInstance().displayImage(images.get(i), imageView);
                layout.addView(imageView);
            }

            if (size > 3) {
                ViewGroup.LayoutParams params = containerScrollView.getLayoutParams();
                params.width = (int) (3.5f * width);
                containerScrollView.setLayoutParams(params);
            } else {
                ViewGroup.LayoutParams params = containerScrollView.getLayoutParams();
                params.width = size * width;
                containerScrollView.setLayoutParams(params);
            }
        }

        private int getCardFlagValue(String scene_id) {
            if (mCardFlag == null) {
                mCardFlag = new HashMap<>();
            }

            if (mCardFlag.containsKey(scene_id)) {
                return mCardFlag.get(scene_id);
            }

            if (mCardFlag.containsKey("scene_id")) {
                if (mCardFlag.get("scene_id") == 1) {
                    mCardFlag.put("scene_id", 2);
                    mCardFlag.put(scene_id, 2);
                } else {
                    mCardFlag.put("scene_id", 1);
                    mCardFlag.put(scene_id, 1);
                }
            } else {
                mCardFlag.put("scene_id", 1);
                mCardFlag.put(scene_id, 1);
            }

            return mCardFlag.get(scene_id);
        }

        /**
         * 获取场景时间表达式
         */
        private String getTimeExpress(String exeTime, String exeTimeExpress) {

            String timeExpress = "";
            if (TextUtils.isEmpty(exeTime) || TextUtils.isEmpty(exeTimeExpress)) {
                return timeExpress;
            }

            try {
                String[] weeks = {"日", "一", "二", "三", "四", "五", "六"};

                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                calendar.setTime(format.parse(exeTime));
                int week = calendar.get(Calendar.DAY_OF_WEEK);

                SimpleDateFormat formatter = new SimpleDateFormat("HH:mm", Locale.getDefault());
                String time = formatter.format(calendar.getTime());

                if (!exeTimeExpress.endsWith("*")) {

                    // 仅一次
                    timeExpress = String.format("%s场景执行", time);
                } else {

                    // 重复
                    timeExpress = String.format("周%s %s场景执行", weeks[week - 1], time);
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }

            return timeExpress;
        }
    }
}
