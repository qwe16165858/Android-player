package objects;

import it.unisa.dia.gas.jpbc.Element;

import java.util.ArrayList;

public class SMEncrypt {
	// security parameters
	SecurityParam sp;
	Element PG; // token

	public SMEncrypt(SecurityParam sp) {
		this.sp = sp;
	}

	public SMEncrypt(SecurityParam sp, Element PG) {
		this.sp = sp;
		this.PG = PG;
	}

	public void setToken(Element PG) {
		this.PG = PG;
	}

	public boolean AVerify(Element AP, int n, int k) {
		Element tmp1 = sp.pairing.pairing(PG, sp.getGax(n));
		Element tmp2 = sp.pairing.pairing(AP, sp.getGax(k));
		return tmp1.isEqual(tmp2);
	}

	public boolean RVerify(Element RP, Element PG2) {
		Element tmp1 = sp.pairing.pairing(PG2, RP);
		Element tmp2 = sp.pairing.pairing(PG, sp.g);

		return tmp1.isEqual(tmp2);
	}

	public CipherText Encrypt(Element M, ArrayList<Integer> A) {
		Element r = sp.pairing.getZr().newRandomElement();

		Element[] coef = calcCoef(A);
		Element tmp = sp.g.duplicate().powZn(coef[0]);
		for (int i = 1; i <= A.size(); i++) {
			tmp.mul(sp.getGax(i).powZn(coef[i]));
		}

		CipherText c = new CipherText();
		c.c1 = tmp.powZn(r).toBytes();
		c.c2 = sp.pairing.pairing(PG, sp.g).powZn(r.duplicate().negate())
				.mul(M).toBytes();
		c.A = A;
		return c;
	}

	private Element[] calcCoef(ArrayList<Integer> G) {
		Polynomial2 result = new Polynomial2(1, 0, sp.pairing);
		for (int i = 0; i < G.size(); i++) {
			Polynomial2 p = new Polynomial2(1, 1, sp.pairing);
			p = p.plus(new Polynomial2(G.get(i), 0, sp.pairing));
			result = result.times(p);
		}
		return result.coef();
	}
}
