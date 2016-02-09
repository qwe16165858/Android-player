/*************************************************************
 * Object for SME decryption
 * For the formula used please refer to Fuchun Guo's paper
 */
package objects;

import it.unisa.dia.gas.jpbc.Element;

import java.util.ArrayList;

public class SMDecrypt {
	private SecurityParam sp;	//security parameter
	private ArrayList<Integer> G; // attribute set of choice
	private Element PG; // token
	private Element sk; // secret key

	//constructor
	public SMDecrypt(SecurityParam sp, ArrayList<Integer> G) {
		this.sp = sp;
		this.G = new ArrayList<Integer>(G);
	}

	//setter of attributes
	public void setG(ArrayList<Integer> G) {
		this.G = G;
	}

	//randomly selecting secret key and generate token based on it
	public Element genToken() {
		sk = sp.pairing.getZr().newRandomElement();	//random a secret key in Zp
		return getTokenByCurrentParam();	//generate token
	}

	//generate token based on the current sk in object
	public Element getTokenByCurrentParam() {
		Element[] coef = calcCoef(G);
		Element tmp = sp.g.duplicate().powZn(coef[0]);

		for (int i = 1; i <= G.size(); i++) {
			tmp.mul(sp.getGax(i).powZn(coef[i]));
		}
		return tmp.powZn(sk);
	}

	//calculating coefficient
	private Element[] calcCoef(ArrayList<Integer> G) {
		Polynomial2 result = new Polynomial2(1, 0, sp.pairing);
		for (int i = 0; i < G.size(); i++) {
			Polynomial2 p = new Polynomial2(1, 1, sp.pairing);
			p = p.plus(new Polynomial2(G.get(i), 0, sp.pairing));
			result = result.times(p);
		}
		return result.coef();
	}

	//generate initial token on an empty attribute list G
	public Element genInitToken() {
		return sp.g.duplicate();	//initial token is actually generator g
	}

	//getter function of token PG
	public Element GetToken() {
		return PG.duplicate();
	}

	//getter function of secret key
	public Element GetSk() {
		return sk.duplicate();
	}

	//generate AProof
	// n is the total attribute amount,
	// k is total deposit
	public Element GenAProof(int n, int k) {
		Element[] coef = calcCoef(G);
		Element tmp = sp.getGax(n - k).powZn(coef[0]);

		for (int i = 1; i <= G.size(); i++) {
			tmp.mul(sp.getGax(n - k + i).powZn(coef[i]));
		}
		return tmp.powZn(sk);
	}

	//generate RProof
	//G2 is the attribute set after last time purchase
	//sk2 is the secret key used in the last purchase
	public Element GenRProof(ArrayList<Integer> G2, Element sk2) {
		// compute relative complement set of G in G2
		ArrayList<Integer> GR = new ArrayList<Integer>(G);
		GR.removeAll(G2);

		Element[] coef = calcCoef(GR);
		Element tmp = sp.g.duplicate().powZn(coef[0]);
		for (int i = 1; i <= GR.size(); i++) {
			tmp.mul(sp.getGax(i).powZn(coef[i]));
		}
		return tmp.powZn(sk.duplicate().div(sk2));
	}

	//decrypt
	public Element Decrpyt(CipherText cipher) {
		ArrayList<Integer> GA = new ArrayList<Integer>(G);
		GA.removeAll(cipher.A);
		Element[] coef = calcCoef(GA);
		Element tmp = sp.g.duplicate().powZn(coef[0]);
		for (int i = 1; i <= GA.size(); i++) {
			tmp.mul(sp.getGax(i).powZn(coef[i]));
		}

		Element c1 = sp.pairing.getG1().newElementFromBytes(cipher.c1);	//c1 in cipher
		Element c2 = sp.pairing.getGT().newElementFromBytes(cipher.c2);	//c2 in cipher
		
		Element D = c2.mul(sp.pairing.pairing(c1.powZn(sk), tmp));	//decrypted message
		return D;
	}
}
