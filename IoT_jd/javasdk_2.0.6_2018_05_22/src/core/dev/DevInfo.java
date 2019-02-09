package core.dev;

public class DevInfo {
	
	public static final short version = 1;
	/**
	 * TODO factory must modify mac for device
	 */
	public static final String mac 	= "ef:78:df:3d:d3:11";
	/**
	 * TODO factory must modify uuid for device
	 */
	public static final String uuid = "87B580";
	
	
	/**
	 * TODO factory modify below value for device.
	 */
	public static final int lancon 	= 1;			// 0：不支持局域网，1：支持局域网 
	public static final int cmdTranType = 1;		// 0:二进制, 1:Lua脚本, 2:json
	public static final int devType = 0;			// device type 0：普通设备，1：网关设备，2：子设备
	public static final int protocol = 0;           // 0:WIFI 1:zigbee 2:bluetooth 3:433
   
	/**
	 * TODO
	 * Key must be private and User Must store Key information and cloud url
	 */
	
	private static String feedId = null;
	private static String accessKey = null;
	private static String localKey = null;
	private static String cloudUrl = null;
	private static short cloudPort = 0;

	private static String shareKey = null;
	private static byte[] sessionKey = null;
	
	/**
	 * TODO factory must modify firmwareVersion for device
	 */
    public static final String firmwareVersion = "1.0.0";
    
    /**
	 * TODO factory must modify idtPublicKey for device
	 */
    public static final String idtPublicKey = "0241A5170B0299294D39B0676D3D85BE732EE3EC664A0DCFA43C871A0D85192371";
    //private String modelCode;
    //private String CID;

    public static final short lanPort = 4320;
    int crc32;
    
    public static JoylinkAppIdentification appIdt;
    public static JoylinkDeviceIdentification deviceIdt;
    
	public DevInfo() {
	    System.out.println("--->version:" + version);
	    System.out.println("--->mac:" + mac);
	    System.out.println("--->uuid:" + uuid);
	    System.out.println("--->lancon:" + lancon);
	    System.out.println("--->cmdTranType:" + cmdTranType);
	    System.out.println("--->devType:" + devType);
	    System.out.println("--->protocol:" + protocol);
	    System.out.println("--->feedid:" + feedId);
	    System.out.println("--->accesskey:" + accessKey);	
	    System.out.println("--->localkey:" + localKey);
	    System.out.println("--->cloudUrl:" + cloudUrl);
	    System.out.println("--->cloudPort:" + cloudPort);	
	    System.out.println("--->firmwareVersion:" + firmwareVersion);
	    System.out.println("--->lanPort:" + lanPort);
	}
	
	private static DevInfo single = null;
	public synchronized static DevInfo getInstance() {
		if (single == null) {
			DevInfo.appIdt = new JoylinkAppIdentification();
			DevInfo.deviceIdt = new JoylinkDeviceIdentification();
			DevInfo.deviceIdt.setCloudPublicKey(idtPublicKey);
			single = new DevInfo();
		}
		return single;
	}
	public int getLanControl() {
		return lancon;
	}

	public static String getFeedId() {
		return feedId;
	}
	public static void setFeedId(String id) {
		feedId = new String(id);
	}
	public static String getAccessKey() {
		return accessKey;
	}
	public static void setAccessKey(String key) {
		accessKey = new String(key);
	}
	public static String getLocalKey(){
		return localKey;
	}
	public static void setLocalKey(String key){
		localKey = new String(key);
	}
	public static String getShareKey(){
		return shareKey;
	}
	public static void setShareKey(String key){
		shareKey = new String(key);
	}
	public static byte[] getSessionKey(){
		return sessionKey;
	}
	public static void setSessionKey(byte[] key){
		sessionKey = new byte[32];
		System.arraycopy(key, 0, sessionKey, 0, 32);
	}
	public static String getCloudUrl(){
		return cloudUrl;
	}
	public static void setCloudUrl(String url){
		cloudUrl = new String(url);
	}
	public static short getCloudPort(){
		return cloudPort;
	}
	public static void setCloudPort(short port){
		cloudPort = port;
	}
	public static String getFirmwareVersion(){
		return firmwareVersion;
	}
	public static short getLANPort(){
		return lanPort;
	}
	public static short getVersion(){
		return version;
	}
	public String getMac(){
		return mac;
	}
	public String getUuid(){
		return uuid;
	}
	public int getLancon(){
		return lancon;
	}
	public int getCmdTranType(){
		return cmdTranType;
	}
	public int getProtocol(){
		return protocol;
	}
	public int getDevType(){
		return devType;
	}
}
