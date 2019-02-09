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

import core.common.ByteUtil;

public class Msg {
	Header header;
	byte[] opt;
	byte msgBody[];
	
	public Msg() {
		this.header = new Header();
	}
	public Msg(int magic, byte type) {
		this.header = new Header(ByteUtil.intConvertLE(magic), (short) 0,
				              (short) 0, (byte) 1, (byte) type, (byte) 0, (byte) 1);
	}
	
	public Header getHeader() {
		return header;
	}
	public void setHeader(Header header) {
		this.header = header;
	}
	public byte[] getOpt() {
		return opt;
	}
	public void setOpt(byte[] opt) {
		this.opt = opt;
	}
	
	public byte[] getMsgBody() {
		return msgBody;
	}
	public void setMsgBody(byte[] msgBody) {
		this.msgBody = msgBody;
	}
}
