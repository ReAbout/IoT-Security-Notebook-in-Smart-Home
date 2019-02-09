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

import core.auth.AES;
import core.auth.ECC;
import core.common.ByteUtil;
import core.common.SingLog;
import core.dev.DevInfo;
import core.msgpacket.Header;
import core.msgpacket.Msg;
import core.msgpacket.Packet;

public class JoylinkDecrypt {
	public JoylinkDecrypt(byte[] buf, byte[] data) {
		
	}
	public int decryptSubDeviceData(byte[] buf,byte[] key, byte[] data) {
		/**
		 * TODO
		 */
		return 0;
	}
	public static int decryptCloudResponse(byte[] buf, Msg msg) {
		byte[] key = null;
		byte[] iv = null;
		byte[] decryptByte;
		SingLog.log().info("Cloud =====printf header ===== \n");
		Header.printfHeader(msg.getHeader());
		
		short optLength = 0;
		Header.parseHeader(buf, msg.getHeader());
		if( msg.getHeader().getOptLen() > 0){
			byte[] opt;
			optLength = msg.getHeader().getOptLen();
			opt = ByteUtil.subBytes(buf, Header.HEADER_SIZE, optLength);
			msg.setOpt(opt);
		}else{
			optLength = 0;
			msg.setOpt(null);
		}
		
		byte[] encryptByte;
		encryptByte = ByteUtil.subBytes(buf, optLength + Header.HEADER_SIZE, 
						msg.getHeader().getPayloadLen());
		
		switch(msg.getHeader().getType()){
			case Packet.PT_AUTH:
				String accessKey = DevInfo.getAccessKey();
				String keyString = accessKey.substring(0, 16);
				String ivString = accessKey.substring(16);
				decryptByte = AES.decrypt(encryptByte, keyString.getBytes(), ivString.getBytes());
				break;
			default:
				byte[] sessionKey = DevInfo.getSessionKey();
				key = ByteUtil.subBytes(sessionKey, 0, 16);
				iv = ByteUtil.subBytes(sessionKey, 16, 16);
				decryptByte = AES.decrypt(encryptByte, key, iv);
				break;
		}
		
		SingLog.log().info("Cloud =====after decrypt data ===== \n");
		ByteUtil.printBytes(decryptByte);
		msg.getHeader().setPayloadLen((short)decryptByte.length);
		msg.setMsgBody(decryptByte);
	
		return 0;
	}
	public static int decryptLANBasicData(byte[] buf, Msg msg) {
		/**
		 * TODO
		 */
		byte[] decryptByte = null;
		String keyString = null;
		String ivString = null;
		boolean verifyEncryptType = false;
		
		Header.parseHeader(buf, msg.getHeader());
		
		System.out.printf("decrypt lan data packet\n");
		//Header.printfHeader(msg.getHeader());
		//JoylinkEncrypt.printfEncryptType(msg.getHeader().getType());
		
		
		byte[] encryptByte;
		//encryptByte = ByteUtil.subBytes(buf, Header.HEADER_SIZE, 
						//msg.getHeader().getPayloadLen() + msg.getHeader().getOptLen());
		encryptByte = ByteUtil.subBytes(buf, Header.HEADER_SIZE + msg.getHeader().getOptLen(), 
				msg.getHeader().getPayloadLen());
		
		/**
		 * TODO verify encrypt type only in the lan
		 */
		
		switch(msg.getHeader().getType()){
			case Packet.PT_SCAN:
				if(JoylinkEncrypt.ET_NOTHING == msg.getHeader().getEnctype()){
					verifyEncryptType = true;
				}
				break;
			case Packet.PT_WRITE_ACCESSKEY:
				if(JoylinkEncrypt.ET_ECDH == msg.getHeader().getEnctype()){
					verifyEncryptType = true;
				}
				break;
			default:
				if(JoylinkEncrypt.ET_ACCESSKEYAES == msg.getHeader().getEnctype()){
					verifyEncryptType = true;
				}
				break;
		}
			
		if(verifyEncryptType == false){
			return -1;
		}
		
		switch(msg.getHeader().getEnctype()){
			case JoylinkEncrypt.ET_NOTHING:
				decryptByte = new byte[encryptByte.length];
				System.arraycopy(encryptByte, 0, decryptByte, 0, encryptByte.length);  
				decryptByte = encryptByte;
				break;
			case JoylinkEncrypt.ET_ECDH:
				byte[] shareKey = null;
				shareKey = ECC.getInstance().generateShareKey(msg.getOpt());
				byte[] keyByte = new byte[16];
				
				System.arraycopy(shareKey, 0, keyByte, 0, 16);
				byte[] ivByte = new byte[16];
				System.arraycopy(shareKey, 4, ivByte, 0, 16);
		
				decryptByte = AES.decrypt(encryptByte, keyByte, ivByte);
				break;
			case JoylinkEncrypt.ET_ACCESSKEYAES:
				String localKey = DevInfo.getLocalKey();
				keyString = localKey.substring(0, 16);
				ivString = localKey.substring(16);
				decryptByte = AES.decrypt(encryptByte, keyString.getBytes(), ivString.getBytes());
				break;
			case JoylinkEncrypt.ET_SESSIONKEYAES:
				break;
			case JoylinkEncrypt.ET_PSKAES:
				break;
			default :
				break;
		}
		
		if(decryptByte.length > 0){
			SingLog.log().info("LAN =====after decrypt data ===== \n");
			ByteUtil.printBytes(decryptByte);
			//update payload length
			msg.getHeader().setPayloadLen((short)decryptByte.length);

			byte[] body = null;
			body = ByteUtil.subBytes(decryptByte, 0, msg.getHeader().getPayloadLen());
			msg.setMsgBody(body);

			return 0;
		}else{
			SingLog.log().info("LAN =====after decrypt data failed ===== \n");
			return -1;
		}
	}
}
