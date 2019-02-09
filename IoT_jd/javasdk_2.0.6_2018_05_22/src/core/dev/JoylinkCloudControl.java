package core.dev;

import java.util.HashMap;

import core.common.ByteConvert;
import core.common.ByteUtil;

public class JoylinkCloudControl {
	
	public int timestamp;
	public int bizCode;
	public int serial;
	public byte[] cmd;
	private static HashMap<String,Integer> map = new HashMap<String, Integer>();

	public int getTimeStamp() {
		return timestamp;
	}
	public void setTimeStamp(int timestamp) {
		this.timestamp = timestamp;
	}
	
	public int getBizCode() {
		return this.bizCode;
	}
	public void setBizCode(int bizCode) {
		this.bizCode = bizCode;
	}
	
	public int getSerial() {
		return serial;
	}
	public void setSerial(int serial) {
		this.serial = serial;
	}
	
	public byte[] getcmd() {
		return cmd;
	}
	public void setCmd(byte[] cmd) {
		this.cmd = cmd;
	}
	
	
	public static boolean parseCloudControl(byte[] buf, JoylinkCloudControl control, String IP) {
		/**
		 * timestamp & bizcode & serial & cmd
		 */
		boolean run = false;
		int index = 0;
		int timestamp = ByteUtil.intConvertLE((int)ByteConvert.bytesToInt(buf));
		control.setTimeStamp(timestamp);
		timestamp = timestamp & 0x7fffffff;
		index += 4;
		
		
		int bizCode = ByteUtil.intConvertLE(ByteConvert.bytesToInt(buf, index));
		index += 4;
		control.setBizCode(bizCode);
		//not conver
		int serial = ByteConvert.bytesToInt(buf, index);
		control.setSerial(serial);
		index += 4;
		
		/**
		 * TODO
		 */
		byte[] cmd = new byte[buf.length - index]; //string must have '\0' in the end
		System.arraycopy(buf, index, cmd, 0, buf.length - index); 
		control.setCmd(cmd);
		
		System.out.printf("parse cloud cmd script:%s, len:%d\n", new String(cmd), control.getcmd().length);
	   
		/**
		 * TODO
		 */
		if(IP.equalsIgnoreCase("smart_cloud")){
			return true;
		}
		int temp = 0;
		if(map.containsKey(IP)){
			temp = map.get(IP);
			if(timestamp > temp){
			//if(temp <= timestamp){
				map.put(IP, timestamp);
				run = true;
			}else{
				run = false;
				System.out.printf("timestamp is wrong\n");
			}
		}else{
			map = new HashMap<String, Integer>();
			map.put(IP, timestamp);
			run = true;
		}
		
		
		return run;
	}
	public static int packageCloudControl(JoylinkCloudControl control, byte[] buf) {
		/**
		 * package timestamp & bizcode & serial & snap shot json
		 */
		int index = 0;
		
		ByteConvert.intToBytes(ByteUtil.intConvertLE(control.getTimeStamp()), buf, index);
		index += 4;
		/**
		 * TODO
		 */
		
		ByteConvert.intToBytes(ByteUtil.intConvertLE(control.getBizCode()), buf, index);
		index += 4;
	
		ByteConvert.intToBytes(control.getSerial(), buf, index);
		index += 4;
		
		System.arraycopy(control.cmd, 0, buf, index, buf.length - index);
		System.out.printf("=============json:%s, len:%d\n", new String(control.cmd), control.getcmd().length);
		   
		
		return buf.length;
	}
}
