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

import core.common.ByteConvert;

public class ByteUtil {    
    /**
     * brief: 
     *
     * @Param: b[]
     * @Param: s
     * @Param: index
     *
     * @Returns: 
     */
    public static void putShort(byte b[], short s, int index) {    
        ByteConvert.shortToBytes(s, b, index);
        //b[index + 1] = (byte) (s >> 8);    
        //b[index + 0] = (byte) (s >> 0);    
    }    

    /**
     * brief: 
     *
     * @Param: b
     * @Param: index
     *
     * @Returns: 
     */
    public static short getShort(byte[] b, int index) {    
        return ByteConvert.bytesToShort(subBytes(b , index, 2));
        //return (short) (((b[index + 1] << 8) | b[index + 0] & 0xff));    
    }    

    /**
     * brief: 
     *
     * @Param: bb
     * @Param: x
     * @Param: index
     *
     * @Returns: 
     */
    public static void putInt(byte[] b, int x, int index) {    
        ByteConvert.intToBytes(x, b, index);
        //bb[index + 3] = (byte) (x >> 24);    
        //bb[index + 2] = (byte) (x >> 16);    
        //bb[index + 1] = (byte) (x >> 8);    
        //bb[index + 0] = (byte) (x >> 0);    
    }    

    /**
     * brief: 
     *
     * @Param: bb
     * @Param: index
     *
     * @Returns: 
     */
    public static int getInt(byte[] bb, int index) {    
        return ByteConvert.bytesToInt(bb, index);
        //return (int) ((((bb[index + 3] & 0xff) << 24)    
                    //| ((bb[index + 2] & 0xff) << 16)    
                    //| ((bb[index + 1] & 0xff) << 8) | ((bb[index + 0] & 0xff) << 0)));    
    }    

    /**
     * brief: 
     *
     * @Param: bb
     * @Param: x
     * @Param: index
     *
     * @Returns: 
     */
    public static void putLong(byte[] bb, long x, int index) {    
        ByteConvert.longToBytes(x, bb, index);
        //bb[index + 7] = (byte) (x >> 56);    
        //bb[index + 6] = (byte) (x >> 48);    
        //bb[index + 5] = (byte) (x >> 40);    
        //bb[index + 4] = (byte) (x >> 32);    
        //bb[index + 3] = (byte) (x >> 24);    
        //bb[index + 2] = (byte) (x >> 16);    
        //bb[index + 1] = (byte) (x >> 8);    
        //bb[index + 0] = (byte) (x >> 0);    
    }    

    /**
     * brief: 
     *
     * @Param: bb
     * @Param: index
     *
     * @Returns: 
     */
    public static long getLong(byte[] bb, int index) {    
        return ByteConvert.bytesToLong(bb, index);
        //return ((((long) bb[index + 7] & 0xff) << 56)    
                //| (((long) bb[index + 6] & 0xff) << 48)    
                //| (((long) bb[index + 5] & 0xff) << 40)    
                //| (((long) bb[index + 4] & 0xff) << 32)    
                //| (((long) bb[index + 3] & 0xff) << 24)    
                //| (((long) bb[index + 2] & 0xff) << 16)    
                //| (((long) bb[index + 1] & 0xff) << 8) | (((long) bb[index + 0] & 0xff) << 0));    
    }    

    /**
     * brief: 
     *
     * @Param: bb
     * @Param: ch
     * @Param: index
     *
     * @Returns: 
     */
    public static void putChar(byte[] bb, char ch, int index) {    
        int temp = (int) ch;    
        for (int i = 0; i < 2; i ++ ) {    
            bb[index + i] = new Integer(temp & 0xff).byteValue();
            temp = temp >> 8; 
        }    
    }    

    /**
     * brief: 
     *
     * @Param: b
     * @Param: index
     *
     * @Returns: 
     */
    public static char getChar(byte[] b, int index) {    
        int s = 0;    
        if (b[index + 1] > 0)    
            s += b[index + 1];    
        else    
            s += 256 + b[index + 0];    
        s *= 256;    
        if (b[index + 0] > 0)    
            s += b[index + 1];    
        else    
            s += 256 + b[index + 0];    
        char ch = (char) s;    
        return ch;    
    }    

    /**
     * brief: 
     *
     * @Param: bb
     * @Param: x
     * @Param: index
     *
     * @Returns: 
     */
    public static void putFloat(byte[] bb, float x, int index) {    
        int l = Float.floatToIntBits(x);    
        for (int i = 0; i < 4; i++) {    
            bb[index + i] = new Integer(l).byteValue();    
            l = l >> 8;    
        }    
    }    

