package objects;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;

import java.io.Serializable;
import java.util.ArrayList;

public class SecurityParam implements Serializable {
	private static final long serialVersionUID = 1L;
	public Pairing pairing;
	public Element g;
	public ArrayList<Element> gax;

	public SecurityParam(Pairing pairing, Element g, ArrayList<Element> gax) {
		this.pairing = pairing;
		this.g = g;
		this.gax = gax;
	}

	public SecurityParam(PairingParameters pp, byte[] g, ArrayList<byte[]> gax) {
		this.pairing = PairingFactory.getPairing(pp);
		PairingFactory.getInstance().setUsePBCWhenPossible(true);
		this.g = pairing.getG1().newElementFromBytes(g);
		this.gax = new ArrayList<Element>();
		for (int i = 0; i < gax.size(); i++) {
			this.gax.add(pairing.getG1().newElementFromBytes(gax.get(i)));
		}
	}

	public Element getGax(int x) {
		return gax.get(x).duplicate();
	}
}