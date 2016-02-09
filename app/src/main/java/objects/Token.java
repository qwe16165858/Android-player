/******************************************************
 * Token and A/RProof sent from client to server.
 * 
 */
package objects;

import it.unisa.dia.gas.jpbc.Element;

public class Token implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	//here using byte[] to make it serializable, Element cannot be serialized
	public byte[] PG;		//token
	public byte[] sigmaA;	//AProof
	public byte[] sigmaR;	//RProof
	
	//constructor
	public Token(Element PG, Element sigmaA, Element sigmaR) {
		this.PG = PG.toBytes();
		this.sigmaA = sigmaA.toBytes();
		this.sigmaR = sigmaR.toBytes();
	}
}
