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

import java.math.BigInteger;

import java.security.SecureRandom;

import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.asn1.sec.SECNamedCurves;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.BasicAgreement;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECKeyGenerationParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.crypto.agreement.ECDHBasicAgreement;
import org.bouncycastle.crypto.generators.ECKeyPairGenerator;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.util.BigIntegers;

import core.common.ByteUtil;

public class ECC {
	private byte[] privateKeyBytes;
	private byte[] publicKeyBytes;
	
	private ECPrivateKeyParameters privateKeyParameters;
	public byte[] getPrivateKeyBytes() {
		return privateKeyBytes;
	}
	
	private static ECC single = null;
	public synchronized static ECC getInstance() {
		if (single == null) {
			single = new ECC();
		}
		return single;
	}
	
	public void setPrivateKeyBytes(byte[] privateKeyBytes) {
		this.privateKeyBytes = privateKeyBytes;
	}
	public byte[] getPublicKeyBytes() {
		return publicKeyBytes;
	}
	public void setPublicKeyBytes(byte[] publicKeyBytes) {
		this.publicKeyBytes = publicKeyBytes;
	}
	
	public ECC() {
		this.privateKeyBytes = new byte[20];
		this.publicKeyBytes = new byte[21];

		//Get domain parameters for example curve secp160r1
		X9ECParameters ecp = SECNamedCurves.getByName("secp160r1");
	    ECDomainParameters domainParams = new ECDomainParameters(ecp.getCurve(),
	                                                             ecp.getG(), ecp.getN(), ecp.getH(),
	                                                             ecp.getSeed());

	    // Generate a private key and a public key
	    AsymmetricCipherKeyPair keyPair;
	    ECKeyGenerationParameters keyGenParams = new ECKeyGenerationParameters(domainParams, new SecureRandom());
	    ECKeyPairGenerator generator = new ECKeyPairGenerator();
	    generator.init(keyGenParams);
	    keyPair = generator.generateKeyPair();
		
	    this.privateKeyParameters = (ECPrivateKeyParameters) keyPair.getPrivate();
	    ECPublicKeyParameters publicKey = (ECPublicKeyParameters) keyPair.getPublic();
	    
	    
	    this.privateKeyBytes = privateKeyParameters.getD().toByteArray();
	    
	    
	    this.publicKeyBytes = publicKey.getQ().getEncoded(true);
	    
	    // First print our generated private key and public key
	    System.out.println("Private key: ");
	    ByteUtil.printBytes(privateKeyBytes, 0);
	    System.out.println("Public key: ");
	    ByteUtil.printBytes(publicKeyBytes, 0);
	}
	
	public byte[] generateShareKey(byte[] publicKeyBytes) {
		ECCurve ecCurve = this.privateKeyParameters.getParameters().getCurve();
		ECDomainParameters ecDomainParameters = this.privateKeyParameters.getParameters();
		ECPublicKeyParameters clientPublicKey = new ECPublicKeyParameters(ecCurve.decodePoint(publicKeyBytes), ecDomainParameters);
		
		BasicAgreement agree = new ECDHBasicAgreement();
		agree.init(privateKeyParameters);
		BigInteger agreementValue = agree.calculateAgreement(clientPublicKey);
		byte[] keyAgreement = BigIntegers.asUnsignedByteArray(agree.getFieldSize(), agreementValue);
		System.out.println("share key");
		ByteUtil.printBytes(keyAgreement);
		return keyAgreement;
	}
}
