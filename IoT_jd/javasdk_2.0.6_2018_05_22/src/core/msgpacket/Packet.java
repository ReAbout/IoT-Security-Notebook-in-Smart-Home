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

package core.msgpacket;

import core.auth.CRC;
import core.common.*;
import core.security.JoylinkDecrypt;

public class Packet {
	public static final int REV_ST_HEADER     = 1;
	public static final int REV_ST_OPT        = 2;
	public static final int REV_ST_PAYLOAD    = 3;
	public static final int REV_ST_ERROR 	  = 4;
	public static final int REV_ST_END		  = 5;

	public static final int	PT_UNKNOWN 		  = 0;
	public static final int	PT_SCAN 		  = 1;
	public static final int	PT_WRITE_ACCESSKEY= 2;
	public static final int	PT_JSONCONTROL   =  3;
	public static final int	PT_SCRIPTCONTROL =  4;

	public static final int	PT_OTA_ORDER =      7;
	public static final int PT_OTA_UPLOAD =     8;
	public static final int PT_AUTH =           9;
	public static final int	PT_BEAT =           10;
	public static final int	PT_SERVERCONTROL =  11;
	public static final int	PT_UPLOAD =         12;
	public static final int	PT_TIME_TASK =      13;
	public static final int	PT_MODEL_CODE =     17;

	public static final int	PT_SUB_AUTH =       102;
	public static final int	PT_SUB_LAN_JSON =   103;
	public static final int	PT_SUB_LAN_SCRIPT = 104;
	public static final int	PT_SUB_ADD =        105;
	public static final int	PT_SUB_HB  =        110;
	public static final int	PT_SUB_CLOUD_CTRL = 111;
	public static final int	PT_SUB_UPLOAD =     112;
	public static final int	PT_SUB_UNBIND =     113;

	/**
	 * packet
	 * @param
	 * @param
	 * @return
	 */
	public static int packetRequest(Msg msg, byte[] buf) {
		SingLog.log().info("===============packet===============\n");
		int index = 0;
		
		//MAGIC has not been set because it has been set by creating Header.
		msg.getHeader().setCrc(ByteUtil.shortConvertLE((short) CRC.CRC16(msg.getMsgBody())));
		msg.getHeader().setOptLen(ByteUtil.shortConvertLE((short)msg.opt.length));
		msg.getHeader().setPayloadLen((ByteUtil.shortConvertLE((short)msg.getMsgBody().length)));
		
		//packet header
		byte[] header = new byte[Header.HEADER_SIZE];
		Header.packetHeader(msg.getHeader(), header);
		
		ByteUtil.bytesCopy(header, 0, buf, index, Header.HEADER_SIZE);
		index += Header.HEADER_SIZE;
		
		//packet opt
		if(msg.getHeader().getOptLen() > 0){
			ByteUtil.bytesCopy(msg.getOpt(), 0, buf, index, msg.getOpt().length);
			index += msg.getOpt().length;	
		}
		//packet payload
		ByteUtil.bytesCopy(msg.getMsgBody(), 0, buf, index, msg.getMsgBody().length);
		index += msg.getMsgBody().length;	

		return index;
	}

	public static int parseResponse(byte[] buf, Msg msg) {
		int st = REV_ST_HEADER;
		int ret = 0;
		do{
			switch(st){
				case REV_ST_HEADER:
					if (Header.parseHeader(buf, msg.getHeader()) == 0){
						if(msg.getHeader().getMagic() == Header.CLOUD_MAGIC 
								|| msg.getHeader().getMagic() == Header.LAN_MAGIC){
							if(msg.getHeader().getOptLen() > 0){
								st = REV_ST_OPT;
								break;
							}else if(msg.getHeader().getPayloadLen() > 0){
								st = REV_ST_PAYLOAD;
								break;
							}
						}else{
							st = REV_ST_ERROR;
						}
					} else {
						st = REV_ST_ERROR;
						SingLog.getSingLog().error("Magic error\n");
					}
					break;
				case REV_ST_OPT:
					byte[] opt = new byte[msg.getHeader().getOptLen()];
					System.arraycopy(buf, Header.HEADER_SIZE, opt, 0, msg.getHeader().getOptLen());
					msg.setOpt(opt);
					st = REV_ST_PAYLOAD;
					break;
				case REV_ST_PAYLOAD:
//					crc include opt and payload ?
					byte[] checker;
					checker = ByteUtil.subBytes(buf, Header.HEADER_SIZE,
							msg.getHeader().getOptLen() + msg.getHeader().getPayloadLen());
					
					int crc = CRC.CRC16(checker);
					if (msg.getHeader().getCrc() != CRC.CRC16(checker)) {
						System.out.printf("checker CRC:%x Header.crc:%x ERROR\n", crc, msg.getHeader().getCrc());
						st = REV_ST_ERROR;
						break;
					}
					
					if(Header.isCloudHeader(msg.getHeader())){
						JoylinkDecrypt.decryptCloudResponse(buf, msg);
					}
					if(Header.isLanHeader(msg.getHeader())){
						ret = JoylinkDecrypt.decryptLANBasicData(buf, msg);
					}
					st = REV_ST_END;
					break;
				default:
					break;
			}
		}while(st != REV_ST_END && st != REV_ST_ERROR);
		return ret;
	}
}
