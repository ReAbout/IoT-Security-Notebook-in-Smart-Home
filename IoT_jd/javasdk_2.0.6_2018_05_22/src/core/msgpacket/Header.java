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

import core.common.ByteConvert;
import core.common.ByteUtil;
import core.security.JoylinkEncrypt;


public class Header {
	//joylink2.0 网络通信字节流为小端模式
	public static final int LAN_MAGIC   = 0x123455BB;  //0x123455BB  lan
	public static final int CLOUD_MAGIC = 0x123455CC;  //0x123455CC  cloud
	public static final int HEADER_SIZE = 16;
	
	private static final int uEccBytes  = 20;
	
	private int		magic;
	private short	optLength;
	private short	payloadLength;

	private byte	version;
	private byte	type;
	private byte	total;
	private byte	index;

	private byte	enctype;
	private byte	reserved;
	private short	crc;
	
	public Header(){
		
	}
	
	public Header(int magic, short payloadLength){
		
	}
	public static void printfHeader(Header header){
		System.out.printf("magic 			%x\n", header.magic);
		System.out.printf("optLength 		%d\n", header.optLength);
		System.out.printf("payloadLength 	%d\n", header.payloadLength);
		System.out.printf("version 			%d\n", header.version);
		System.out.printf("type 			%d\n", header.type);
		System.out.printf("enctype 			%d\n", header.enctype);
		System.out.printf("crc 				%x\n", header.crc);
	}
	
	public Header(int magic, short payloadLength, short optLength, byte version,
				  byte type, byte index, byte enctype){
		
		this.magic = magic;
		if(enctype == JoylinkEncrypt.ET_ECDH){
			this.optLength = uEccBytes;
		}else{
			this.optLength = optLength;
		}
		this.payloadLength = payloadLength;
		this.version = version;
		this.type = type;
		this.total = 0;
		this.index = index;
		this.enctype = enctype;
		this.reserved = 0;
		this.crc = 0;
	}
	
	public int getMagic() {
		return magic;
	}
	public void setMagic(int magic) {
		this.magic = magic;
	}

	public short getOptLen() {
		return optLength;
	}
	public void setOptLen(short optLength) {
		this.optLength = optLength;
	}
	
	public short getPayloadLen() {
		return payloadLength;
	}
	public void setPayloadLen(short payloadLength) {
		this.payloadLength = payloadLength;
	}
	
	public byte getVersion() {
		return version;
	}
	public void setVersion(byte version) {
		this.version = version;
	}
	
	public byte getType() {
		return type;
	}
	public void setType(byte type) {
		this.type = type;
	}
	
	public byte getTotal() {
		return total;
	}
	public void setTotal(byte total) {
		this.total = total;
	}
	
	public byte getIndex() {
		return index;
	}
	public void setIndex(byte index) {
		this.index = index;
	}
	
	public byte getEnctype() {
		return enctype;
	}
	public void setEnctype(byte enctype) {
		this.enctype = enctype;
	}
	
	public byte getReserved() {
		return reserved;
	}
	public void setReserved(byte reserved) {
		this.reserved = reserved;
	}
	
	public short getCrc() {
		return crc;
	}
	public void setCrc(short crc) {
		this.crc = crc;
	}
	
	public static int parseHeader(byte[] buf, Header header) {
		int index = 0, ret = -1;
		
		header.setMagic(ByteUtil.intConvertLE((int)ByteConvert.bytesToUint(buf)));
		index += 4;
		
		header.setOptLen(ByteUtil.shortConvertLE((short)ByteConvert.bytesToUshort(buf, index)));
		index += 2;
		
		header.setPayloadLen(ByteUtil.shortConvertLE((short)ByteConvert.bytesToUshort(buf, index)));
		index += 2;
		
		header.setVersion(buf[index]);
		index += 1;
		
		header.setType(buf[index]);
		index += 1;
		
		header.setTotal(buf[index]);
		index += 1;
		
		header.setIndex(buf[index]);
		index += 1;
	
		header.setEnctype(buf[index]);
		index += 1;
		
		header.setReserved(buf[index]);
		index += 1;

		header.setCrc(ByteUtil.shortConvertLE((short)ByteConvert.bytesToUshort(buf, index)));
		index += 2;
		if(HEADER_SIZE == index){
			ret = 0;
		}
		return ret;
	}
	public static boolean isCloudHeader(Header header) {
		boolean ret = false;
		
		if(header.getMagic() == Header.CLOUD_MAGIC){
			ret = true;
		}
		return ret;
	}
	public static boolean isCloudHeader(byte[] buf) {
		int magic = 0;
		boolean ret = false;
		
		magic = ByteUtil.intConvertLE(ByteConvert.bytesToInt(buf));
		if(magic == Header.CLOUD_MAGIC){
			ret = true;
		}
		return ret;
	}
	
