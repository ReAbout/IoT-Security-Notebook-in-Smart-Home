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

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Timer;
import java.util.TimerTask;

import core.common.ByteUtil;
import core.common.RetCode;
import core.common.SingLog;
import core.dev.DevInfo;
import core.msgpacket.Header;
import core.msgpacket.Msg;
import core.msgpacket.Packet;
import core.security.JoylinkCloudAuth;
import core.security.JoylinkEncrypt;
import core.dev.*;


public class Cloud{
	public static final int JL_CLOUD_CON_ST_ERROR      =-1;
    public static final int JL_CLOUD_CON_ST_INIT       = 0;
    public static final int JL_CLOUD_CON_ST_AUTH       = 1;
    public static final int JL_CLOUD_CON_ST_WORK       = 2;
   
    private static int st = JL_CLOUD_CON_ST_INIT;
    private static int delayCount = 0;

	public Cloud() {
	}
	private static Cloud single = null;
	public synchronized static Cloud getInstance() {
		if (single == null) { single = new Cloud();
		}
		return single;
	}
    
    private static Selector selector = null;  
    public static SocketChannel sc = null;  
    
    private static RetCode listenToCloud() {
    	RetCode ret = RetCode.ERROR;
    	try {
			while (selector.select() > 0) {
				
				for (final SelectionKey sk : selector.selectedKeys()) {
					
					selector.selectedKeys().remove(sk);
					if (sk.isReadable()) {
						Msg msg = new Msg();
						SocketChannel socketChannel = (SocketChannel)sk.channel();
//        				read header
						ByteBuffer buf1 = ByteBuffer.allocate(core.msgpacket.Header.HEADER_SIZE);
						int recvLength = socketChannel.read(buf1);
						if(recvLength <= 0){
							System.out.printf("[Cloud] it receives a zero data packet(header) or socket is error\n");
							break;
						}
						System.out.printf("cloud recv header buf length:%d\n", recvLength);
					    ByteUtil.printBytes(buf1.array());
						Header header = new Header();
				        if (Header.parseHeader(buf1.array(), header) != 0) {
				        	if(header.getMagic() != core.msgpacket.Header.CLOUD_MAGIC){
				        		System.out.printf("cloud magic error\n");
				        	}
				        	break;
				        }
				        Header.printfHeader(header);
				        
//        	        	read desc and body
				        ByteBuffer buf2 = ByteBuffer.allocate(Header.calculateRecvBufSize(header));
				        recvLength = socketChannel.read(buf2);
				        if(recvLength <= 0){
				        	System.out.printf("[Cloud] it receives a zero data packet(opt and payload) or socket is error\n");
							break;
				        }
				        
			    		byte[] buf = ByteUtil.byteCat(buf1.array(), buf2.array());
			    		System.out.printf("cloud ============recv buf===========length:" + buf.length + "\n");
				        ByteUtil.printBytes(buf);
				        
				        Packet.parseResponse(buf, msg);
				        ret = handleCloudData(msg);
				        if (RetCode.OK != ret) {
				        	break;
				        }
					}
				}
				if (ret != RetCode.OK) {
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
    	if (ret != RetCode.OK) {
			return RetCode.ERROR;
    	}
    	return RetCode.OK;
    }
    private static RetCode handleCloudData(Msg msg) {
    	
    	Header.printfHeader(msg.getHeader());
    	Header.prasePacketTpye((byte)msg.getHeader().getType());
    	
        switch (msg.getHeader().getType()){
        	case Packet.PT_AUTH:
        		/**
        		 * 认证之后必须发一次设备快照
        		 */
        		JoylinkCloudAuth.parsePacketResponse(msg.getMsgBody());
        		byte[] sessionKey = null;
        		sessionKey = JoylinkCloudAuth.getCloudAuthResponseSessionKey();
        		DevInfo.setSessionKey(sessionKey);
        		System.out.printf("AUTH response sessionKey is\n");
		        ByteUtil.printBytes(JoylinkCloudAuth.getCloudAuthResponseSessionKey());
		        System.out.printf("AUTH OK\n");
		        
		        byte[] upload = null;
				upload = JoylinkDeviceUpload.packageUploadRequest();
				
				byte[] upload_buf = null;
				upload_buf = JoylinkEncrypt.encryptCloudRequest((byte) Packet.PT_UPLOAD, upload);
				sendWithNoReply(upload_buf);
		        
        		Cloud.runTimer();
        		break;
         	case Packet.PT_BEAT:
        		JoylinkCloudHeartBeat heart = new JoylinkCloudHeartBeat();
        		JoylinkCloudHeartBeat.parseHeartBeatResponse(msg.getMsgBody(), heart);
        		break;
            case Packet.PT_MODEL_CODE:
                break;
            case Packet.PT_SERVERCONTROL:
            	JoylinkCloudProcess.joylinkCloudProcessCtrl(msg, sc);
                break;
            case Packet.PT_UPLOAD:
            	JoylinkDeviceUpload.parseUploadResponse(msg.getMsgBody());
                break;
            case Packet.PT_SUB_HB:
                break;
            case Packet.PT_SUB_UPLOAD:
                break;
            case Packet.PT_SUB_CLOUD_CTRL:
                break;
            case Packet.PT_SUB_UNBIND:
                break;
            case Packet.PT_OTA_ORDER:
                break;
            case Packet.PT_OTA_UPLOAD:
                break;
            case Packet.PT_TIME_TASK:
                break;
            default:
            	System.out.printf("Server cloud:" + "unknown msg.getHeader().getType\r\n");
                break;
        }
    	
    	return RetCode.OK;
    }
    public synchronized static void sendWithNoReply(byte[] buf) {
    	ByteBuffer bbuf = ByteBuffer.wrap(buf);
    	try {
			int len = sc.write(bbuf);
			System.out.printf("sendWithNoReply to cloud len:%d\n", len);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    public synchronized static Msg sendWithReply(byte[] buf) {
    	sendWithNoReply(buf);
    	return readAndParseData(10);
    }
    
    public static Msg readAndParseData(int timeout) {
		Msg msg = new Msg();
		ByteBuffer buf1 = ByteBuffer.allocate(core.msgpacket.Header.HEADER_SIZE);
		
		while(true) {  
            int readBytes = 0;
			try {
				readBytes = sc.read(buf1);
			} catch (IOException e) {
				e.printStackTrace();
			}  
            if(readBytes > 0) {  
                SingLog.getLogger().info("Server: readBytes = " + readBytes + "\n");  
                break;
            }  
        }

        Header header = new Header();
        if (0 != Header.parseHeader(buf1.array(), header)) {
        	SingLog.getLogger().severe("magic error");
        	return null;
        }
        
        ByteBuffer buf2 = ByteBuffer.allocate(Header.calculateRecvBufSize(header));
        while(true) {  
            int readBytes = 0;
			try {
				readBytes = sc.read(buf2);
			} catch (IOException e) {
				e.printStackTrace();
			}  
            if(readBytes > 0) {  
                System.out.printf("recv cloud data: readBytes = " + readBytes + "\n");  
                break;
            }  
        }
        byte[] buf = ByteUtil.byteCat(buf1.array(), buf2.array());
        System.out.printf("============recv buf===========length:%d\n", buf.length);
        ByteUtil.printBytes(buf);
        
        Packet.parseResponse(buf, msg);
        return msg;
	}
  
	private RetCode statusInit(DevInfo devInfo){
        RetCode ret = RetCode.ERROR;
     
        String url = DevInfo.getCloudUrl();
        short cloudPort = DevInfo.getCloudPort();
        if(url != null){
            SingLog.log().info("cloud:" + url + " port:" + cloudPort + "\n");
            int  connectFail = 0;
			try {
				selector = Selector.open();
		        InetSocketAddress isa = new InetSocketAddress(url, cloudPort);
		        sc = SocketChannel.open(isa);
		        sc.configureBlocking(false);
		        sc.register(selector, SelectionKey.OP_READ);
			} catch (Exception e) {
				e.printStackTrace();
				return RetCode.ERROR;
			}
            connectFail = 0;  
            if(connectFail == 1){
                st = JL_CLOUD_CON_ST_INIT;
                return RetCode.ERROR;
            }else{
                st = JL_CLOUD_CON_ST_AUTH;
                ret = RetCode.OK;
            }
        }

        return ret;
    }
	
	private RetCode auth(){	
		/**
		 * TODO just send auth packet
		 */
		byte[] sendBuf = null;
		sendBuf = JoylinkEncrypt.encryptCloudRequest((byte) Packet.PT_AUTH, null);
	
		sendWithNoReply(sendBuf);
		
        return RetCode.OK;
    }
	
	private static RetCode heartbeat(){
		byte[] heart = new byte[JoylinkCloudHeartBeat.CLOUD_HEART_BEAT_REQUEST_SIZE];
		JoylinkCloudHeartBeat.packageHeartBeatRequest(heart);
		
		byte[] sendBuf = null;
		sendBuf = JoylinkEncrypt.encryptCloudRequest((byte) Packet.PT_BEAT, heart);
		
		sendWithNoReply(sendBuf);
		byte[] upload = null;
		upload = JoylinkDeviceUpload.packageUploadRequest();
		
		byte[] upload_buf = null;
		upload_buf = JoylinkEncrypt.encryptCloudRequest((byte) Packet.PT_UPLOAD, upload);
		sendWithNoReply(upload_buf);
	
        return RetCode.OK;
    }
	public static void runTimer(){
		TimerTask task = new TimerTask() {  
            @Override  
            public void run() {  
                // task to run goes here  
            	if (st == JL_CLOUD_CON_ST_WORK) {
            		if (RetCode.OK == heartbeat()){
            			System.out.printf("send heartbeat to cloud OK\n");
            		} else {
            			System.out.printf("send heartbeat to cloud ERROR\n");
            			st = JL_CLOUD_CON_ST_ERROR;
            			System.out.printf("cancel task to send heartbeat\n");
            			this.cancel();
            		}
            	} else {
            		System.out.printf("cancel task to send heartbeat\n");
            		this.cancel();
            	}
            }  
        };  
        Timer heartTimer = new Timer();  
        long delay = 20 * 100;  
        long intevalPeriod = 20 * 1000;  
        // schedules the task to be run in an interval  
        heartTimer.scheduleAtFixedRate(task, delay, intevalPeriod);
	}
	
	public void proc(DevInfo devInfo) {
		
		TimerTask ErrorHandleTimerTask = new TimerTask() {  
			public void run(){
				if (st == JL_CLOUD_CON_ST_ERROR) {
					try {
						if(sc != null && sc.isOpen()){
							sc.close();
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
					st = JL_CLOUD_CON_ST_INIT;
				}
			}     
		};
//		handle error
		Timer errorTimer = new Timer();
		errorTimer.schedule(ErrorHandleTimerTask, 10 * 1000, 20 * 1000);

		while(true) {
			if(DevInfo.getFeedId() == null){
	            if(st != JL_CLOUD_CON_ST_INIT){
	                st = JL_CLOUD_CON_ST_INIT;
	            }
	            if (sc != null) {
	            	try {
	            		if(sc != null && sc.isOpen()){
							sc.close();
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
	            }
	        }else {
	        	switch(st){
	        		case JL_CLOUD_CON_ST_ERROR:
	        			
	        			break;
	        		case JL_CLOUD_CON_ST_INIT:
	        			if (RetCode.OK == statusInit(devInfo)) {
	        				st = JL_CLOUD_CON_ST_AUTH;
	        			} else {
	        				st = JL_CLOUD_CON_ST_ERROR;
	        				break;
	        			}
	        		case JL_CLOUD_CON_ST_AUTH:
	        			if (RetCode.OK == auth()) {
	        				st = JL_CLOUD_CON_ST_WORK;
	        			} else {
	        				st = JL_CLOUD_CON_ST_ERROR;
	        				break;
	        			}
	        			break;
	        		case JL_CLOUD_CON_ST_WORK:
	        			if (RetCode.OK != listenToCloud()) {
	        				st = JL_CLOUD_CON_ST_ERROR;
	        			}
	        			break;
	        		default:
	        			break;
	        	}
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
	}
}
