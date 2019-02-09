package com.jd.mobile.joylink;

/**
 * Created by wanggang
 */
public class SecurityService {
    static {
    	System.loadLibrary("auth");
        System.loadLibrary("ecdsa");
    }
    /**
     * @param
     * @param
     * @return
     */
    public native byte[] sign(byte[] privateKey, byte[] text);
    /**
     * @param
     * @param
     * @param
     * @return
     */
    public native boolean verify(byte[] sign, byte[] publickKey, byte[] text);

    /**
     * @param
     * @param
     * @param
     * @return
     */
    public native KeyGenerator getKeyPair();

}
