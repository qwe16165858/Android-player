/**************************************************************
 * Security parameter used in transfer and storage.
 */
package objects;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;

import java.io.Serializable;
import java.util.ArrayList;

public class transSP implements Serializable {
	private static final long serialVersionUID = 1L;
	private PairingParameters pp;	//pairing parameter
	private byte[] g;				//generator
	private ArrayList<byte[]> gax;	//computed values g^a^x

	//constructor
	public transSP(int totalAttributeCnt) {
		this.pp = PairingFactory.getPairingParameters("/root/a.properties");
		Pairing pairing = PairingFactory.getPairing(pp);
		PairingFactory.getInstance().setUsePBCWhenPossible(true);
		
		Element gE= pairing.getG1().newRandomElement();
		this.g=gE.toBytes();
		
		gax = new ArrayList<byte[]>();
		Element a = pairing.getZr().newRandomElement();
		gax.add(gE.toBytes());	//set gax[0]=g
		for (int i = 0; i <= totalAttributeCnt; i++) {
			gax.add(gE.powZn(a).toBytes());	//compute g^a^x
		}
	}

	//constructor
	public transSP(PairingParameters pairingParameters, Element g, ArrayList<Element> gax) {
		this.pp = pairingParameters;
		this.g = g.toBytes();
		this.gax=new ArrayList<byte[]>();
		for (int i =0; i <gax.size(); i++) {
			this.gax.add(gax.get(i).toBytes());
		}
	}

	//convert transSP object to SecurityParam object
	public SecurityParam toSP() {
		return new SecurityParam(pp, g, gax);
	}

}
