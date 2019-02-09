package com.jd.smartcloudmobilesdk.demo.scene;

import android.content.Context;
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
import com.jd.smartcloudmobilesdk.demo.scene.model.DeviceDes;
import com.jd.smartcloudmobilesdk.demo.scene.model.DeviceStream;

import java.text.DecimalFormat;
import java.util.List;

public class ResponseOptsAdapter extends SectionedBaseAdapter {
    private List<DeviceStream> list;
    private Context context;
    private boolean b = true;//模式
    private TextView item_choose;
    private String symbol;
    private String progressValue;//当前值
    private boolean customer_choose = false;//自定义模式
    private TextView sub_choose;//子项选中状态
    private TextView echo;
    private int seekbar_value;
    private DecimalFormat dFormat;

    /**
     * @return the echo
     */
    public TextView getEcho() {
        return echo;
    }

    /**
     * @param echo the echo to set
     */
    public void setEcho(TextView echo) {
        this.echo = echo;
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

    public View getItem_choose() {
        // TODO Auto-generated method stub
        return item_choose;
    }

    /**
     * @param item_choose the item_choose to set
     */
    public void setItem_choose(TextView item_choose) {
        this.item_choose = item_choose;
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

    public ResponseOptsAdapter(Context context) {
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

    /**
     * @return the symbol
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * @param symbol the symbol to set
     */
    public void setSymbol(String symbol) {
        this.symbol = symbol;
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

    @Override
    public View getItemView(int section, int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHoler;
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

        return convertView;
    }

    public void modelStatus(List<DeviceDes> descList) {
        for (int i = 0; i < descList.size(); i++) {
            descList.get(i).setStatus(false);
        }
        notifyDataSetChanged();
    }

    @Override
    public View getSectionHeaderView(int section, View convertView, ViewGroup parent) {
        final SectionViewHolder holder;
        if (convertView == null || convertView.getTag() == null) {
            holder = new SectionViewHolder();
            convertView = View.inflate(context, R.layout.item_response_opts, null);
            holder.title_margin = (RelativeLayout) convertView.findViewById(R.id.title_margin);
            holder.dc_opts_tv = (TextView) convertView.findViewById(R.id.dc_opts_tv);
            holder.customer = (CheckBox) convertView.findViewById(R.id.customer);
            holder.ll_opts = (LinearLayout) convertView.findViewById(R.id.ll_opts);

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
        holder.ll_opts.setVisibility(View.GONE);
        holder.customer.setVisibility(View.GONE);
        final DeviceStream data = list.get(section);

        holder.dc_opts_tv.setText(data.getStream_name());

        if (section == 0) {
            holder.title_margin.setPadding(0, 15, 0, 0);
            holder.title_margin.setVisibility(View.VISIBLE);
        } else {
            holder.title_margin.setVisibility(View.GONE);
            holder.title_margin.setPadding(0, 32, 0, 0);
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
                    holder.choose_value.setMax((int) (Float.parseFloat(data.getMax_value()) * 1000) - (int) (Float.parseFloat(data.getMin_value()) * 1000));
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
                    holder.choose_value.setMax((int) (Float.parseFloat(data.getMax_value())) - (int) (Float.parseFloat(data.getMin_value())));
                    if (data.getSymbol() != null) {
                        setSymbol(data.getSymbol());
                        holder.symbol.setText(data.getSymbol());
                        holder.first_value.setText((int) (Float.parseFloat(data.getMin_value())) + data.getSymbol());
                        holder.last_value.setText((int) (Float.parseFloat(data.getMax_value())) + data.getSymbol());
                    } else {
                        holder.first_value.setText((int) (Float.parseFloat(data.getMin_value())) + "");
                        holder.last_value.setText((int) (Float.parseFloat(data.getMax_value())) + "");
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
                    holder.choose_value.setProgress(0);
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
            holder.customer.setChecked(true);
            if (data.getDeviceDes() != null) {
                modelStatus(data.getDeviceDes());
            }
        } else {
            setCustomer_choose(false);
            holder.customer.setChecked(false);
        }
        holder.customer.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                if (buttonView.isChecked()) {
                    if (getSub_choose() != null) {
                        getSub_choose().setBackgroundResource(0);
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
                    holder.dc_opts_sub.setClickable(false);
                    holder.dc_opts_add.setClickable(false);
                    holder.choose_value.setEnabled(false);
                    holder.choose_value.setProgress(0);
                }
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
                                    holder.show_value.setText(data.getMin_value());
                                }
                                holder.choose_value.setProgress(0);
                                setProgressValue(data.getMin_value());
                            } else {
                                holder.show_value.setText(dFormat.format((float) ((int) (Float.parseFloat(data.getMin_value()) * 1000) + percent) / 1000) + "");
                                holder.choose_value.setProgress(holder.choose_value.getProgress() - 1);
                                setProgressValue(dFormat.format((float) ((int) (Float.parseFloat(data.getMin_value()) * 1000) + percent) / 1000) + "");
                            }
                        } else {
                            if ((int) Float.parseFloat(data.getMin_value()) + percent < (int) Float.parseFloat(data.getMin_value())) {
                                if (data.getMin_value().equals("0")) {
                                    holder.show_value.setText("0.000");
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
                                    holder.show_value.setText("0.000");
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
                        if (((int) Float.parseFloat(data.getMin_value()) + percent) < (int) Float.parseFloat(data.getMin_value())) {
                            holder.show_value.setText(data.getMin_value() + "");
                            setProgressValue(data.getMin_value());
                        } else {
                            holder.show_value.setText(((int) Float.parseFloat(data.getMin_value()) + percent) + "");
                            setProgressValue(((int) Float.parseFloat(data.getMin_value()) + percent) + "");
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
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }
        });

        return convertView;
    }

    static class ViewHolder {
        TextView dc_opts_tv, item_choose;
        RelativeLayout fc_item;
    }

    static class SectionViewHolder {
        TextView dc_opts_tv;
        CheckBox customer;
        LinearLayout ll_opts;
        TextView show_value;//选择值
        TextView symbol;
        TextView dc_opts_sub, dc_opts_add;
        SeekBar choose_value;
        TextView first_value, last_value;
        RelativeLayout title_margin;
    }

}
