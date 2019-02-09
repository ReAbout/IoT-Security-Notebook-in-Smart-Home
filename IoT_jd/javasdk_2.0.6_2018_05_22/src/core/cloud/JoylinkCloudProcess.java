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

import java.nio.channels.SocketChannel;

import core.common.SingLog;
import core.dev.JoylinkDeviceScriptControl;
import core.msgpacket.Msg;
import core.msgpacket.Packet;
import core.security.JoylinkEncrypt;
import core.dev.*;

public class JoylinkCloudProcess{
	public static final int JL_BZCODE_GET_SNAPSHOT 	= 1004;
	public static final int JL_BZCODE_CTRL 			= 1002;
	public static final int JL_BZCODE_UNBIND 		= 1060;
	
	public JoylinkCloudProcess() {
	}
    
	public static void joylinkCloudProcessCtrl(Msg msg, SocketChannel sc) {
		JoylinkDeviceScriptControl scriptControl = new JoylinkDeviceScriptControl();
		JoylinkCloudControl control = new JoylinkCloudControl();
		
		/**
		 * TODO null point, I will fix in some day
		 */
		if(msg.getMsgBody() == null){
			System.out.printf("Msg body is null, there is a error for data\n");
			return;
		}
		
		System.out.printf("Msg Body len:%d\n", msg.getMsgBody().length);
		JoylinkCloudControl.parseCloudControl(msg.getMsgBody(), control, "smart_cloud");
		
	    switch(control.bizCode){
	    	case JL_BZCODE_CTRL:
	    		JoylinkDeviceScriptControl.parseScriptRequest(control.getcmd(), scriptControl);
	    		/**
	    		 * package json data for device snap shot
	    		 */
	    		String snapShop;
	    		snapShop = JoylinkDeviceIO.packageDeviceSnapShot(scriptControl);
	    		control.setCmd(snapShop.getBytes());
	    		
	    		control.setTimeStamp((int)(System.currentTimeMillis()));
	    		if(control.bizCode == JL_BZCODE_GET_SNAPSHOT){
	    			control.setBizCode(104);
	    		}else if(control.bizCode == JL_BZCODE_CTRL){
	    			control.setBizCode(102);
	    		}
	    		byte[] buf = new byte[12 + snapShop.length()];
	    		JoylinkCloudControl.packageCloudControl(control, buf);
	    		byte[] encryptData = null;
	    		encryptData = JoylinkEncrypt.encryptCloudRequest((byte)Packet.PT_SERVERCONTROL,
	    											buf);
	    		Cloud.sendWithNoReply(encryptData);
	    		break;
	    	case JL_BZCODE_GET_SNAPSHOT:
	    		SingLog.log().info("bizCode snap shop\n");
	    		break;
	    	default:
	    		SingLog.log().info("bizCode unkown bizCode\n");
	    		break;
	    }

	}
}
