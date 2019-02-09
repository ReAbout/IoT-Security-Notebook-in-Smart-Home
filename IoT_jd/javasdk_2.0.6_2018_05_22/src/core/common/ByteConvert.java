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

public class ByteConvert {
	
	public static int longToInt(long n) {
        byte[] b = new byte[4];
        b[3] = (byte) (n & 0xff);
        b[2] = (byte) (n >> 8  & 0xff);
        b[1] = (byte) (n >> 16 & 0xff);
        b[0] = (byte) (n >> 24 & 0xff);
        
        return    b[3] & 0xff 
                | (b[2] & 0xff) << 8 
                | (b[1] & 0xff) << 16
                | (b[0] & 0xff) << 24;
        
 	}

    public static byte[] longToBytes(long n) {
        byte[] b = new byte[8];
        b[7] = (byte) (n & 0xff);
        b[6] = (byte) (n >> 8  & 0xff);
        b[5] = (byte) (n >> 16 & 0xff);
        b[4] = (byte) (n >> 24 & 0xff);
        b[3] = (byte) (n >> 32 & 0xff);
        b[2] = (byte) (n >> 40 & 0xff);
        b[1] = (byte) (n >> 48 & 0xff);
        b[0] = (byte) (n >> 56 & 0xff);
        return b;
    }
    
    public static void longToBytes( long n, byte[] array, int offset ){
        array[7+offset] = (byte) (n & 0xff);
        array[6+offset] = (byte) (n >> 8 & 0xff);
        array[5+offset] = (byte) (n >> 16 & 0xff);
        array[4+offset] = (byte) (n >> 24 & 0xff);
        array[3+offset] = (byte) (n >> 32 & 0xff);
        array[2+offset] = (byte) (n >> 40 & 0xff);
        array[1+offset] = (byte) (n >> 48 & 0xff);
        array[0+offset] = (byte) (n >> 56 & 0xff);
    }
    
    public static long bytesToLong( byte[] array )
    {
        return ((((long) array[ 0] & 0xff) << 56)
              | (((long) array[ 1] & 0xff) << 48)
              | (((long) array[ 2] & 0xff) << 40)
              | (((long) array[ 3] & 0xff) << 32)
              | (((long) array[ 4] & 0xff) << 24)
              | (((long) array[ 5] & 0xff) << 16)
              | (((long) array[ 6] & 0xff) << 8) 
              | (((long) array[ 7] & 0xff) << 0));        
    }
    
    public static long bytesToLong( byte[] array, int offset )
    {
        return ((((long) array[offset + 0] & 0xff) << 56)
              | (((long) array[offset + 1] & 0xff) << 48)
              | (((long) array[offset + 2] & 0xff) << 40)
              | (((long) array[offset + 3] & 0xff) << 32)
              | (((long) array[offset + 4] & 0xff) << 24)
              | (((long) array[offset + 5] & 0xff) << 16)
              | (((long) array[offset + 6] & 0xff) << 8) 
              | (((long) array[offset + 7] & 0xff) << 0));            
    }
    
    public static byte[] intToBytes(int n) {
        byte[] b = new byte[4];
        b[3] = (byte) (n & 0xff);
        b[2] = (byte) (n >> 8 & 0xff);
        b[1] = (byte) (n >> 16 & 0xff);
        b[0] = (byte) (n >> 24 & 0xff);
        return b;
    }
    
    public static void intToBytes( int n, byte[] array, int offset ){
        array[3+offset] = (byte) (n & 0xff);
        array[2+offset] = (byte) (n >> 8 & 0xff);
        array[1+offset] = (byte) (n >> 16 & 0xff);
        array[offset] = (byte) (n >> 24 & 0xff);
    }    

    public static int bytesToInt(byte b[]) {
        return    b[3] & 0xff 
               | (b[2] & 0xff) << 8 
               | (b[1] & 0xff) << 16
               | (b[0] & 0xff) << 24;
    }