	public static boolean isLanHeader(Header header) {
		boolean ret = false;
		
		if(header.getMagic() == Header.LAN_MAGIC){
			ret = true;
		}
		return ret;
	}
	public static boolean isLanHeader(byte[] buf) {
		int magic = 0;
		boolean ret = false;
		
		magic = ByteUtil.intConvertLE(ByteConvert.bytesToInt(buf));
		
		if(magic == Header.LAN_MAGIC){
			ret = true;
		}
		return ret;
	}
	
	public static int packetHeader(Header header, byte[] buf) {
		int index = 0, ret = -1;
		
		ByteConvert.uintToBytes(header.getMagic(), buf, index);
		index += 4;
		
		ByteConvert.shortToBytes(header.getOptLen(), buf, index);
		index += 2;
		
		ByteConvert.shortToBytes(header.getPayloadLen(), buf, index);
		index += 2;
		
		ByteConvert.ubyteToBytes(header.getVersion(), buf, index);
		index += 1;
		
		ByteConvert.ubyteToBytes(header.getType(), buf, index);
		index += 1;
		
		ByteConvert.ubyteToBytes(header.getTotal(), buf, index);
		index += 1;
		
		ByteConvert.ubyteToBytes(header.getIndex(), buf, index);
		index += 1;
		
		ByteConvert.ubyteToBytes(header.getEnctype(), buf, index);
		index += 1;
		
		ByteConvert.ubyteToBytes(header.getReserved(), buf, index);
		index += 1;

		ByteConvert.shortToBytes(header.getCrc(), buf, index);
		index += 2;
		
		if(HEADER_SIZE == index){
			ret = 0;
		}
		return ret;
	}
	
	public int calculatePayloadPosition(byte[] buf, Header header){
		int position = 0;
		if(0 == Header.parseHeader(buf, header)){
			if(header.getEnctype() == JoylinkEncrypt.ET_ECDH){
				position = HEADER_SIZE + uEccBytes;
			}else{
				position = HEADER_SIZE;
			}
		}
		return position;
	}
	public static int calculateRecvBufSize(Header header){
		return header.getOptLen() + header.getPayloadLen();
	}
	
	public static void prasePacketTpye(byte type){
		switch(type){
		
			case Packet.PT_UNKNOWN:
				System.out.printf("Packet Type is unkown\n");
				break;
			case Packet.PT_SCAN:
				System.out.printf("Packet Type is PT_SCAN\n");
				break;
			case Packet.PT_WRITE_ACCESSKEY:
				System.out.printf("Packet Type is PT_WRITE_ACCESSKEY\n");
				break;
			case Packet.PT_JSONCONTROL:
				System.out.printf("Packet Type is PT_JSONCONTROL\n");
				break;
			case Packet.PT_SCRIPTCONTROL:
				System.out.printf("Packet Type is PT_SCRIPTCONTROL\n");
				break;
			case Packet.PT_OTA_ORDER:
				System.out.printf("Packet Type is PT_OTA_ORDER\n");
				break;
			case Packet.PT_OTA_UPLOAD:
				System.out.printf("Packet Type is PT_OTA_UPLOAD\n");
				break;
			case Packet.PT_AUTH:
				System.out.printf("Packet Type is PT_AUTH\n");
				break;
			case Packet.PT_BEAT:
				System.out.printf("Packet Type is PT_BEAT\n");
				break;
			case Packet.PT_SERVERCONTROL:
				System.out.printf("Packet Type is PT_SERVERCONTROL\n");
				break;
			case Packet.PT_UPLOAD:
				System.out.printf("Packet Type is PT_UPLOAD\n");
				break;
			case Packet.PT_TIME_TASK:
				System.out.printf("Packet Type is PT_TIME_TASK\n");
				break;
			case Packet.PT_MODEL_CODE:
				System.out.printf("Packet Type is PT_MODEL_CODE\n");
				break;
			case Packet.PT_SUB_AUTH:
				System.out.printf("Packet Type is PT_SUB_AUTH\n");
				break;
			case Packet.PT_SUB_LAN_JSON:
				System.out.printf("Packet Type is PT_SUB_LAN_JSON\n");
				break;
			case Packet.PT_SUB_LAN_SCRIPT:
				System.out.printf("Packet Type is PT_SUB_LAN_SCRIPT\n");
				break;
			case Packet.PT_SUB_ADD:
				System.out.printf("Packet Type is PT_SUB_ADD\n");
				break;
			case Packet.PT_SUB_HB:
				System.out.printf("Packet Type is PT_SUB_HB\n");
				break;
			case Packet.PT_SUB_CLOUD_CTRL:
				System.out.printf("Packet Type is PT_SUB_CLOUD_CTRL\n");
				break;
			case Packet.PT_SUB_UPLOAD:
				System.out.printf("Packet Type is PT_SUB_UPLOAD\n");
				break;
			case Packet.PT_SUB_UNBIND:
				System.out.printf("Packet Type is PT_SUB_UNBIND\n");
				break;
			default :
				System.out.printf("Packet Type is unkown\n");
		}
		
	}
}
