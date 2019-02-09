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

package core.common;

public class Common {
	public static final String JOYLINK_VERSION = "2.0.0";

	public static final int LAN =0x00;
	public static final int CLOUD = 0x01;
	public static final int ALL = 0x02;
			
	/** 
     * 
     *  
     * @param buf 
     * @return 
     */  
    public static String parseByte2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;  
            }  
            sb.append(hex.toUpperCase());
        }  
        return sb.toString();
    }
  
    private static byte toByte(char c) {   
        byte b = (byte) "0123456789ABCDEF".indexOf(c);   
        return b;   
    }
    /** 
     * 
     *  
     * @param hexStr 
     * @return 
     */ 
	public static byte[] parseHexStr2Byte(String hexStr) {
		int len = (hexStr.length() / 2);   
	    byte[] result = new byte[len];   
	    char[] achar = hexStr.toCharArray();   
	    for (int i = 0; i < len; i++) {   
	     int pos = i * 2;   
	     result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));   
	    }   
	    return result;
    }
	
    public static String strFirstToUpper(String s){
        return s.replaceFirst(s.substring(0, 1), s.substring(0, 1).toUpperCase());
    }

}
