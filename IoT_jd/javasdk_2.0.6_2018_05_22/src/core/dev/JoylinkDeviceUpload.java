package core.dev;

import core.common.ByteConvert;
import core.common.ByteUtil;

public class JoylinkDeviceUpload{
	
	public static int parseUploadResponse(byte[] buf){
		/**
		 * parse timestamp & code 
		 */
		int index = 0;
		
		int timestamp = (int)ByteConvert.bytesToUint(buf);
		index += 4;
		
		int code = (int)ByteConvert.bytesToUint(buf, index);
		index += 4;
		
		System.out.printf("recv cloud upload response timestamp:%x code:%x\n", timestamp, code);
		
		return 0;
	}
	
	public static byte[] packageUploadRequest(){
		int index = 0;
		JoylinkDeviceScriptControl sc = new JoylinkDeviceScriptControl();
		/**
		 * package json data for device snap shot
		 */
		String snapShop;
		snapShop = JoylinkDeviceIO.packageDeviceSnapShot(sc);
		
		byte[] buf = new byte[snapShop.getBytes().length + 4];

		ByteConvert.intToBytes((int)System.currentTimeMillis(), buf, index);
		index += 4;
		System.arraycopy(snapShop.getBytes(), 0, buf, index, snapShop.getBytes().length);  
		index += snapShop.getBytes().length;
		
		System.out.printf("cloud upload package request length:%d\n", buf.length);
	    ByteUtil.printBytes(buf);
	
		return buf;
	}
}
