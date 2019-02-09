package com.jd.smartcloudmobilesdk.demo.ifttt;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.jd.smartcloudmobilesdk.demo.R;
import com.jd.smartcloudmobilesdk.demo.ifttt.model.DeviceDes;
import com.jd.smartcloudmobilesdk.demo.ifttt.model.DeviceStream;

import java.text.DecimalFormat;
import java.util.List;

public class DeviceOptsAdapter extends SectionedBaseAdapter {
    private List<DeviceStream> list;
    private Context context;
    private String choose = null;
    private boolean b;//模式
    private boolean customer_choose = false;//自定义模式
    private String progressValue;//当前值
    private TextView echo;
    private int seekbar_value;
    private TextView item_choose;
    private String symbol;//单位
    private TextView sub_choose;//子项选中状态
    private DecimalFormat dFormat;

    /**
     * @return the sub_choose
     */
    public TextView getSub_choose() {
        return sub_choose;
    }

    /**
     * @param sub_choose the sub_choose to set
     */
    public void setSub_choose(TextView sub_choose) {
        this.sub_choose = sub_choose;
    }

    /**
     * @return the customer_choose
     */
    public boolean isCustomer_choose() {
        return customer_choose;
    }

    /**
     * @param customer_choose the customer_choose to set
     */
    public void setCustomer_choose(boolean customer_choose) {
        this.customer_choose = customer_choose;
    }

    /**
     * @return the units
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * @param symbol the units to set
     */
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    /**
     * @return the item_choose
     */
    public TextView getItem_choose() {
        return item_choose;
    }

    /**
     * @param item_choose the item_choose to set
     */
    public void setItem_choose(TextView item_choose) {
        this.item_choose = item_choose;
    }

    /**
     * @return the progressValue
     */
    public String getProgressValue() {
        return progressValue;
    }

    /**
     * @param progressValue the progressValue to set
     */
    public void setProgressValue(String progressValue) {
        this.progressValue = progressValue;
    }

    /**
     * @return the b
     */
    public boolean isB() {
        return b;
    }

    /**
     * @param b the b to set
     */
    public void setB(boolean b) {
        this.b = b;
    }

    /**
     * @return the choose
     */
    public String getChoose() {
        return choose;
    }

    /**
     * @param choose the choose to set
     */
    public void setChoose(String choose) {
        this.choose = choose;
    }

    public DeviceOptsAdapter(Context context) {
        dFormat = new DecimalFormat("##0.000");
        this.context = context;
    }

