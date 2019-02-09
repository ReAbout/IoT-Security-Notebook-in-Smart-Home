package core.dev;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jd.mobile.joylink.KeyGenerator;
import com.jd.mobile.joylink.SecurityService;

import core.common.Common;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

public class JoylinkDeviceLanWriteKey{
	public static String readKeyFromfile(){
		String json = null;
	
		
		File file = new File(Environment.getExternalStorageDirectory()+"/lankey.txt");
    	try {
			FileInputStream fis = new FileInputStream(file);
			BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
			String msg = null;
			StringBuilder builder = new StringBuilder();
			while ((msg = reader.readLine())!=null) {
				builder.append(msg);
			}
			json = builder.toString();
			reader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	/**
    	 * TODO customer must decrypt lankey.txt
    	 */
		
		return json;
	}
	
	public static void writeKeyTofile(String json){
		File file = new File(Environment.getExternalStorageDirectory()+"/lankey.txt");
		Log.d("joylink", "writeKeyTofile path" + file.getAbsolutePath());
    	try {
			FileOutputStream fos = new FileOutputStream(file);
			OutputStreamWriter write = new OutputStreamWriter(fos);
			write.write(json);
			write.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	/**
    	 * TODO customer must encrypt lankey.txt
    	 */
	}
	
	public static boolean parseWriteKeyRequest(byte[] buf, boolean auth){
		
		boolean verify = false;
		
		/***********************************/
		/**
		 *TODO
		 */
		System.out.printf("write key json string : %s\n", new String(buf));
	
		/***********************************/
		try {
			JSONObject jsonObject = new JSONObject(new String(buf));
			String data = jsonObject.getString("data");

			JSONObject dataObject = new JSONObject(new String(data));

			String feedid = dataObject.optString("feedid");
			System.out.printf("feedid:%s\n", feedid);
			// update DevInfo.feedID
			DevInfo.setFeedId(feedid);

			String localKey = dataObject.getString("localkey");
			System.out.printf("localkey:%s\n", localKey);
			DevInfo.setLocalKey(localKey);

			String accessKey = dataObject.getString("accesskey");
			System.out.printf("accesskey:%s\n", accessKey);
			DevInfo.setAccessKey(accessKey);

			JSONArray serverArrayObject = dataObject
					.getJSONArray("joylink_server");

			for (int i = 0; i < serverArrayObject.length(); i++) {
				String value = serverArrayObject.getString(i);
				int index = value.indexOf(':');

				String url = value.substring(0, index);
				DevInfo.setCloudUrl(url);
				DevInfo.setCloudPort((short) Integer.parseInt(value
						.substring(index + 1)));
				System.out.printf("joylink_server:%s port:%d\n", url,
						Integer.parseInt(value.substring(index + 1)));
			}
			if(auth){
				String c_idt = dataObject.optString("c_idt");
				if(!TextUtils.isEmpty(c_idt)){
					JSONObject idtObject = new JSONObject(new String(c_idt));
					
					String cloudSignature = idtObject.optString("cloud_sig");
					if(!TextUtils.isEmpty(cloudSignature)){
						System.out.printf("cloudSignature:%s\n", cloudSignature);
						DevInfo.deviceIdt.setCloudSignature(Common.parseHexStr2Byte(cloudSignature));
						SecurityService s = new SecurityService();
						
						Log.d("joylink", "pubkey " + DevInfo.deviceIdt.getCloudPublicKey().getBytes() + "\n");
						Log.d("joylink", "random " + DevInfo.deviceIdt.getRandom().getBytes() + "\n");

						verify = s.verify(Common.parseHexStr2Byte(cloudSignature),
								Common.parseHexStr2Byte(DevInfo.deviceIdt.getCloudPublicKey()),
								DevInfo.deviceIdt.getRandom().getBytes());
						
						Log.e("JOYLINK","============= verify " + verify);
					}
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/**
		 * JoylinkDeviceLanWriteKey.writeKeyTofile is only for verify=ture
		 */
		if(verify){
			JoylinkDeviceLanWriteKey.writeKeyTofile(new String(buf));
		}
		/**
		 * I don't parse opt because it doesn't use now.
		 */
		return verify;
	}
	
	public static String packageWriteKeyResponse(){
		/**
		 * String data[] = "{\"code\":0}";
		 */
		String data = new String("{\"code\":0}");
		
		return data;
	}
}
