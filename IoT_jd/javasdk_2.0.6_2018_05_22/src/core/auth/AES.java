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

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES
 * 
 */
public class AES {
	
    private static final String CipherMode = "AES/CBC/PKCS5Padding";

    public static byte[] encrypt(byte[] content, byte[] password, byte[] iv) {
        try {
            SecretKeySpec key = new SecretKeySpec(password, "AES");
            IvParameterSpec ivByte = new IvParameterSpec(iv);
            Cipher cipher = Cipher.getInstance(CipherMode);
            cipher.init(Cipher.ENCRYPT_MODE, key, ivByte);
            byte[] result = cipher.doFinal(content);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }   
    
    public static byte[] decrypt(byte[] content, byte[] password, byte[] iv) {
        try {
        	SecretKeySpec key = new SecretKeySpec(password, "AES");
        	IvParameterSpec ivByte = new IvParameterSpec(iv);
            Cipher cipher = Cipher.getInstance(CipherMode);
            cipher.init(Cipher.DECRYPT_MODE, key, ivByte);
            byte[] result = cipher.doFinal(content);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