    public void setList(List<DeviceStream> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public List<DeviceStream> getList() {
        return list;
    }

    public void setEcho(TextView textView) {
        // TODO Auto-generated method stub
        this.echo = textView;
    }

    public TextView getEcho() {
        // TODO Auto-generated method stub
        return echo;
    }

    @Override
    public Object getItem(int section, int position) {
        if (null == list) {
            return null;
        } else {
            List<DeviceDes> models = list.get(section).getDeviceDes();
            if (null == models) {
                return null;
            } else {
                if (position != -1) {
                    return models.get(position);
                } else {
                    return null;
                }
            }
        }

    }

    @Override
    public long getItemId(int section, int position) {
        return position;
    }

    @Override
    public int getSectionCount() {
        if (null == list) {
            return 0;
        } else {
            return list.size();
        }
    }

    @Override
    public int getCountForSection(int section) {
        if (null == list) {
            return 0;
        } else {
            List<DeviceDes> models = list.get(section).getDeviceDes();
            if (null == models) {
                return 0;
            } else {
                return list.get(section).getDeviceDes().size();
            }

        }
    }

    public void modelStatus(List<DeviceDes> dlist) {
        // TODO Auto-generated method stub
        for (int i = 0; i < dlist.size(); i++) {
            dlist.get(i).setStatus(false);
        }
        notifyDataSetChanged();
    }

    @Override
    public View getItemView(int section, int position, View convertView, ViewGroup parent) {
        ViewHolder viewHoler;
        if (convertView == null || convertView.getTag() == null) {
            viewHoler = new ViewHolder();
            convertView = View.inflate(context, R.layout.fragment_connect_item2, null);
            viewHoler.dc_opts_tv = (TextView) convertView.findViewById(R.id.item_name);
            viewHoler.item_choose = (TextView) convertView.findViewById(R.id.item_choose);
            viewHoler.fc_item = (RelativeLayout) convertView.findViewById(R.id.fc_item);
            convertView.setTag(viewHoler);
        } else {
            viewHoler = (ViewHolder) convertView.getTag();
        }
        setSub_choose(viewHoler.item_choose);
        DeviceDes data = list.get(section).getDeviceDes().get(position);
        if (data.getValue() != null) {
            viewHoler.dc_opts_tv.setText(data.getValue());
        } else {
            viewHoler.dc_opts_tv.setText(data.getDescription());
        }
        if (data.isStatus()) {
            viewHoler.item_choose.setBackgroundResource(R.mipmap.ico_ok_h);
        } else {
            viewHoler.item_choose.setBackgroundResource(0);
        }
        //		viewHoler.fc_item.setBackground(context.getResources().getDrawable(R.color.light_gray5));
        return convertView;
    }

    @Override
    public View getSectionHeaderView(int section, View convertView, ViewGroup parent) {
        final SectionViewHolder holder;
        if (convertView == null || convertView.getTag() == null) {
            holder = new SectionViewHolder();
            convertView = View.inflate(context, R.layout.dc_opts_item, null);
            holder.title_margin = (RelativeLayout) convertView.findViewById(R.id.title_margin);
            holder.dc_opts_tv = (TextView) convertView.findViewById(R.id.dc_opts_tv);
            holder.ll_opts = (LinearLayout) convertView.findViewById(R.id.ll_opts);
            holder.customer = (CheckBox) convertView.findViewById(R.id.customer);

            holder.tv_big = (TextView) convertView.findViewById(R.id.tv_big);
            holder.tv_equal = (TextView) convertView.findViewById(R.id.tv_equal);
            holder.tv_small = (TextView) convertView.findViewById(R.id.tv_small);

            holder.show_value = (TextView) convertView.findViewById(R.id.show_value);
            holder.symbol = (TextView) convertView.findViewById(R.id.show_symbol);

            holder.dc_opts_sub = (TextView) convertView.findViewById(R.id.dc_opts_sub);
            holder.dc_opts_add = (TextView) convertView.findViewById(R.id.dc_opts_add);
            holder.choose_value = (SeekBar) convertView.findViewById(R.id.choose_value);

            holder.first_value = (TextView) convertView.findViewById(R.id.first_value);
            holder.last_value = (TextView) convertView.findViewById(R.id.last_value);

            convertView.setTag(holder);
        } else {
            holder = (SectionViewHolder) convertView.getTag();
        }

        final DeviceStream data = list.get(section);
        if (section == 0) {
            holder.title_margin.setVisibility(View.VISIBLE);
            holder.title_margin.setPadding(0, 15, 0, 0);
        } else {
            holder.title_margin.setPadding(0, 32, 0, 0);
            holder.title_margin.setVisibility(View.GONE);
        }

        if (data.getValue_des() == null) {
            if (data.getDeviceDes() != null) {
                holder.ll_opts.setVisibility(View.GONE);
                holder.customer.setVisibility(View.GONE);
            } else {
                holder.ll_opts.setVisibility(View.VISIBLE);
                holder.customer.setVisibility(View.GONE);
                if (data.getMin_value() == null) {
                    data.setMin_value("0");
                }
                if (data.getMax_value() == null) {
                    data.setMax_value("0");
                }


                if (data.getValue_type().equals("float")) {
                    int max = 0;
                    max = (int) (Float.parseFloat(data.getMax_value()) * 1000) - (int) (Float.parseFloat(data.getMin_value()) * 1000);
                    holder.choose_value.setMax(max);
                    if (data.getSymbol() != null) {
                        setSymbol(data.getSymbol());
                        holder.symbol.setText(data.getSymbol());
                        holder.first_value.setText(data.getMin_value() + data.getSymbol());
                        holder.last_value.setText(data.getMax_value() + data.getSymbol());
                    } else {
                        holder.first_value.setText(data.getMin_value());
                        holder.last_value.setText(data.getMax_value());
                    }
                } else {
//					holder.choose_value.setMax(Integer.parseInt(data.getMax_value())-Integer.parseInt(data.getMin_value()));
                    holder.choose_value.setMax((int) Float.parseFloat(data.getMax_value()) - (int) Float.parseFloat(data.getMin_value()));
                    if (data.getSymbol() != null) {
                        setSymbol(data.getSymbol());
                        holder.symbol.setText(data.getSymbol());
                        holder.first_value.setText((int) Float.parseFloat(data.getMin_value()) + data.getSymbol());
                        holder.last_value.setText((int) Float.parseFloat(data.getMax_value()) + data.getSymbol());
                    } else {
                        holder.first_value.setText((int) Float.parseFloat(data.getMin_value()) + "");
                        holder.last_value.setText((int) Float.parseFloat(data.getMax_value()) + "");
                    }
                }

                if (getProgressValue() != null) {
                    if (!holder.customer.isChecked()) {
                        holder.customer.setChecked(true);
                    }

                    if (data.getValue_type().equals("float")) {
                        seekbar_value = (int) (Float.parseFloat(getProgressValue()) * 1000) - (int) (Float.parseFloat(data.getMin_value()) * 1000);
                    } else {
                        seekbar_value = (int) (Float.parseFloat(getProgressValue())) - (int) (Float.parseFloat(data.getMin_value()));
                    }
                    holder.choose_value.setProgress(seekbar_value);
                    holder.show_value.setText(getProgressValue());
                } else {
                    if (data.getValue_type().equals("float")) {
                        holder.show_value.setText("0.000");
                    } else {
                        holder.show_value.setText((int) (Float.parseFloat(data.getMin_value())) + "");
                    }
                }
            }
        } else {
            holder.ll_opts.setVisibility(View.GONE);
            holder.customer.setVisibility(View.INVISIBLE);
        }
        if (isCustomer_choose()) {
            if (getChoose() != null) {
                if (getChoose().equals(">")) {
                    if (!holder.customer.isChecked()) {
                        holder.customer.setChecked(true);
                    }
                    holder.tv_big.setTextColor(Color.WHITE);
                    holder.tv_equal.setTextColor(context.getResources().getColor(R.color.gray));
                    holder.tv_small.setTextColor(context.getResources().getColor(R.color.gray));
                    holder.tv_small.setBackgroundResource(R.drawable.text_corner2_h);
                    holder.tv_equal.setBackgroundResource(R.drawable.text_corner1_h);
                    holder.tv_big.setBackgroundResource(R.drawable.text_corner);
                    setChoose(">");
                } else if (getChoose().equals("==")) {
                    if (!holder.customer.isChecked()) {
                        holder.customer.setChecked(true);
                    }
                    setChoose("==");
                    holder.tv_equal.setTextColor(Color.WHITE);
                    holder.tv_big.setTextColor(context.getResources().getColor(R.color.gray));
                    holder.tv_small.setTextColor(context.getResources().getColor(R.color.gray));
                    holder.tv_small.setBackgroundResource(R.drawable.text_corner2_h);
                    holder.tv_big.setBackgroundResource(R.drawable.text_corner_h);
                    holder.tv_equal.setBackgroundResource(R.drawable.text_corner1);
                } else if (getChoose().equals("<")) {
                    if (!holder.customer.isChecked()) {
                        holder.customer.setChecked(true);
                    }
                    holder.tv_small.setTextColor(Color.WHITE);
                    holder.tv_equal.setTextColor(context.getResources().getColor(R.color.gray));
                    holder.tv_big.setTextColor(context.getResources().getColor(R.color.gray));
                    holder.tv_big.setBackgroundResource(R.drawable.text_corner_h);
                    holder.tv_equal.setBackgroundResource(R.drawable.text_corner1_h);
                    holder.tv_small.setBackgroundResource(R.drawable.text_corner2);
                    setChoose("<");
                }
            }
            holder.customer.setChecked(true);
            if (data.getDeviceDes() != null) {
                modelStatus(data.getDeviceDes());
            }
        } else {
            holder.customer.setChecked(false);
            notifyDataSetChanged();
        }
        holder.customer.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                if (buttonView.isChecked() && buttonView.isClickable()) {
                    //					setProgressValue(data.getMin_value());
                    if (getSub_choose() != null) {
                        getSub_choose().setBackgroundResource(0);
                    }
                    if (!holder.tv_big.isClickable()) {
                        holder.tv_big.setClickable(true);
                    }
                    if (!holder.tv_equal.isClickable()) {
                        holder.tv_equal.setClickable(true);
                    }
                    if (!holder.tv_small.isClickable()) {
                        holder.tv_small.setClickable(true);
                    }
                    if (!holder.dc_opts_sub.isClickable()) {
                        holder.dc_opts_sub.setClickable(true);
                    }
                    if (!holder.dc_opts_add.isClickable()) {
                        holder.dc_opts_add.setClickable(true);
                    }
                    if (!holder.choose_value.isEnabled()) {
                        holder.choose_value.setEnabled(true);
                    }
                    setCustomer_choose(true);
                    notifyDataSetChanged();
                } else {
                    setCustomer_choose(false);
                    holder.tv_big.setClickable(false);
                    holder.tv_equal.setClickable(false);
                    holder.tv_small.setClickable(false);
                    holder.dc_opts_sub.setClickable(false);
                    holder.dc_opts_add.setClickable(false);
                    //					holder.choose_value.setEnabled(false);
                    holder.choose_value.setProgress(0);
                }
            }
        });

        holder.tv_big.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (!holder.customer.isChecked()) {
                    holder.customer.setChecked(true);
                }
                holder.tv_big.setTextColor(Color.WHITE);
                holder.tv_equal.setTextColor(context.getResources().getColor(R.color.gray));
                holder.tv_small.setTextColor(context.getResources().getColor(R.color.gray));
                holder.tv_small.setBackgroundResource(R.drawable.text_corner2_h);
                holder.tv_equal.setBackgroundResource(R.drawable.text_corner1_h);
                holder.tv_big.setBackgroundResource(R.drawable.text_corner);
                setChoose(">");
            }
        });

        holder.tv_equal.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (!holder.customer.isChecked()) {
                    holder.customer.setChecked(true);
                }
                setChoose("==");
                holder.tv_equal.setTextColor(Color.WHITE);
                holder.tv_big.setTextColor(context.getResources().getColor(R.color.gray));
                holder.tv_small.setTextColor(context.getResources().getColor(R.color.gray));
                holder.tv_small.setBackgroundResource(R.drawable.text_corner2_h);
                holder.tv_big.setBackgroundResource(R.drawable.text_corner_h);
                holder.tv_equal.setBackgroundResource(R.drawable.text_corner1);

            }
        });

        holder.tv_small.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (!holder.customer.isChecked()) {
                    holder.customer.setChecked(true);
                }
                holder.tv_small.setTextColor(Color.WHITE);
                holder.tv_equal.setTextColor(context.getResources().getColor(R.color.gray));
                holder.tv_big.setTextColor(context.getResources().getColor(R.color.gray));
                holder.tv_big.setBackgroundResource(R.drawable.text_corner_h);
                holder.tv_equal.setBackgroundResource(R.drawable.text_corner1_h);
                holder.tv_small.setBackgroundResource(R.drawable.text_corner2);
                setChoose("<");
            }
        });

        holder.dc_opts_sub.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (!holder.customer.isChecked()) {
                    holder.customer.setChecked(true);
                }
                int percent;
                if (holder.choose_value.getProgress() >= 0) {
                    if (data.getValue_type().equals("float")) {
                        percent = holder.choose_value.getProgress() - 1;
                        if ((int) Float.parseFloat(data.getMax_value()) == 0) {//过滤掉数值不为小数
                            if (((int) (Float.parseFloat(data.getMin_value()) * 1000) + percent) < (int) (Float.parseFloat(data.getMin_value()) * 1000)) {
                                if (data.getMin_value().equals("0")) {
                                    holder.show_value.setText("0.000");
                                } else {
                                    holder.show_value.setText(dFormat.format(dFormat.format(data.getMin_value())));
                                }
                                holder.choose_value.setProgress(0);
                                setProgressValue(data.getMin_value());
                            } else {
                                holder.show_value.setText(dFormat.format((float) ((int) (Float.parseFloat(data.getMin_value()) * 1000) + percent) / 1000) + "");
                                holder.choose_value.setProgress(holder.choose_value.getProgress() - 1);
                                setProgressValue(dFormat.format((float) ((int) (Float.parseFloat(data.getMin_value()) * 1000) + percent) / 1000) + "");
                            }
                        } else {
                            if (((int) Float.parseFloat(data.getMin_value()) + percent) < (int) Float.parseFloat(data.getMin_value())) {
                                if (data.getMin_value().equals("0")) {
                                    holder.show_value.setText(dFormat.format(data.getMin_value()));
                                } else {
                                    holder.show_value.setText(data.getMin_value());
                                }
                                holder.choose_value.setProgress(0);
                                setProgressValue(data.getMin_value());
                            } else {
                                holder.show_value.setText(dFormat.format((float) ((int) (Float.parseFloat(data.getMin_value()) * 1000) + percent) / 1000) + "");
                                holder.choose_value.setProgress(holder.choose_value.getProgress() - 1);
                                setProgressValue(dFormat.format((float) ((int) (Float.parseFloat(data.getMin_value()) * 1000) + percent) / 1000) + "");
                            }
                        }
                    } else {
                        percent = holder.choose_value.getProgress() - 1;
                        if (percent + (int) Float.parseFloat(data.getMin_value()) < (int) Float.parseFloat(data.getMin_value())) {
                            holder.show_value.setText((int) Float.parseFloat(data.getMin_value()) + "");
                            holder.choose_value.setProgress(0);
                            setProgressValue((int) Float.parseFloat(data.getMin_value()) + "");
                        } else {
                            holder.show_value.setText(percent + (int) Float.parseFloat(data.getMin_value()) + "");
                            holder.choose_value.setProgress(holder.choose_value.getProgress() - 1);
                            setProgressValue(percent + (int) Float.parseFloat(data.getMin_value()) + "");
                        }
                    }
                } else {
                    holder.show_value.setText(0 + "");
                    setProgressValue(0 + "");
                    holder.choose_value.setProgress(0);
                }
            }
        });

        holder.dc_opts_add.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (!holder.customer.isChecked()) {
                    holder.customer.setChecked(true);
                }
                int percent;
                if (data.getValue_type().equals("float")) {
                    percent = holder.choose_value.getProgress() + 1;
                    if ((int) Float.parseFloat(data.getMax_value()) == 0) {
                        holder.choose_value.setProgress(holder.choose_value.getProgress() + 1);
                        holder.show_value.setText(dFormat.format((float) ((int) (Float.parseFloat(data.getMin_value()) * 1000) + percent) / 1000) + "");
                        setProgressValue(dFormat.format((float) ((int) (Float.parseFloat(data.getMin_value()) * 1000) + percent) / 1000) + "");
                    } else {
                        percent = holder.choose_value.getProgress() + 1;
                        holder.choose_value.setProgress(holder.choose_value.getProgress() + 1);
                        holder.show_value.setText(dFormat.format((float) ((int) (Float.parseFloat(data.getMin_value()) * 1000) + percent) / 1000) + "");
                        setProgressValue(dFormat.format((float) ((int) (Float.parseFloat(data.getMin_value()) * 1000) + percent) / 1000) + "");
                    }
                } else {
                    percent = holder.choose_value.getProgress() + 1;
                    if (percent + (int) Float.parseFloat(data.getMin_value()) > (int) Float.parseFloat(data.getMax_value())) {
                        holder.choose_value.setProgress(holder.choose_value.getProgress());
                        holder.show_value.setText((int) Float.parseFloat(data.getMax_value()) + "");
                        setProgressValue((int) Float.parseFloat(data.getMax_value()) + "");
                    } else {
                        holder.choose_value.setProgress(holder.choose_value.getProgress() + 1);
                        holder.show_value.setText(percent + (int) Float.parseFloat(data.getMin_value()) + "");
                        setProgressValue(percent + (int) Float.parseFloat(data.getMin_value()) + "");
                    }
                }
            }
        });

        holder.choose_value.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
                if (!holder.customer.isChecked()) {
                    holder.customer.setChecked(true);
                }
                if (data.getMin_value() != null) {
                    int percent;
                    if (data.getValue_type().equals("float")) {
                        percent = seekBar.getProgress();
                        if ((int) Float.parseFloat(data.getMax_value()) == 0) {
                            if (((int) (Float.parseFloat(data.getMin_value()) * 1000) + percent) < (int) (Float.parseFloat(data.getMin_value()) * 1000)) {
                                if (data.getMin_value().equals("0")) {
                                    holder.show_value.setText(dFormat.format(data.getMin_value()));
                                } else {
                                    holder.show_value.setText(data.getMin_value());
                                }
                                setProgressValue(data.getMin_value() + "");
                            } else {
                                holder.show_value.setText(dFormat.format((float) ((int) (Float.parseFloat(data.getMin_value()) * 1000) + percent) / 1000) + "");
                                setProgressValue(dFormat.format((float) ((int) (Float.parseFloat(data.getMin_value()) * 1000) + percent) / 1000) + "");
                            }
                        } else {
                            if ((int) Float.parseFloat(data.getMin_value()) + percent < (int) Float.parseFloat(data.getMin_value())) {
                                if (data.getMin_value().equals("0")) {
                                    holder.show_value.setText("0.000");
                                } else {
                                    holder.show_value.setText(data.getMin_value());
                                }
                                setProgressValue(data.getMin_value() + "");
                            } else {
                                holder.show_value.setText(dFormat.format((float) ((int) (Float.parseFloat(data.getMin_value()) * 1000) + percent) / 1000) + "");
                                setProgressValue(dFormat.format((float) ((int) (Float.parseFloat(data.getMin_value()) * 1000) + percent) / 1000) + "");
                            }
                        }
                    } else {
                        percent = seekBar.getProgress();
                        if ((int) Float.parseFloat(data.getMin_value()) + percent < (int) Float.parseFloat(data.getMin_value())) {
                            holder.show_value.setText(data.getMin_value() + "");
                            setProgressValue(data.getMin_value());
                        } else {
                            holder.show_value.setText((int) Float.parseFloat(data.getMin_value()) + percent + "");
                            setProgressValue((int) Float.parseFloat(data.getMin_value()) + percent + "");
                        }
                    }
                } else {
                    if (getProgressValue() != null) {
                        holder.choose_value.setProgress(seekbar_value);
                    } else {
                        holder.show_value.setText(0 + "");
                        setProgressValue(0 + "");
                        seekBar.setProgress(0);
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                // TODO Auto-generated method stub
            }
        });

        holder.dc_opts_tv.setText(data.getStream_name());
        return convertView;
    }

    static class ViewHolder {
        TextView dc_opts_tv, item_choose;
        RelativeLayout fc_item;
    }

    static class SectionViewHolder {
        TextView dc_opts_tv;
        TextView tv_big, tv_equal, tv_small;//逻辑运算符
        TextView show_value;//选择值
        TextView symbol;//选择值
        TextView dc_opts_sub, dc_opts_add;
        SeekBar choose_value;
        TextView first_value, last_value;
        LinearLayout ll_opts;
        CheckBox customer;
        RelativeLayout title_margin;
    }

}
