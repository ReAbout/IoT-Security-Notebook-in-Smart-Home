package core.dev;

public class JoylinkAppIdentification {

	public static final int JL_E_SCAN_DEFAULT = 0;
	public static final int JL_E_SCAN_WAITCONF = 1;
	public static final int JL_E_SCAN_CONFIGED = 2;

	private static String uuid = null;
	private static int type = JL_E_SCAN_DEFAULT;
	private static String random = null;
	
	public static String getUuid() {
		return uuid;
	}
	public void setUuid(String id) {
		uuid = new String(id);
	}
	
	public  String getRandom() {
		return random;
	}
	public  void setRandom(String rd) {
		random = new String(rd);
	}
	
	public  int getScanType() {
		return type;
	}
	public  void setScanType(int t) {
		type = t;
	}
}
