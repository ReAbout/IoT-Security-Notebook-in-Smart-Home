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

package core.security;

import java.util.Random;

import core.common.ByteConvert;
import core.common.ByteUtil;

public class JoylinkCloudAuth {
	
	public static int timestamp;
	public int random;
	private static byte[] sessionKey;
	
	private static final int CLOUD_AUTH_REQUEST_SIZE  = 8;
	private static final int CLOUD_AUTH_RESPONSE_SIZE = 32 + 8;
	
	public int getRandom(){
		return random;
	}
	public void setRandom(int random){
		this.random = random;
	}
	public static int getCloudAuthRequestSize() {
		return CLOUD_AUTH_REQUEST_SIZE;
	}
	public static int getCloudAuthResponseSize() {
		return CLOUD_AUTH_RESPONSE_SIZE;
	}
	public static byte[] getCloudAuthResponseSessionKey() {
		return sessionKey;
	}
	public static int setCloudAuthResponseSessionKey(byte[] key) {
		sessionKey = new byte[32];
		System.arraycopy(key, 0, sessionKey, 0, 32);
		return 0;
	}
	public JoylinkCloudAuth() {
	}
	public static int packageAuthPacketRequest(byte[] buf, JoylinkCloudAuth auth){
		
		int index = 0;
		
		int timestamp = ByteConvert.longToInt((int)System.currentTimeMillis());
		int random = new Random().nextInt();
		
		//int timestamp = 0x55667788;
		//int random = 0x11223344;
		auth.setRandom(random);
		
		ByteConvert.intToBytes(ByteUtil.intConvertLE(timestamp), buf, index);
		index += 4;
		
		ByteConvert.intToBytes(ByteUtil.intConvertLE(auth.getRandom()), buf, index);
		index += 4;
		
		System.out.printf("timestamp:%x random:%x\n",timestamp, random);
		return 0;
	}
	public static int parsePacketResponse(byte[] buf){
		/**
		 * timestamp & random & sessionKey
		 */
		byte[] key;
		key = ByteUtil.subBytes(buf, 8, 32);
		JoylinkCloudAuth.setCloudAuthResponseSessionKey(key);
		
		return 0;
	}
}
