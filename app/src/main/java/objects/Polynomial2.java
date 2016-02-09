package objects;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;

/**************************************************************************************
 * This source file is modified from the Polynomial.java of Princeton University.
 * http://introcs.cs.princeton.edu/java/92symbolic/Polynomial.java
 * The modification is to let the original class support JPBC Element as coefficients.
 * By Qingbo Guan
 **************************************************************************************/

/*************************************************************************
 * Compilation: javac Polynomial.java Execution: java Polynomial
 *
 * Polynomials with integer coefficients.
 *
 * % java Polynomial zero(x) = 0 p(x) = 4x^3 + 3x^2 + 2x + 1 q(x) = 3x^2 + 5
 * p(x) + q(x) = 4x^3 + 6x^2 + 2x + 6 p(x) * q(x) = 12x^5 + 9x^4 + 26x^3 + 18x^2
 * + 10x + 5 p(q(x)) = 108x^6 + 567x^4 + 996x^2 + 586 0 - p(x) = -4x^3 - 3x^2 -
 * 2x - 1 p(3) = 142 p'(x) = 12x^2 + 6x + 2 p''(x) = 24x + 6
 *
 *************************************************************************/

public class Polynomial2 {
	Pairing pairing;
	private Element[] coef; // coefficients
	private int deg; // degree of polynomial (0 for the zero polynomial)

	// a * x^b
	public Polynomial2(int a, int b, Pairing pairing) {
		this.pairing = pairing;
		coef = new Element[b + 1];
		for (int i = 0; i < coef.length; i++) {
			coef[i] = pairing.getZr().newElement(0);
		}
		coef[b] = pairing.getZr().newElement(a);
		deg = degree();
	}

	// a * x^b
	public Polynomial2(Element a, int b, Pairing pairing) {
		this.pairing = pairing;
		coef = new Element[b + 1];
		for (int i = 0; i < coef.length; i++) {
			coef[i] = pairing.getZr().newElement(0);
		}
		coef[b] = a.duplicate();
		deg = degree();
	}

	// return the degree of this polynomial (0 for the zero polynomial)
	public int degree() {
		int d = 0;
		for (int i = 0; i < coef.length; i++)
			if (!coef[i].isEqual(pairing.getZr().newElement(0)))
				d = i;
		return d;
	}

	// return c = a + b
	public Polynomial2 plus(Polynomial2 b) {
		Polynomial2 a = this;
		Polynomial2 c = new Polynomial2(0, Math.max(a.deg, b.deg), pairing);
		for (int i = 0; i <= a.deg; i++) {// c.coef[i] += a.coef[i];
			c.coef[i].add(a.coef[i]);
		}
		for (int i = 0; i <= b.deg; i++) {// c.coef[i] += b.coef[i];
			c.coef[i].add(b.coef[i]);
		}
		c.deg = c.degree();
		return c;
	}

	// return (a - b)
	public Polynomial2 minus(Polynomial2 b) {
		Polynomial2 a = this;
		Polynomial2 c = new Polynomial2(0, Math.max(a.deg, b.deg), pairing);
		for (int i = 0; i <= a.deg; i++) {
			c.coef[i].add(a.coef[i]);
		}
		for (int i = 0; i <= b.deg; i++) {
			c.coef[i].sub(b.coef[i]);
		}
		c.deg = c.degree();
		return c;
	}

	// return (a * b)
	public Polynomial2 times(Polynomial2 b) {
		Polynomial2 a = this;
		Polynomial2 c = new Polynomial2(0, a.deg + b.deg, pairing);
		for (int i = 0; i <= a.deg; i++) {
			for (int j = 0; j <= b.deg; j++) {
				c.coef[i + j].add(a.coef[i].duplicate().mulZn(b.coef[j]));
			}
		}
		c.deg = c.degree();
		return c;
	}

	// return a(b(x)) - compute using Horner's method
	public Polynomial2 compose(Polynomial2 b) {
		Polynomial2 a = this;
		Polynomial2 c = new Polynomial2(0, 0, pairing);
		for (int i = a.deg; i >= 0; i--) {
			Polynomial2 term = new Polynomial2(a.coef[i], 0, pairing);
			c = term.plus(b.times(c));
		}
		return c;
	}

	// do a and b represent the same polynomial?
	public boolean eq(Polynomial2 b) {
		Polynomial2 a = this;
		if (a.deg != b.deg)
			return false;
		for (int i = a.deg; i >= 0; i--)
			if (a.coef[i] != b.coef[i])
				return false;
		return true;
	}

	// use Horner's method to compute and return the polynomial evaluated at x
	public Element evaluate(Element x) {
		Element p = pairing.getZr().newElement(0);
		for (int i = deg; i >= 0; i--) {
			p = coef[i].duplicate().add(x.duplicate().mul(p));
		}
		return p;
	}

	// differentiate this polynomial and return it
	public Polynomial2 differentiate() {
		if (deg == 0)
			return new Polynomial2(0, 0, pairing);
		Polynomial2 deriv = new Polynomial2(0, deg - 1, pairing);
		deriv.deg = deg - 1;
		for (int i = 0; i < deg; i++)
			deriv.coef[i] = pairing.getZr().newElement(i + 1).mul(coef[i + 1]);
		return deriv;
	}

	// convert to string representation
	public String toString() {
		if (deg == 0)
			return "" + coef[0];
		if (deg == 1)
			return coef[1] + "x + " + coef[0];
		String s = coef[deg] + "x^" + deg;
		for (int i = deg - 1; i >= 0; i--) {
			if (coef[i].isEqual(pairing.getZr().newElement(0)))
				continue;
			else
				s = s + " + " + (coef[i]);
			if (i == 1)
				s = s + "x";
			else if (i > 1)
				s = s + "x^" + i;
		}
		return s;
	}

	public Element[] coef() {
		return coef;
	}
	
	/* test code
	public static void main(String[] args) {
		Pairing pairing = PairingFactory.getPairing("params/a.properties");
		Polynomial2 p1 = new Polynomial2(1, 1, pairing);
		Polynomial2 p2 = new Polynomial2(1, 0, pairing);
		Polynomial2 pp1 = p1.plus(p2);

		Polynomial2 p3 = new Polynomial2(1, 1, pairing);
		Polynomial2 p4 = new Polynomial2(2, 0, pairing);
		Polynomial2 pp2 = p3.plus(p4);

		Polynomial2 p5 = new Polynomial2(1, 1, pairing);
		Polynomial2 p6 = new Polynomial2(3, 0, pairing);
		Polynomial2 pp3 = p5.plus(p6);

		System.out.println(pp1.times(pp2).times(pp3));
	}
	//*/
}