    public static int bytesToInt(byte b[], int offset) {
        return    b[offset+3] & 0xff 
               | (b[offset+2] & 0xff) << 8 
               | (b[offset+1] & 0xff) << 16
               | (b[offset] & 0xff) << 24;
    }

    public static byte[] uintToBytes( long n )
    {
        byte[] b = new byte[4];
        b[3] = (byte) (n & 0xff);
        b[2] = (byte) (n >> 8 & 0xff);
        b[1] = (byte) (n >> 16 & 0xff);
        b[0] = (byte) (n >> 24 & 0xff);
        
        return b;
    }

    public static void uintToBytes( long n, byte[] array, int offset ){
        array[3+offset] = (byte) (n );
        array[2+offset] = (byte) (n >> 8 & 0xff);
        array[1+offset] = (byte) (n >> 16 & 0xff);
        array[offset]   = (byte) (n >> 24 & 0xff);
    }

    public static long bytesToUint(byte[] array) {  
        return ((long) (array[3] & 0xff))  
             | ((long) (array[2] & 0xff)) << 8  
             | ((long) (array[1] & 0xff)) << 16  
             | ((long) (array[0] & 0xff)) << 24;  
    }

    public static long bytesToUint(byte[] array, int offset) {   
        return ((long) (array[offset+3] & 0xff))  
              | ((long) (array[offset+2] & 0xff)) << 8  
             | ((long) (array[offset+1] & 0xff)) << 16  
             | ((long) (array[offset]   & 0xff)) << 24;  
    }

    public static byte[] shortToBytes(short n) {
        byte[] b = new byte[2];
        b[1] = (byte) ( n       & 0xff);
        b[0] = (byte) ((n >> 8) & 0xff);
        return b;
    }
    
    public static void shortToBytes(short n, byte[] array, int offset ) {        
        array[offset+1] = (byte) ( n       & 0xff);
        array[offset] = (byte) ((n >> 8) & 0xff);
    }
    
    public static short bytesToShort(byte[] b){
        return (short)( b[1] & 0xff
                      |(b[0] & 0xff) << 8 ); 
    }    

    public static short bytesToShort(byte[] b, int offset){
        return (short)( b[offset+1] & 0xff
                      |(b[offset]    & 0xff) << 8 ); 
    }

    public static byte[] ushortToBytes(int n) {
        byte[] b = new byte[2];
        b[1] = (byte) ( n       & 0xff);
        b[0] = (byte) ((n >> 8) & 0xff);
        return b;
    }    

    public static void ushortToBytes(int n, byte[] array, int offset ) {
        array[offset+1] = (byte) ( n       & 0xff);
        array[offset] = (byte)   ((n >> 8) & 0xff);
    }

    public static int bytesToUshort(byte b[]) {
        return    b[1] & 0xff 
               | (b[0] & 0xff) << 8;
    }    

    public static int bytesToUshort(byte b[], int offset) {
        return    b[offset+1] & 0xff 
               | (b[offset]   & 0xff) << 8;
    }    

    public static byte[] ubyteToBytes( int n ){
        byte[] b = new byte[1];
        b[0] = (byte) (n & 0xff);
        return b;
    }

    public static void ubyteToBytes( int n, byte[] array, int offset ){
        array[offset] = (byte) (n & 0xff);
    }

    public static int bytesToUbyte( byte[] array ){            
        return array[0] & 0xff;
    }        

    public static int bytesToUbyte( byte[] array, int offset ){            
        return array[offset] & 0xff;
    }
    
    public static boolean isBigEndian() {
    	short x = 0;
    	byte x1 = 0;
    	
    	x = 0x1122;
    	x1 = (byte) (x & 0xff);
    	
    	if (x1 == 0x22) {
    		return false;
    	} else if(x1 == 0x11) {
    		return true;
    	}
    	return false;
    }
    
}
