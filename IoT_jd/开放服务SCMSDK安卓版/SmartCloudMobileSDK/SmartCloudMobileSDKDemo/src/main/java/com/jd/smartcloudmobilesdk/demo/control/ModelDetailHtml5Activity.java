package com.jd.smartcloudmobilesdk.demo.control;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.jd.smartcloudmobilesdk.demo.R;
import com.jd.smartcloudmobilesdk.demo.control.view.JboxPopupMenu2;
import com.jd.smartcloudmobilesdk.demo.utils.DisplayUtils;

/**
 * Created by pengmin1 on 2017/3/8.
 *
 */
public class ModelDetailHtml5Activity extends Activity {
	private String feed_id, url;

	private WebView webView;
	// private WebViewJavascriptBridge bridge;
	private ImageView iMore;
	private TextView tvClose;
	private TextView tvTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_model_device_html5_detail);
		// 这里获得跳转的参数
		feed_id = getIntent().getExtras().getString("feed_id");
		url = getIntent().getExtras().getString("url");

		iMore = (ImageView) findViewById(R.id.i_more);
		tvClose = (TextView) findViewById(R.id.tv_close);
		tvTitle = (TextView) findViewById(R.id.tv_title);
		// 初始化webview
		webView = (WebView) this.findViewById(R.id.webView);
		// Html5Adapter h5a = new
		// Html5Adapter(ModelDetailHtml5Activity.this,feed_id,handler);
		// int si = android.os.Build.VERSION.SDK_INT;
		// Log.e("GAO", "si=" + si);
		// if ( si >= 11) {
		// try {
		// Log.e("GAO", "rm");
		// webView.removeJavascriptInterface("searchBoxJavaBridge_");
		// webView.removeJavascriptInterface("accessibility");
		// webView.removeJavascriptInterface("accessibilityTraversal");
		// } catch (Throwable e) {
		// Log.e("GAO", "rm error");
		// }
		// }

		// bridge = new WebViewJavascriptBridge(this, webView,
		// h5a.new UserServerHandler());

		Log.e("GAO", "url=" + url);

		// 取html模板，并加载进来 ; 取快照及合成在html中调js sdk来做
		loadHtml(url);

	}

	private void loadHtml(String url) {
		webView.setVerticalScrollBarEnabled(false);
		webView.getSettings().setAppCacheEnabled(false);
		webView.loadUrl(url);
		webView.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onReceivedTitle(WebView view, String title) {
				super.onReceivedTitle(view, title);
				if (!title.isEmpty()) {
					if (title.length() > 8) {
						title = title.substring(0, 8) + "...";
					}
					tvTitle.setText(title);
				}
			}
		});
	}

	public void back(View view) {

		// 回退,退出
		if (webView.canGoBack()) {
			webView.goBack();
		} else {
			finish();
		}
	}

	public void close(View view) {
		// 退出
		finish();
	}

	public void more(View view) {
		String[] imenuStrs = new String[] { "刷新" };// ,"举报"
		int[] iicons = new int[] { R.mipmap.refresh_gif, R.mipmap.report,
				R.mipmap.image_127_2x, R.mipmap.image_127_2x };
		int iwidth = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 130, getResources()
						.getDisplayMetrics());
		String inumber = "";
		/*
		 * if (count>99){ number ="99+"; }else{ if (count!=0){ number =count+"";
		 * }else{ number =""; } }
		 */
		final JboxPopupMenu2 ipopupMenu = new JboxPopupMenu2(this, imenuStrs,
				iicons, iwidth, inumber);

		int ih = 0;
		int iw = 0;
		if (DisplayUtils.getDisplayHeight() <= 1280) {
			ih = 118;
			iw = 10;
		} else if (DisplayUtils.getDisplayHeight() > 1280) {
			ih = 250;
			iw = 20;
		}
		ipopupMenu.showAtLocation(iMore, Gravity.RIGHT | Gravity.TOP, iw, ih);
		ipopupMenu.showAsDropDown(iMore, 0, 10);
		ipopupMenu
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						switch (position) {
						case 0:
							ipopupMenu.closePopupMenu();
							webView.reload();
							Log.e("GAO", "h5 menu 1");
							break;
						case 1:
							ipopupMenu.closePopupMenu();
							Log.e("GAO", "h5 menu 1");
							break;

						default:
							break;

						}
					}
				});
	}

	// public Handler handler = new Handler() {
	//
	// @Override
	// public void handleMessage(Message msg) {
	// // TODO 接收消息并且去更新UI线程上的控件内容
	// if (msg.what == 1) {
	// //msg.obj;
	// Log.e("GAO","new h5 handler: " + msg.obj);
	// // {"showShare":true,"showBack":true,"showMore":true}
	//
	// JSONObject jsonObject;
	// try {
	// jsonObject = new JSONObject(msg.obj.toString());
	// //iLeft.setVisibility(jsonObject.getBoolean("showBack")?View.VISIBLE:View.GONE);
	// //
	// iMore.setVisibility(jsonObject.optBoolean("showMore",true)?View.VISIBLE:View.GONE);
	//
	// } catch (JSONException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	// }
	// else if (msg.what == 2) {
	// JLog.e("GAO","main h5 handler 2: " + msg.obj);
	// if (webView.canGoBack()) {
	// tvClose.setVisibility(View.VISIBLE);
	// }
	// else{
	// tvClose.setVisibility(View.INVISIBLE);
	// }
	//
	// }
	// else if (msg.what == 5) {
	// JLog.e("GAO","main h5 handler 5: " + msg.obj);
	// String t = msg.obj.toString();
	// if(!t.isEmpty()){
	// // 首页
	// JLog.e("GAO","显示title");
	// //Show
	// if(t.length() > 8){
	// t = t.substring(0, 8) + "...";
	// }
	//
	// tvTitle.setText(t);
	// }
	//
	// }else if(msg.what == 6){
	// finish();
	// }
	// super.handleMessage(msg);
	// }
	// };

	@Override
	public void onBackPressed() {
		// 获取历史列表
		/*
		 * WebBackForwardList mWebBackForwardList =
		 * webView.copyBackForwardList(); String u =
		 * mWebBackForwardList.getCurrentItem().getUrl();//getOriginalUrl();
		 * Log.e("GAO","closeBP" + u + ",size=" +mWebBackForwardList.getSize());
		 * 
		 * int ic = mWebBackForwardList.getCurrentIndex(); if(ic >0){ String u2
		 * = mWebBackForwardList.getItemAtIndex(ic-1).getUrl();
		 * Log.e("GAO","closeBP2: " + u2 + "$$");
		 * 
		 * if(u2.equals(h5_url) || u2.equals(h5_url+"#")){ // 首页
		 * Log.e("GAO","显示BP首页2: " + u2); //Show
		 * iLeft.setVisibility(View.VISIBLE); iMore.setVisibility(View.VISIBLE);
		 * } }
		 * 
		 * Log.e("GAO","closeBP1: " + u + "$$"); if(u.equals(h5_url) ||
		 * u.equals(h5_url+"#")){ // 首页 Log.e("GAO","显示BP首页1: " + u); finish();
		 * }
		 */

		if (webView.canGoBack()) {
			webView.goBack();
		} else {
			super.onBackPressed();
		}
	}

}
