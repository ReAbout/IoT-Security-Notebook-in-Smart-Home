package com.jd.smartcloudmobilesdk.demo.control.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.jd.smartcloudmobilesdk.demo.R;
import com.jd.smartcloudmobilesdk.demo.control.utils.HtmlTitleMappings;
import com.jd.smartcloudmobilesdk.demo.utils.DisplayUtils;

/**
 * Created by pengmin1 on 2017/3/8.
 *
 */
public class HtmlTitleButton extends FrameLayout {
	private static final String TAG = "HtmlTitleButton";
	private TextView mTextView;
	private ImageView mImageView;
	private int mChildPaddingBottom;
	private int mChildPaddingTop;
	private int mChildPaddingLeft;
	private int mChildPaddingRight;
	private View mNativeView;
	private String mImageKey;
	
	public HtmlTitleButton(Context context) {
		super(context);
		init();
	}

	public HtmlTitleButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		final TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.HtmlTitleButton);
		mChildPaddingBottom = (int) attributes.getDimension(R.styleable.HtmlTitleButton_child_paddingbottom, 0);
		mChildPaddingTop = (int) attributes.getDimension(R.styleable.HtmlTitleButton_child_paddingtop, 0);
		mChildPaddingLeft = (int) attributes.getDimension(R.styleable.HtmlTitleButton_child_paddingleft, 0);
		mChildPaddingRight = (int) attributes.getDimension(R.styleable.HtmlTitleButton_child_paddingright, 0);
		attributes.recycle();
	}
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		init();
	}
	private void init(){
		int count = getChildCount();
		if(count>0){
			mNativeView = getChildAt(0);
		}
		mTextView = new TextView(getContext());
		mTextView.setPadding(DisplayUtils.dip2px(getContext(),15.5f), mChildPaddingTop, DisplayUtils.dip2px(getContext(),15.5f), mChildPaddingBottom);
		
		
		mImageView = new ImageView(getContext());
		mImageView.setPadding(mChildPaddingLeft, mChildPaddingTop, mChildPaddingRight, mChildPaddingBottom);
		
		LayoutParams params1 = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		params1.gravity = Gravity.CENTER;
		mTextView.setGravity(Gravity.CENTER);
		mTextView.setTextColor(Color.parseColor("#333333"));
		addView(mTextView, params1);

		LayoutParams params2 = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		params2.gravity = Gravity.CENTER;
		addView(mImageView, params2);
		
		setNativeViewVisibility(View.GONE);
		setImageViewVisibility(View.GONE);
		setTextViewVisibility(View.GONE);
	}
	
	public void setText(String text){
		mTextView.setText(text);
		setImageViewVisibility(View.GONE);
		setTextViewVisibility(View.VISIBLE);
		setNativeViewVisibility(View.GONE);
	}
	/***
	 * see {@link HtmlTitleMappings} constant {@link } and so on
	 * @param key
	 * @return
	 */
	public void setImage(String key){
		mImageKey = key;
		if(HtmlTitleMappings.KEY_SETTING.equals(key)&&mNativeView!=null){
			setNativeViewVisibility(View.VISIBLE);
			setImageViewVisibility(View.GONE);
			setTextViewVisibility(View.GONE);
		}else{
			mImageView.setImageResource(HtmlTitleMappings.getDrawable(key));
			setImageViewVisibility(View.VISIBLE);
			setTextViewVisibility(View.GONE);
			setNativeViewVisibility(View.GONE);
		}
	}
	public String getImageKey(){
		return mImageKey;
	}
//	@Override
//	public void setOnClickListener(OnClickListener l) {
//		super.setOnClickListener(l);
//		if(mTextView.getVisibility()==View.VISIBLE){
//			mTextView.setOnClickListener(l);
//		}else{
//			mImageView.setOnClickListener(l);
//		}
//	}
	
	public void setImageViewVisibility(int visibility){
		mImageView.setVisibility(visibility);
	}
	
	public void setTextViewVisibility(int visibility){
		mTextView.setVisibility(visibility);
	}
	
	public void setNativeViewVisibility(int visibility){
		if(mNativeView!=null){
			mNativeView.setVisibility(visibility);
			if(visibility == View.VISIBLE){
				setImageViewVisibility(View.GONE);
				setTextViewVisibility(View.GONE);
			}
		}
	}
}
