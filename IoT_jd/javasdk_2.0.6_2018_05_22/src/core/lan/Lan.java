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

package core.lan;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.util.Log;

import core.common.ByteUtil;
import core.common.RetCode;
import core.msgpacket.Header;
import core.msgpacket.Msg;
import core.msgpacket.Packet;
import core.security.JoylinkEncrypt;
import core.dev.*;

public class Lan {
	private static DatagramSocket server = null;
	private static DatagramPacket receivePack = null;
	private static byte[] buffer = null;
	
	private static boolean deviceBindStatus = false;
	
	private static List<HashMap> list;

	public Lan() {
	}

	private static Lan single = null;
	
	private static boolean getDeviceBindStatus() {
		return deviceBindStatus;
	}
	public static void setDeviceBindStatus(boolean status) {
		deviceBindStatus = status;
	}

	public synchronized static Lan getInstance() {
		if (single == null) {
			single = new Lan();
		}
		return single;
	}

	private static RetCode listenToLan() {
		RetCode ret = RetCode.ERROR;
		list = new ArrayList();
		int verify = 0;

		Log.d("joylink", "LAN listen socket start\n");

		while (true) {
			try {
				server.receive(receivePack);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Log.d("joylink", "Receive LAN data len\n");

			System.out.printf(
					"Receive LAN data len:%d IP adress:%s, port:%d\n",
					receivePack.getLength(), receivePack.getAddress()
							.toString().substring(1), receivePack.getPort());

			if (Header.isLanHeader(receivePack.getData()) != true) {
				System.out.printf("MAGIC ERROR\n");
				continue;
			}
			byte[] recvBuf = new byte[receivePack.getLength()];

			System.arraycopy(receivePack.getData(), 0, recvBuf, 0,
					receivePack.getLength());
			// System.out.printf("receive data is\n");
			// ByteUtil.printBytes(recvBuf);

			/**
			 * TODO A UDP packet size is smaller 1400 bytes, other case is not
			 * consider now.
			 */
			Header header = new Header();
			if (Header.parseHeader(receivePack.getData(), header) != 0) {
				if (header.getMagic() != core.msgpacket.Header.LAN_MAGIC) {
					System.out.printf("cloud magic error\n");
				}
				continue;
			}
			Header.printfHeader(header);
			Msg msg = new Msg();

			verify = Packet.parseResponse(receivePack.getData(), msg);
			String ipAddress = receivePack.getAddress().toString().substring(1);
			
			if(!verify){
				ret = handleLanData(msg, ipAddress, receivePack.getPort(), null);
				if (RetCode.OK != ret) {
					break;
				}
			}
		}
		return RetCode.OK;
	}

	private static RetCode handleLanData(Msg msg, String fromIpAdress, int port, String MAC) {

		// Header.printfHeader(msg.getHeader());
		Header.prasePacketTpye((byte) msg.getHeader().getType());
		String payload = null;
		byte[] sendBuf = null;
		
		/**
		 * TODO null point, I will fix in some day
		 */
		if(msg.getMsgBody() == null){
			System.out.printf("Msg body is null, there is a error for data\n");
			return RetCode.ERROR;
		}
		

		switch (msg.getHeader().getType()) {
		case Packet.PT_SCAN:
			if (JoylinkDeviceLanScan.parseLanScanRequest(msg.getMsgBody()) != 0) {
				//System.out.printf("PT_SCAN parse json error\r\n");
				Log.d("joylink", "PT_SCAN parse json error\n");
				break;
			}
			
			boolean bindStatus = getDeviceBindStatus();
			
			payload = null;
			payload = JoylinkDeviceLanScan.packageLanScanResponse(bindStatus);
			sendBuf = null;
			sendBuf = JoylinkEncrypt.encryptLANBasicData((byte) Packet.PT_SCAN,
					(int) JoylinkEncrypt.ET_NOTHING, payload.getBytes());
			sendWithNoReply(sendBuf, fromIpAdress, port);
			break;
		case Packet.PT_WRITE_ACCESSKEY:
			/*
			 * TODO factory must do because it is safe for customer
			 */
			String keyJson = null;
			keyJson = JoylinkDeviceLanWriteKey.readKeyFromfile();
			if (keyJson != null) {
				
				System.out.printf("===========The device has binded, please resetting device by physical method and delete joylink_lan.txt\r\n");
			
				payload = "{\"code\":-2001}";

				sendBuf = null;
				sendBuf = JoylinkEncrypt.encryptLANBasicData(
						(byte) Packet.PT_WRITE_ACCESSKEY,
						(int) JoylinkEncrypt.ET_NOTHING, payload.getBytes());
				sendWithNoReply(sendBuf, fromIpAdress, port);
				break;
			}
			
			if (false == JoylinkDeviceLanWriteKey
					.parseWriteKeyRequest(ByteUtil.subBytes(msg.getMsgBody(),
							4, msg.getMsgBody().length - 4), true)) {
				System.out.printf("PT_WRITE_ACCESSKEY verify failed\r\n");
				break;
			}
			/**
			 * TODO device is active
			 */
			payload = null;
			payload = JoylinkDeviceLanWriteKey.packageWriteKeyResponse();

			sendBuf = null;
			sendBuf = JoylinkEncrypt.encryptLANBasicData(
					(byte) Packet.PT_WRITE_ACCESSKEY,
					(int) JoylinkEncrypt.ET_NOTHING, payload.getBytes());
			sendWithNoReply(sendBuf, fromIpAdress, port);
			
			setDeviceBindStatus(true);
			/**
			 * TODO
			 */
			break;
		case Packet.PT_JSONCONTROL:
			/**
			 * TODO
			 */
			break;
		case Packet.PT_SCRIPTCONTROL:
			/**
			 * TODO
			 */
			JoylinkLanProcess.joylinkLanProcessCtrl(msg, fromIpAdress, port);
			break;
		case Packet.PT_MODEL_CODE:
			break;
		default:
			System.out.printf("Lan unknown msg.getHeader().getType:%d\r\n", msg
					.getHeader().getType());
			break;
		}

		return RetCode.OK;
	}

	public synchronized static void sendWithNoReply(byte[] buf, String IP,
			int port) {

		try {
			/**
			 * TODO Mobile phone receive UDP packets using port 4320 or 80
			 */
			DatagramPacket packet = new DatagramPacket(buf, buf.length,
					InetAddress.getByName(IP), port);

			packet.setData(buf);
			packet.setLength(buf.length);
			server.send(packet);
			System.out.printf("Lan sendWithNoReply :%d\n", buf.length);
			
			// ByteUtil.printBytes(buf);

			/*
			 * DatagramSocket socket = null; socket = new DatagramSocket();
			 * socket.setReuseAddress(true);
			 * 
			 * DatagramPacket packet = new DatagramPacket(buf, buf.length,
			 * InetAddress.getByName(IP), port); packet.setData(buf);
			 * packet.setLength(buf.length); socket.send(packet);
			 * System.out.printf("Lan sendWithNoReply :%d\n", buf.length);
			 * ByteUtil.printBytes(buf);
			 * 
			 * if (socket != null) { socket.close(); }
			 */
		} catch (Exception e) {
			e.printStackTrace();
			/*
			 * if (socket != null) { socket.close(); }
			 */
		}
	}

	private int statusInit() {
		buffer = new byte[1400];
		try {
			server = new DatagramSocket(4320);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			server.setBroadcast(true);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			server.close();
			e.printStackTrace();
		}
		try {
			server.setReuseAddress(true);
		} catch (SocketException e) {
			server.close();
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		receivePack = new DatagramPacket(buffer, buffer.length);

		Log.d("joylink", "LAN status init ok\n");
		return 0;
	}

	public void proc() {
		if (RetCode.E_OK == statusInit()) {
			Lan.listenToLan();
		}
		/**
		 * TODO add sleep 
		 */
		try { 
			 Thread.sleep(300); 
		} catch (InterruptedException e) { 
			 e.printStackTrace(); 
		}
	}
	public void updateHashMap() {
		
		list.iterator();
		HashMap map = new HashMap();
		map.put("1324","1");
		list.add(map);
		list.size();
		list.remove(0);
	}
	
	public void avoidRepeatAttack() {
		HashMap map = new HashMap();
		map.put("1324","1");
		list.add(map);
		list.size();
		list.remove(0);
		
	}
}
