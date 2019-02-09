/*Copyright (c) 2015-2050, JD Smart All rights reserved.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License. */

package core.cloud;  

import core.common.ByteConvert;
import core.common.ByteUtil;
import core.dev.*;

public class JoylinkCloudHeartBeat{
	public int timestamp; 
	public short verion;
	public short rssi;
	public static final int CLOUD_HEART_BEAT_REQUEST_SIZE = 8;
	public static final int CLOUD_HEART_BEAT_RESPONSE_SIZE = 8;
	
	public int code; 
	
	
	public int getTimeStamp() {
		return timestamp;
	}
	public void setTimeStamp(int timestamp) {
		this.timestamp = timestamp;
	}
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	
	
	public JoylinkCloudHeartBeat() {
	}
	
	public static int parseHeartBeatResponse(byte[] buf, JoylinkCloudHeartBeat heart){
		/**
		 * parse timestamp & code 
		 */
		ByteUtil.printBytes(buf);
		
		int index = 0;
		heart.setTimeStamp((int)ByteConvert.bytesToUint(buf));
		index += 4;
		
		heart.setCode((short)ByteConvert.bytesToUshort(buf, index));
		index += 4;
		
		System.out.printf("recv cloud heart beat response timestamp:%x code:%x\n", heart.getTimeStamp(),
					heart.getCode());
		return 0;
	}
	
	public static int packageHeartBeatRequest(byte[] buf){
		
		int index = 0;
		int timestamp = ByteConvert.longToInt((int)System.currentTimeMillis());
		
		ByteConvert.intToBytes(ByteUtil.intConvertLE(timestamp), buf, index);
		index += 4;
		
		ByteConvert.shortToBytes(ByteUtil.shortConvertLE(DevInfo.getVersion()), buf, index);
		index += 2;
		
		ByteConvert.shortToBytes(ByteUtil.shortConvertLE((short) 1), buf, index);
		index += 2;
		System.out.printf("cloud heart beat length:%d\n", buf.length);
	    ByteUtil.printBytes(buf);
		
		return index;
	}
}
