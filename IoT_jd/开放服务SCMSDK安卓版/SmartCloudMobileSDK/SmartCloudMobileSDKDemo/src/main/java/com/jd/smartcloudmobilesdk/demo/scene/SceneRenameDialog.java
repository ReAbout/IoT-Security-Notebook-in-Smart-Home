package com.jd.smartcloudmobilesdk.demo.scene;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.jd.smartcloudmobilesdk.demo.R;

/**
 * 场景重命名
 * Created by yangchangan on 2017/6/29.
 */
public class SceneRenameDialog extends Dialog implements View.OnClickListener {

    private EditText et_name;
    private Context context;
    private String name;
    private RenameListener renameListener;

    public void setRenameListener(RenameListener renameListener) {
        this.renameListener = renameListener;
    }

    public void setName(String name) {
        this.name = name;
    }

    public interface RenameListener {
        void onRename(String name);
    }

    public SceneRenameDialog(Context context) {
        super(context, R.style.jdPromptDialog1);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.scene_modify_name_dialog);
        this.setCanceledOnTouchOutside(false);
        initView();
    }

    private void initView() {
        findViewById(R.id.tv_delete).setOnClickListener(this);
        findViewById(R.id.tv_sure).setOnClickListener(this);
        findViewById(R.id.tv_cancel).setOnClickListener(this);
        et_name = (EditText) findViewById(R.id.et_name);
        if (!TextUtils.isEmpty(name)) {
            et_name.setText(name);
            et_name.setSelection(name.length());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_sure:
                if (!TextUtils.isEmpty(et_name.getText().toString().trim())) {
                    if (renameListener != null) {
                        renameListener.onRename(et_name.getText().toString());
                    }
                    SceneRenameDialog.this.dismiss();
                } else {
                    Toast.makeText(context, "名字不能为空", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.tv_cancel:
                SceneRenameDialog.this.dismiss();
                break;
            case R.id.tv_delete:
                et_name.setText("");
                break;
        }
    }
}
