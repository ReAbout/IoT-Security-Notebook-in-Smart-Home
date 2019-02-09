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

package core.auth;

import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;


import core.common.Common;

public class MD5 {

	public MD5() {
	}
	/**
	 * md5
	 * @param input
	 * @return
	 */
	public static String bytesToMD5(byte[] input) {
		String md5str = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] buff = md.digest(input);
			md5str = Common.parseByte2HexStr(buff);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return md5str;
	}
	
	/**
	 * md5
	 * @param str
	 * @return
	 */
	public static String strToMD5(String str) {
		byte[] input = str.getBytes();
		return bytesToMD5(input);
	}
	
	/**
	 *
	 * @param file
	 * @return
	 */
	public static String fileToMD5(File file) {
		if(file == null) {
			return null;
		}
		if(file.exists() == false) {
			return null;
		}
		if(file.isFile() == false) {
			return null;
		}
		FileInputStream fis = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			fis = new FileInputStream(file);
			byte[] buff = new byte[1024];
			int len = 0;
			while(true) {
				len = fis.read(buff, 0, buff.length);
				if(len == -1){
					break;
				}
				md.update(buff,0,len);
			}
			fis.close();
			return Common.parseByte2HexStr(md.digest());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
