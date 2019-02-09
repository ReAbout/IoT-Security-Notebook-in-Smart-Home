package com.jd.smartcloudmobilesdk.demo.control.view;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.jd.smartcloudmobilesdk.demo.R;

import java.util.List;


public class PopUpMenuView extends PopupWindow {
    private Context context;
    private View view;
    private ListView listView;
    private List<String> list;
//    private List<String> iconList;
//    private List<String> callBacklist;
    private List<String> textList;
    private OnPopMenuCallBack popMenuCallBack;
    private ApplicationInfo appInfo;


    public PopUpMenuView(Context context) {
        this(context, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public PopUpMenuView(Context context, int with, int height) {
        this.context = context;
        setWidth(with);
        setHeight(height);
        setFocusable(true);
        setTouchable(true);
        setOutsideTouchable(true);
        ColorDrawable dw = new ColorDrawable(0xff291612);
        setBackgroundDrawable(dw);

        appInfo = PopUpMenuView.this.context.getApplicationInfo();

        view = LayoutInflater.from(context).inflate(R.layout.popmenu_layout,null);
        setContentView(view);

        listView = (ListView) view.findViewById(R.id.popMenu_list);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(PopUpMenuView.this.context,"您选择了"+i, Toast.LENGTH_LONG).show();
                popMenuCallBack.onSelectItem(i);
            }
        });

    }

    public void initData(){

        listView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return list.size();
            }

            @Override
            public Object getItem(int position) {
                return list.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View itemView;
                if (convertView == null){
                    itemView = View.inflate(context, R.layout.detail_popmenuitem_layout,null);
                }else {
                    itemView = convertView;
                }

                TextView itemTextView = (TextView) itemView.findViewById(R.id.popItemTextID);
                itemTextView.setText(list.get(position));

//                int resID = PopUpMenuView.this.context.getResources().getIdentifier(iconList.get(position), "drawable", appInfo.packageName);
//                ImageView itemImageView = (ImageView) itemView.findViewById(R.id.popItemImageID);
//                itemImageView.setImageResource(resID);

                return itemView;
            }
        });
    }

    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            this.showAsDropDown(parent, parent.getLayoutParams().width / 2, 2);
        } else {
            this.dismiss();
        }
    }

    public void setOnPopItemCallBack(PopUpMenuView.OnPopMenuCallBack callBack) {
        popMenuCallBack = callBack;
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

//    public List<String> getIconList() {
//        return iconList;
//    }
//
//    public void setIconList(List<String> iconList) {
//        this.iconList = iconList;
//    }
//
//    public List<String> getCallBacklist() {
//        return callBacklist;
//    }
//
//    public void setCallBacklist(List<String> callBacklist) {
//        this.callBacklist = callBacklist;
//    }

    public List<String> getTextList() {
        return textList;
    }

    public void setTextList(List<String> textList) {
        this.textList = textList;
    }

    public static interface OnPopMenuCallBack {

        public void onSelectItem(int index);

    }
}
