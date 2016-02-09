/*************************************************************
 * Class for cipher message used in SME encrpytion
 */
package objects;
import it.unisa.dia.gas.jpbc.Element;
import java.util.ArrayList;

public class CipherText implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	public byte[] c1;	//c1, stored in byte[] to support serializing when transmission
	public byte[] c2;	//c2, as c1
	ArrayList<Integer> A;	//attributes set used when encrypting this message

	//constructor
	public CipherText() {
	}

	//constructor
	public CipherText(Element c1, Element c2, ArrayList<Integer> A) {
		this.c1 = c1.toBytes();
		this.c2 = c2.toBytes();
		this.A = A;
	}
}
