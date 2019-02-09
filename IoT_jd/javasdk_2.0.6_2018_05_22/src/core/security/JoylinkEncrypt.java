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
import core.auth.CRC;
import core.auth.ECC;
import core.common.ByteConvert;
import core.common.ByteUtil;
import core.dev.DevInfo;
import core.msgpacket.Header;
import core.msgpacket.Packet;

public class JoylinkEncrypt {
	
	public static final byte ET_NOTHING 			= 0;
	public static final byte ET_PSKAES 				= 1;
	public static final byte ET_ECDH 				= 2;
	public static final byte ET_ACCESSKEYAES 		= 3;
	public static final byte ET_SESSIONKEYAES 		= 4;
	
	private long timestamp;
	
	public static void printfEncryptType(byte encryptType){
		switch(encryptType){
			case ET_NOTHING:
				System.out.printf("Encrypt Type is ET_NOTHING(no encrypt)\n");
				break;
			case ET_PSKAES:
				System.out.printf("Encrypt Type is ET_PSKAES(no encrypt)\n");
				break;
			case ET_ECDH:
				System.out.printf("Encrypt Type is ET_ECDH\n");
				break;
			case ET_ACCESSKEYAES:
				System.out.printf("Encrypt Type is ET_ACCESSKEYAES\n");
				break;
			case ET_SESSIONKEYAES:
				System.out.printf("Encrypt Type is ET_SESSIONKEYAES\n");
				break;
			default:
				System.out.printf("Encrypt Type is unknown\n");
				break;
		}
		
	}
	
	public long getTimestamp() {
		timestamp = System.currentTimeMillis();
		return timestamp;
	}
	 
	public void setTimestamp(long timestamp) {
		   this.timestamp = timestamp;
    }

	public JoylinkEncrypt() {
		/**
		 * TODO
		 */
	}
	
	public int encryptSubDeviceData(byte[] buf, int enctype, byte[] key, byte[] payload){
		
		/**
		 * TODO
		 */
		return 0;
	}
	public static byte[] encryptCloudRequest(byte type, byte[] payload){
		
		byte[] encryptByte;
		byte[] buf = null;
		
		Header header = new Header(ByteUtil.intConvertLE(core.msgpacket.Header.CLOUD_MAGIC), (short) 0,
				(short) 0, (byte) 1, type, (byte) 0, (byte) 1);
		switch(type){
			case Packet.PT_AUTH://AccessKey
				header.setEnctype(ET_ACCESSKEYAES);
				JoylinkCloudAuth auth = new JoylinkCloudAuth();
				
				//packet payload
				byte[] rawByte = new byte[JoylinkCloudAuth.getCloudAuthRequestSize()];
				JoylinkCloudAuth.packageAuthPacketRequest(rawByte, auth);
				
				//packet opt feedid and random
				String feedId = DevInfo.getFeedId();
				byte[] bufOpt;
				byte[] bufRandom = new byte[4];
				
				ByteConvert.intToBytes(ByteUtil.intConvertLE(auth.getRandom()), bufRandom, 0);
				bufOpt = ByteUtil.byteCat(feedId.getBytes(), bufRandom);
				
				header.setOptLen((short)ByteUtil.shortConvertLE((short)bufOpt.length));
			
				//encrypt payload AccessKey
				String accessKey = DevInfo.getAccessKey();
				String keyString = accessKey.substring(0, 16);
				String ivString = accessKey.substring(16);
				
				encryptByte = AES.encrypt(rawByte, keyString.getBytes(), ivString.getBytes());
				header.setPayloadLen((short) ByteUtil.shortConvertLE((short)encryptByte.length));
				
				//CRC
				byte[] buf1;
				buf1 = ByteUtil.byteCat(bufOpt, encryptByte);
				header.setCrc((short) ByteUtil.shortConvertLE(CRC.CRC16(buf1)));
				//packet header
				byte[] buf0 = new byte[Header.HEADER_SIZE];
				Header.printfHeader(header);
				Header.packetHeader(header, buf0);
				
				buf = ByteUtil.byteCat(buf0, buf1);
				
				System.out.printf("before encrypt buf length:%d\n", buf.length);
				System.out.printf("=====after encrypt data:%d =====\n", encryptByte.length);
				ByteUtil.printBytes(buf);
				break;
			default: //sessionKey. it don't have opt
				header.setEnctype(ET_SESSIONKEYAES);
				//encrypt payload SessionKEY
				byte[] key = null;
				byte[] iv = null;
				
				key = ByteUtil.subBytes(DevInfo.getSessionKey(), 0, 16);
				iv = ByteUtil.subBytes(DevInfo.getSessionKey(), 16, 16);
				
				encryptByte = AES.encrypt(payload, key, iv);
				//update payload length
				header.setPayloadLen(ByteUtil.shortConvertLE((short) encryptByte.length));
				//update CRC
				header.setCrc((short) ByteUtil.shortConvertLE(CRC.CRC16(encryptByte)));
				//packet header
				byte[] buf2 = new byte[Header.HEADER_SIZE];
				Header.packetHeader(header, buf2);
				buf = ByteUtil.byteCat(buf2, encryptByte);
				
				System.out.printf("=====after encrypt data =====\n");
				ByteUtil.printBytes(buf);
				System.out.printf("encrypt data bodylen:" + encryptByte.length + "\n");
				break;
		}
		return buf;
	}


