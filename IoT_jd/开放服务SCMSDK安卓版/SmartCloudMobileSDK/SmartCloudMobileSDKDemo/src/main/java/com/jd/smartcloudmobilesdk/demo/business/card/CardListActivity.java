package com.jd.smartcloudmobilesdk.demo.business.card;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jd.smartcloudmobilesdk.business.BusinessManager;
import com.jd.smartcloudmobilesdk.demo.BaseActivity;
import com.jd.smartcloudmobilesdk.demo.R;
import com.jd.smartcloudmobilesdk.demo.business.card.model.CardCell;
import com.jd.smartcloudmobilesdk.demo.business.card.model.CardModel;
import com.jd.smartcloudmobilesdk.net.ResponseCallback;
import com.jd.smartcloudmobilesdk.utils.CommonUtil;
import com.jd.smartcloudmobilesdk.utils.JLog;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yangchangan on 2018/5/2.
 */
public class CardListActivity extends BaseActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout mSwipeLayout;
    private ListView mCardListView;
    private CardListAdapter mCardListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_list);
        initView();
    }

    private void initView() {
        findViewById(R.id.iv_left).setOnClickListener(this);
        TextView titleView = (TextView) findViewById(R.id.tv_title);
        titleView.setText("卡片列表");
        titleView.setOnClickListener(this);

        mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.layout_swipe_refresh);
        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setColorSchemeResources(android.R.color.holo_red_light);

        mCardListView = (ListView) findViewById(R.id.list_view);
        mCardListAdapter = new CardListAdapter(this);
        mCardListView.setAdapter(mCardListAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getCardList();
    }

    @Override
    public void onRefresh() {
        getCardList();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_left:

                this.finish();
                break;
        }
    }

    private void getCardList() {
        BusinessManager.getListCards(new ResponseCallback() {

            @Override
            public void onStart() {
                super.onStart();
                showLoadingDialog();
            }

            @Override
            public void onSuccess(String response) {
                JLog.e(TAG, "onSuccess response = " + response);
                if (CommonUtil.isSuccessWithToast(mContext, response)) {
                    try {
                        String result = new JSONObject(response).optString("result");
                        CardModel cardModel = new Gson().fromJson(result, new TypeToken<CardModel>() {
                        }.getType());
                        if (cardModel != null) {
                            setCardList(cardModel.getCards());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(String response) {
                JLog.e(TAG, "onFailure response = " + response);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                dismissLoadingDialog();

                mSwipeLayout.setRefreshing(false);
            }
        });
    }

    private void setCardList(List<CardCell> cardList) {
        removeInvalidCardFlag(cardList);

        if (mCardListAdapter != null) {
            mCardListAdapter.setList(cardList);
        }
    }

    private void removeInvalidCardFlag(List<CardCell> cardList) {
        Map<String, Integer> cardFlag = getCardFlag();
        if (cardList == null || cardFlag == null) {
            return;
        }

        Map<String, Integer> validFlag = new HashMap<>();
        for (String key : cardFlag.keySet()) {
            for (CardCell card : cardList) {
                if ("scene_id".equals(key) || key.equals(card.getScene_id())) {
                    validFlag.put(key, cardFlag.get(key));
                }
            }
        }

        setCardFlag(validFlag);
    }

    public Map<String, Integer> getCardFlag() {
        if (mCardListAdapter != null) {
            return mCardListAdapter.getCardFlag();
        }

        return null;
    }

    public void setCardFlag(Map<String, Integer> cardFlag) {
        if (mCardListAdapter != null) {
            mCardListAdapter.setCardFlag(cardFlag);
        }
    }
}
