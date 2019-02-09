package core.dev;

import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;
import android.util.Log;
import core.auth.ECC;
import core.common.Common;

public class JoylinkDeviceLanScan{
	
	public static int parseLanScanRequest(byte[] buf){
		int ret = -1;
		/**
		 * json data is "{\"cmd\":2}", we don't care. 
		 */
		/**
		 * {"cmd":2,"uuid":"XXXXXX", "status":1 "idt":{"app_rand":"xxxxxx"}}
		 */
		try {
			JSONObject jsonObject = new JSONObject(new String(buf));
			String uuid = null;
			uuid = jsonObject.optString("uuid");
			if(!TextUtils.isEmpty(uuid)){
				System.out.printf("uuid:%s\n", uuid);
				// update identification uuid
				DevInfo.appIdt.setUuid(uuid);
			}
			
			int type = 0;
			type = jsonObject.optInt("status", 0);
			System.out.printf("type:%s\n", type);
			// update identification scan type
			DevInfo.appIdt.setScanType(type);
		
			String idt = null;
			idt = jsonObject.optString("idt");
			if(!TextUtils.isEmpty(idt)){
				JSONObject idtObject = new JSONObject(new String(idt));

				String app_rand = idtObject.optString("app_rand");
				System.out.printf("app_rand:%s\n", app_rand);
				// update identification app random
				DevInfo.appIdt.setRandom(app_rand);
			}
			
			ret = 0;

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ret;
	}
	
	public static String packageLanScanResponse(boolean deviceBindStatus){
		/**
		 * TODO this is a io control for device
		 */
		
		JSONObject jsonObject = new JSONObject();
		
        try {
			jsonObject.put("code", 0);
			jsonObject.put("msg", "scan ok");
	        jsonObject.put("mac", DevInfo.mac);
	        jsonObject.put("productuuid", DevInfo.uuid);
	        jsonObject.put("feedid", DevInfo.getFeedId());
	        jsonObject.put("devkey", Common.parseByte2HexStr(ECC.getInstance().getPublicKeyBytes()));
	        jsonObject.put("lancon", DevInfo.lancon);
	        jsonObject.put("devtype", DevInfo.devType);
	        jsonObject.put("trantype", DevInfo.cmdTranType);
	        jsonObject.put("CID", "");
	        jsonObject.put("firmwareVersion", "");
	        jsonObject.put("modelCode", "");
	        
	        /**
	         * TODO
	         */
	    	//if(DevInfo.deviceIdt.getRandom() == null && (DevInfo.getAccessKey() == null)){
	        if(!deviceBindStatus){
		        if(DevInfo.deviceIdt.getRandom() == null){
		    		long t = System.currentTimeMillis();
			        Random rd = new Random(t);
			        byte[] rondom = new byte[32];
			        rd.nextBytes(rondom);
			        String randomValue;
			        randomValue = Common.parseByte2HexStr(rondom);
			        DevInfo.deviceIdt.setRandom(randomValue.substring(32));
		    	}
	    	
	    		JSONObject idtObject = new JSONObject();
		    	
		    	idtObject.put("t", 0);
		    	idtObject.put("d_p", Common.parseByte2HexStr(ECC.getInstance().getPublicKeyBytes()));
		    	idtObject.put("d_s", "");
		    	idtObject.put("f_p", "");
		    	idtObject.put("f_s", "");
		    	
		    	idtObject.put("d_r", DevInfo.deviceIdt.getRandom());
		    	idtObject.put("d_as", "");
		    	idtObject.put("d_s", "");
		    	
			    jsonObject.put("d_idt", idtObject);
	    	}
	        
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
		//System.out.printf("Lan scan response json is:%s, length:%d\n", jsonObject.toString(), jsonObject.toString().length());
		//Log.e("JOYLINK","json \n"+jsonObject.toString());
		
		return jsonObject.toString();
		
	}
}