	public static byte[] encryptLANBasicData(byte type, int enctype, byte[] payload) {
		byte[] encryptByte;
		byte[] buf = null;
		String keyString = null;
		String ivString = null;
		
		Header header = new Header(ByteUtil.intConvertLE(core.msgpacket.Header.LAN_MAGIC), (short) 0,
				(short) 0, (byte) 1, type, (byte) 0, (byte) enctype);
		switch(enctype){
			case ET_NOTHING://NO encrypt
			case ET_PSKAES:
				header.setOptLen(ByteUtil.shortConvertLE((short) 0));
				header.setPayloadLen(ByteUtil.shortConvertLE((short)payload.length));
				//update CRC check
				header.setCrc((short) ByteUtil.shortConvertLE(CRC.CRC16(payload)));
				//packet header
				byte[] buf1 = new byte[Header.HEADER_SIZE];
				Header.printfHeader(header);
				Header.packetHeader(header, buf1);
				
				//pakcet payload
				buf = ByteUtil.byteCat(buf1, payload);
				System.out.printf("===== ET_NOTHING org data =====\n");
				ByteUtil.printBytes(buf);
				break;
			case ET_ACCESSKEYAES://LocalKEY
				String localKey = DevInfo.getLocalKey();
				keyString = localKey.substring(0, 16);
				ivString = localKey.substring(16);
				encryptByte = AES.encrypt(payload, keyString.getBytes(), ivString.getBytes());
				
				header.setOptLen(ByteUtil.shortConvertLE((short) 0));
				header.setPayloadLen(ByteUtil.shortConvertLE((short)encryptByte.length));
				//update CRC
				header.setCrc((short) ByteUtil.shortConvertLE(CRC.CRC16(encryptByte)));
				//packet header
				byte[] buf2 = new byte[Header.HEADER_SIZE];
				Header.packetHeader(header, buf2);
				
				//pakcet payload
				buf = ByteUtil.byteCat(buf2, encryptByte);
				System.out.printf("===== ET_ACCESSKEYAES encrypt data =====\n");
				ByteUtil.printBytes(buf);
				break;
			case ET_ECDH: //opt length is not zero which size is 21 bytes. ShareKEY
				
				byte[] devPublicKey = new byte[21];
				System.arraycopy(ECC.getInstance().getPublicKeyBytes(), 0, devPublicKey, 0, 21);
				//update opt len
				header.setOptLen(ByteUtil.shortConvertLE((short) 21));
				
				byte[] data = ByteUtil.byteCat(devPublicKey, payload);
				byte[] shareKey = null;
				shareKey = ECC.getInstance().generateShareKey(ECC.getInstance().getPublicKeyBytes());
				keyString = shareKey.toString().substring(0, 16);
				ivString = shareKey.toString().substring(16);
				
				encryptByte = AES.encrypt(data, keyString.getBytes(), ivString.getBytes());
				//update payload length
				header.setPayloadLen((ByteUtil.shortConvertLE((short)encryptByte.length)));
				//update CRC
				header.setCrc((short) ByteUtil.shortConvertLE(CRC.CRC16(encryptByte)));
				
				//packet header
				byte[] buf3 = new byte[Header.HEADER_SIZE];
				Header.packetHeader(header, buf3);
				//pakcet payload
				buf = ByteUtil.byteCat(buf3, encryptByte);
				System.out.printf("===== ET_ECDH encrypt data =====\n");
				ByteUtil.printBytes(buf);
				break;
			default: 
				break;
		}
		return buf;
	}
}
