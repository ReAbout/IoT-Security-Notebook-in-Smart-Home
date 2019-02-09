
package com.jd.smartcloudmobilesdk.demo.control.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jd.smartcloudmobilesdk.demo.BaseActivity;
import com.jd.smartcloudmobilesdk.demo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pengmin1 on 2017/3/8.
 *
 */
public class JboxPopupMenu2 {
    private PopIconAdapter mPopIconAdapter;
    private Context mContext;
    private ListView mListView;
    private View mView;
    private int type = 0;
    private PopupWindow mPopupWindow = null;
    private List<Integer> unclickablePosition = new ArrayList<Integer>();

    public JboxPopupMenu2(Context context, String[] str) {
        this.mContext = context;
        init(str);
    }

    public JboxPopupMenu2(Context context, String[] str, int width) {
        this.mContext = context;
        init(str, width);
    }
    
    public JboxPopupMenu2(Context context, String[] str, int width, int[] icon, String tab) {
        this.mContext = context;
        devicePop(str, width,icon);
    }

    public JboxPopupMenu2(Context context, int type, String[] str, int width) {
        this.mContext = context;
        this.type = type;
        init(str, width);
    }

    public JboxPopupMenu2(Context context, String[] str, int width, int height) {
        this.mContext = context;
        init(str, width, height);
    }


    public JboxPopupMenu2(Context context, String[] str, int[] icons, int width) {
        this.mContext = context;
        init(str, icons, width);
    }

    public JboxPopupMenu2(Context context, String[] str, int[] icons, int width, String count) {
        this.mContext = context;
        init(str, icons, width,count);
    }

    public PopupWindow getPopupWindow() {
        return mPopupWindow;
    }

    private void init(String[] str) {
        if (mPopupWindow == null) {
            LayoutInflater localLayoutInflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mView = localLayoutInflater.inflate(R.layout.list_menu, null);
            mListView = (ListView) mView.findViewById(R.id.listview);
            mPopIconAdapter = new PopIconAdapter(str);
            mListView.setAdapter(mPopIconAdapter);
            mListView.setItemsCanFocus(false);
            mPopupWindow = new PopupWindow(mView, LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT, true);
            mPopupWindow.setBackgroundDrawable(mContext.getResources().getDrawable(
                    R.drawable.titlebar_left_pop_bg));
        }
    }

    // 左侧无icon的pop
    private void init(String[] str, int width) {
        if (mPopupWindow == null) {
            LayoutInflater localLayoutInflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mView = localLayoutInflater.inflate(R.layout.list_menu, null);
            mListView = (ListView) mView.findViewById(R.id.listview);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            mListView.setLayoutParams(params);
            mPopIconAdapter = new PopIconAdapter(str);
            mListView.setAdapter(mPopIconAdapter);
            mListView.setItemsCanFocus(false);
            mPopupWindow = new PopupWindow(mView, width, LayoutParams.WRAP_CONTENT, true);
            mPopupWindow.setBackgroundDrawable(mContext.getResources().getDrawable(
                    R.drawable.titlebar_left_pop_bg));
        }
    }

    private void init(String[] str, int width, int height) {
        if (mPopupWindow == null) {
            LayoutInflater localLayoutInflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mView = localLayoutInflater.inflate(R.layout.list_menu, null);
            mListView = (ListView) mView.findViewById(R.id.listview);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            mListView.setLayoutParams(params);
            mPopIconAdapter = new PopIconAdapter(str);
            mListView.setAdapter(mPopIconAdapter);
            mListView.setItemsCanFocus(false);
            mPopupWindow = new PopupWindow(mView, width, height, true);
            mPopupWindow.setBackgroundDrawable(mContext.getResources().getDrawable(
                    R.drawable.titlebar_left_pop_bg));
        }
    }
    //设备互联
    private void devicePop(String[] str, int width,int[] icon) {
        if (mPopupWindow == null) {
            LayoutInflater localLayoutInflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mView = localLayoutInflater.inflate(R.layout.list_menu, null);
            mListView = (ListView) mView.findViewById(R.id.listview);
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width,
//                    LinearLayout.LayoutParams.WRAP_CONTENT);
//            mListView.setLayoutParams(params);
            mPopIconAdapter = new PopIconAdapter(str,icon,"");
            mListView.setAdapter(mPopIconAdapter);
            mListView.setItemsCanFocus(false);
            mPopupWindow = new PopupWindow(mView, width, LayoutParams.WRAP_CONTENT, true);
            mPopupWindow.setBackgroundDrawable(mContext.getResources().getDrawable(
                    R.drawable.pop));
        }
    }

    // 右侧有icon的pop
    private void init(String[] str, int[] icons, int width) {
        if (mPopupWindow == null) {
            LayoutInflater localLayoutInflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mView = localLayoutInflater.inflate(R.layout.list_menu, null);
            mListView = (ListView) mView.findViewById(R.id.listview);
//            mListView.setPadding(0, 10, 0, 0);
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width,
//                    LinearLayout.LayoutParams.WRAP_CONTENT);
//            mListView.setLayoutParams(params);
            mPopIconAdapter = new PopIconAdapter(str, icons,"");
            mListView.setAdapter(mPopIconAdapter);
            mListView.setItemsCanFocus(false);
            mPopupWindow = new PopupWindow(mView, width, LayoutParams.WRAP_CONTENT, true);
            mPopupWindow.setBackgroundDrawable(mContext.getResources().getDrawable(
                    R.drawable.pop));
        }
    }