    /**
     * brief: 
     *
     * @Param: b
     * @Param: index
     *
     * @Returns: 
     */
    public static float getFloat(byte[] b, int index) {    
        int l;    
        l = b[index + 0];    
        l &= 0xff;    
        l |= ((long) b[index + 1] << 8);    
        l &= 0xffff;    
        l |= ((long) b[index + 2] << 16);    
        l &= 0xffffff;    
        l |= ((long) b[index + 3] << 24);    
        return Float.intBitsToFloat(l);    
    }    

    /**
     * brief: 
     *
     * @Param: bb
     * @Param: x
     * @Param: index
     *
     * @Returns: 
     */
    public static void putDouble(byte[] bb, double x, int index) {    
        // byte[] b = new byte[8];    
        long l = Double.doubleToLongBits(x);    
        for (int i = 0; i < 4; i++) {    
            bb[index + i] = new Long(l).byteValue();    
            l = l >> 8;    
        }    
    }    

    /**
     * brief: 
     *
     * @Param: b
     * @Param: index
     *
     * @Returns: 
     */
    public static double getDouble(byte[] b, int index) {    
        long l;    
        l = b[0];    
        l &= 0xff;    
        l |= ((long) b[1] << 8);    
        l &= 0xffff;    
        l |= ((long) b[2] << 16);    
        l &= 0xffffff;    
        l |= ((long) b[3] << 24);    
        l &= 0xffffffffl;    
        l |= ((long) b[4] << 32);    
        l &= 0xffffffffffl;    
        l |= ((long) b[5] << 40);    
        l &= 0xffffffffffffl;    
        l |= ((long) b[6] << 48);    
        l &= 0xffffffffffffffl;    
        l |= ((long) b[7] << 56);    
        return Double.longBitsToDouble(l);    
    }    

    /**
     * brief: 
     *
     * @Param: src
     * @Param: begin
     * @Param: count
     *
     * @Returns: 
     */
    public static byte[] subBytes(byte[] src, int begin, int count) {  
        byte[] bs = new byte[count];  
        System.arraycopy(src, begin, bs, 0, count);  
        return bs;  
    }  

    /**
     * brief: 
     *
     * @Param: first
     * @Param: second
     *
     * @Returns: 
     */
    public static byte[] byteCat(byte[] first, byte[] second) {  
        byte[] result = new byte[first.length + second.length];  
        System.arraycopy(first, 0, result, 0, first.length);  
        System.arraycopy(second, 0, result, first.length, second.length);  
        return result;  
    } 

    /**
     * brief: 
     *
     * @Param: src
     * @Param: begin
     * @Param: dest
     * @Param: start
     * @Param: count
     *
     * @Returns: 
     */
    public static int bytesCopy(byte[] src, int begin, 
            byte[] dest, int start, int count) {  
        System.arraycopy(src, begin, dest, start, count);  
        return count;  
    }  

    /**
     * brief: 
     *
     * @Param: buf
     * @Param: offset
     *
     * @Returns: 
     */
    public static void printBytes(byte[] buf, int offset){
        for (int i = 0; i + offset < buf.length; i++){
            String hex = Integer.toHexString(buf[i + offset] & 0xFF);
            if (hex.length() == 1){
                hex = '0' + hex;
            }
            System.out.print(hex.toUpperCase() + " ");
            if((i+1)%4 == 0){
                System.out.print("|" + " ");
            }
            if((i+1)%16 == 0){
                System.out.print("\n");
            }
        }
        System.out.println("");
    }
    /**
     * brief: 
     *
     * @Param: buf
     *
     * @Returns: 
     */
    public static void printBytes(byte[] buf){
        printBytes(buf, 0);
    }
    
    public static int intConvertLE(int i){
    	byte[] b = ByteConvert.intToBytes(i);
    	int le = 0;
    	le = ((b[3] & 0xff) << 24) |
    		 ((b[2] & 0xff) << 16) |
    		 ((b[1] & 0xff) << 8)  |
    		 ((b[0] & 0xff)) ;
    	return le;
    }
    public static short shortConvertLE(short i){
    	byte[] b = ByteConvert.shortToBytes(i);
    	short le = 0;
    	le = (short)(((b[1] & 0xff) << 8) | (b[0] & 0xff));
    	
    	return le;
    }
}   
