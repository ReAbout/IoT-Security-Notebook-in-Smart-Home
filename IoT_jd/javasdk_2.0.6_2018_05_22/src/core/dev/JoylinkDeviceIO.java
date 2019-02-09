package core.dev;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import core.common.SingLog;

public class JoylinkDeviceIO{
	
	public static int control(JoylinkDeviceScriptControl sc) {
		/**
		 * TODO this is a io control for device
		 */
		System.out.printf("device io control\n");
		SingLog.log().info("=============device run io control =============\n");
		return 0;
	}
	
	public static String packageDeviceSnapShot(JoylinkDeviceScriptControl sc) {
		/**
		 * TODO this is a io control for device
		 * {"code":0,"streams":[{"stream_id":"power","current_value":"0"}]}:
		 */
		JSONObject jsonObject = new JSONObject();
		
		try {
			jsonObject.put("code", 0);

			JSONArray array = new JSONArray();
			JSONObject streamsObject = new JSONObject();

			streamsObject.put("stream_id", "power");
			streamsObject.put("current_value",
					Integer.toString(sc.getParamValue()));

			array.put(streamsObject);
			jsonObject.put("streams", array);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.printf("device snap shot json:%s\n", jsonObject.toString());
		
		return jsonObject.toString();
	}
}
