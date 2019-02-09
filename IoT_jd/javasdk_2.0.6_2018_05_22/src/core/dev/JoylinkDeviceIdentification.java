package core.dev;

public class JoylinkDeviceIdentification {

	private static String cloudPublicKey = null;
	private static byte[] cloudSignature;
	private static String random = null;
	
	private static String signature = null;
	private static String factoryPublicKey = null;
	private static String factorySignature = null;

	private static String aRandomSignature = null;

	private static String uuid = null;
	private static int type = 0;

	public  String getCloudPublicKey() {
		return cloudPublicKey;
	}

	public void setCloudPublicKey(String key) {
		cloudPublicKey = new String(key);
	}

	public String getRandom() {
		return random;
	}

	public void setRandom(String rd) {
		random = new String(rd);
	}

	public int getScanType() {
		return type;
	}

	public void setScanType(int t) {
		type = t;
	}
	
	public static byte[] getCloudSignature() {
		return cloudSignature;
	}

	public void setCloudSignature(byte[] signature) {
		cloudSignature = new byte[signature.length];
	}
	
}
