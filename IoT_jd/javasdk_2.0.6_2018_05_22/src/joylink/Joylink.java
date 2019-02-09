package joylink;

import com.jd.mobile.joylink.SecurityService;
import com.jd.mobile.joylink.KeyGenerator;

import android.util.Log;
import core.auth.ECC;
import core.cloud.Cloud;
import core.dev.*;
import core.lan.Lan;

public class Joylink {
	private static DevInfo devInfo;
	private static Cloud cloud;
	private static Lan lan;
	private static ECC ecc;
	
	
	final static String joylinkJavaSdkVersion = "2.0.6";

	public static void test() {
		String keyJson = null;
		System.out.printf("Joylink Java SDK Version %s\n", joylinkJavaSdkVersion);
		
		devInfo = DevInfo.getInstance();
		cloud = Cloud.getInstance();
		ecc = ECC.getInstance();
		lan = Lan.getInstance();
		
		boolean deviceBindStatus = false;
		
		keyJson = JoylinkDeviceLanWriteKey.readKeyFromfile();
		if (keyJson != null) {
			deviceBindStatus = true;
			JoylinkDeviceLanWriteKey.parseWriteKeyRequest(keyJson.getBytes(), false);
		} else {
			deviceBindStatus = false;
			Log.d("joylink", "You must auth device frist !!!!!!!!\n");
			Log.d("joylink", "You must auth device frist !!!!!!!!\n");
			Log.d("joylink", "You must auth device frist !!!!!!!!\n");
		}
		
		lan.setDeviceBindStatus(deviceBindStatus);
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				Log.d("joylink", "run cloud pthread ....\n");
				cloud.proc(devInfo);
			}
		}).start();

		new Thread(new Runnable() {
			@Override
			public void run() {
				Log.d("joylink", "run Lan pthread ....\n");
				lan.proc();
			}
		}).start();
	}
}
