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
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import core.common.ByteUtil;

public class Test {

	public Test() {
	}
	
	/**
	 * @param args
	 * @throws InvalidAlgorithmParameterException 
	 * @throws NoSuchProviderException 
	 * @throws NoSuchAlgorithmException 
	 */
	public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException {
//		encrypt byte
		byte[] rawByte = { 0x61, 0x62, 0x63, 0x64, 0x65 };
		ByteUtil.printBytes(rawByte);
		byte[] password = {'4','e','f','3','e','3','d','6','3','a','5','2','3','8','e','2'};
		byte[] iv = {'1','d','5','3','8','f','b','0','4','7','f','b','d','a','1','d'};
		byte[] encryptByte = AES.encrypt(rawByte, password, iv);
		ByteUtil.printBytes(encryptByte);
//		decrypt byte
		byte[] decryptByte = AES.decrypt(encryptByte, password, iv);
		ByteUtil.printBytes(decryptByte);
		
//		crc
		byte[] crcData = { 0x7E, 0x00, 0x05, 0x60, 0x31, 0x32, 0x33 };
	    @SuppressWarnings("unused")
		int crc16Checksum = CRC.CRC16(crcData);
	    
//	    md5
	    byte[] data = {'a', 'b', 'c'};
	    @SuppressWarnings("unused")
		String md5Str = MD5.bytesToMD5(data);
	    
	    md5Str = MD5.strToMD5("abc");
	    
	    File file = new File("//192.168.211.130/share/lp");
	    md5Str = MD5.fileToMD5(file);
	    
//	    ECC
	    /*
		ECC.getSigInstans();
		ByteUtil.printBytes(ECC.getSigInstans().getPrivateKeyBytes(), 0);
		ByteUtil.printBytes(ECC.getSigInstans().getPublicKeyBytes(), 0);
		ECC.getSigInstans().generateShareKey(ECC.getSigInstans().getPublicKeyBytes());
		*/
	}
}
