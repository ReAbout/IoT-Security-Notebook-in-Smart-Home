package core.dev;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JoylinkDeviceScriptControl {
	
	public static final int LIGHT_CMD_NONE  = -1;
	public static final int LIGHT_CMD_POWER = 1;
	
	public static final int LIGHT_CTRL_NONE = -1;
	public static final int LIGHT_CTRL_OFF	= 0;
	public static final int LIGHT_CTRL_ON 	= 1;
	
	static int cmd;
	static int paramValue;

	public int getCmd() {
		return cmd;
	}
	
	public int getParamValue() {
		return paramValue;
	}
	
	public static int packageScriptResponse(byte[] buf, JoylinkDeviceScriptControl sc){
		/**
		 * TODO
		 */
       
		return 0;
	}
	
	public static int parseScriptRequest(byte[] buf, JoylinkDeviceScriptControl sc){
		/**
		 * {"snapshot":[{"current_value":"0","stream_id":"power"}],"streams":[{
		 * "current_value":"0","stream_id":"power"}]}
		 */
		try {
			JSONObject jsonObject = new JSONObject(new String(buf));
		
			JSONArray streamsArrayObject = jsonObject.getJSONArray("streams");

			for (int i = 0; i < streamsArrayObject.length(); i++) {
				JSONObject obj = streamsArrayObject.getJSONObject(i);
				String streamId = obj.getString("stream_id");
				String currentValue = obj.getString("current_value");
				System.out.printf(
						"parse streams json stream_id:%s, current_value:%s\n",
						streamId, currentValue);

				if (streamId.equals("power")) {
					JoylinkDeviceScriptControl.cmd = LIGHT_CMD_POWER;
				} else {
					JoylinkDeviceScriptControl.cmd = LIGHT_CMD_NONE;
				}

				if (currentValue.equals("1")) {
					JoylinkDeviceScriptControl.paramValue = LIGHT_CTRL_ON;
				} else {
					JoylinkDeviceScriptControl.paramValue = LIGHT_CTRL_OFF;
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return JoylinkDeviceIO.control(sc);
	}
}