    // 右侧有icon并且有消息提示的pop
    private void init(String[] str, int[] icons, int width,String count) {
        if (mPopupWindow == null) {
            LayoutInflater localLayoutInflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mView = localLayoutInflater.inflate(R.layout.list_menu, null);
            mListView = (ListView) mView.findViewById(R.id.listview);
//            mListView.setPadding(0, 10, 0, 0);
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width,
//                    LinearLayout.LayoutParams.WRAP_CONTENT);
//            mListView.setLayoutParams(params);
            mPopIconAdapter = new PopIconAdapter(str, icons,count);
            mListView.setAdapter(mPopIconAdapter);
            mListView.setItemsCanFocus(false);
            mPopupWindow = new PopupWindow(mView, width, LayoutParams.WRAP_CONTENT, true);
            mPopupWindow.setBackgroundDrawable(mContext.getResources().getDrawable(
                    R.drawable.pop));
        }
    }



    // 20130122-4上传pw,提供一个方法使其可以重新设置背景 119
    public void setBackground(Drawable background) {
        mPopupWindow.setBackgroundDrawable(background);
    }

    public void showAsDropDown(View view1, int x, int y) {
        if (mContext == null) {
            return;
        }

        if ((mContext instanceof Activity) && ((Activity) mContext).isFinishing()) {
            return;
        }

        if ((mContext instanceof BaseActivity)
                && /*JDApplication.getInstance().checkActivityIsDestoryed(
                        (JDBaseActivity) mContext)*/ false) {
            return;
        }
        mPopupWindow.showAsDropDown(view1, x, y);
    }

    public void showAtLocation(View view1, int gravity, int x, int y) {
        if (mContext == null) {
            return;
        }

        if ((mContext instanceof Activity) && ((Activity) mContext).isFinishing()) {
            return;
        }

        if ((mContext instanceof BaseActivity)
                && /*JDApplication.getInstance().checkActivityIsDestoryed(
                        (JDBaseActivity) mContext)*/ false) {
            return;
        }
        mPopupWindow.showAtLocation(view1, gravity, x, y);
    }

    public boolean isShowing() {
        return (mPopupWindow != null) && (mPopupWindow.isShowing());
    }

    public void closePopupMenu() {
        if ((mPopupWindow != null) && (mPopupWindow.isShowing()))
            mPopupWindow.dismiss();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListView.setOnItemClickListener(listener);
    }

    public void setUnclickablePosition(int position) {
        unclickablePosition.add(position);
    }

    public List<Integer> getUnclickablePosition() {
        return unclickablePosition;
    }
    
    private class PopIconAdapter extends BaseAdapter {

        private String[] str;
        private int[] icons;
        private String count;

        public PopIconAdapter(String[] str, int[] icons,String count) {
            this.str = str;
            this.icons = icons;
            this.count =count;
        }

        public PopIconAdapter(String[] str) {
            // TODO Auto-generated constructor stub
            this.str = str;
        }

        @Override
        public int getCount() {
            return str.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            if (null == convertView) {
                holder = new ViewHolder();
                if (icons == null) {
                    if (type == 0) {
                        convertView = View.inflate(mContext, R.layout.list_item, null);
                        holder.content = (TextView) convertView
                                .findViewById(R.id.list_item_textview);
                    } else {
                        convertView = View.inflate(mContext, R.layout.feedback_list_item, null);
                        holder.content = (TextView) convertView
                                .findViewById(R.id.feedback_list_item_textview);
                    }
                } else {
                    if (count!=null&& !count.equals("")){
                        convertView = View.inflate(mContext, R.layout.list_icon_item, null);
                        holder.icon = (ImageView) convertView
                                .findViewById(R.id.titlebar_right_pop_icon);
                        holder.content = (TextView) convertView
                                .findViewById(R.id.titlebar_right_pop_content);
                        holder.number_img = (TextView) convertView
                                .findViewById(R.id.number_img);
                    }else {
                        convertView = View.inflate(mContext, R.layout.list_icon_item, null);
                        holder.icon = (ImageView) convertView
                                .findViewById(R.id.titlebar_right_pop_icon);
                        holder.content = (TextView) convertView
                                .findViewById(R.id.titlebar_right_pop_content);
                        holder.number_img = (TextView) convertView
                                .findViewById(R.id.number_img);
                    }
                }
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (icons != null) {
                holder.icon.setImageResource(icons[position]);
            }
            if(holder.number_img!=null){
            	if (count!=null && !count.equals("")&& str[position].equals("消息")){
            		holder.number_img.setVisibility(View.VISIBLE);
            		holder.number_img.setText(count+"");
            	}else{
            		holder.number_img.setVisibility(View.GONE);
            	}
            }
            holder.content.setText(str[position]);
            for (int i : unclickablePosition) {
                if (position == i) {
                    convertView.setEnabled(false);
                }
            }
            if (!unclickablePosition.contains(0) && position == 0) {
                convertView.setEnabled(true);
            }
            return convertView;
        }

    }

    static class ViewHolder {
        ImageView icon;
        TextView content,number_img;
    }
}
