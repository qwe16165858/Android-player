/*********************************************************
 * Class to backup the information need at client side.
 */
package objects;
import it.unisa.dia.gas.jpbc.Element;

import java.util.ArrayList;

public class UserClient implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	public int totalDeposit;	//total deposit
	public int currentBalance;	//current balance
	public ArrayList<Integer> G;	//latest purchased attributes set
	public byte[] sk; //secret key
	public transSP tSP;	//security parameter

	//constructor
	public UserClient() {
		this.totalDeposit = 0;
		this.currentBalance = 0;
		this.G = new ArrayList<Integer>();
		this.sk = null;
		this.tSP = null;
	}

	//constructor
	public UserClient(int init, transSP tSP) {
		this.totalDeposit = init;
		this.currentBalance = init;
		this.G = new ArrayList<Integer>();
		this.tSP = tSP;
		this.sk = tSP.toSP().pairing.getZr().newElement(1).toBytes();
	}

	//recharge
	public void check(int newTotalDeposit) {
		this.currentBalance += (newTotalDeposit - totalDeposit);
		this.totalDeposit = newTotalDeposit;
	}

	//update the information
	public void update(int currentBalance, ArrayList<Integer> G, Element sk) {
		this.currentBalance = currentBalance;
		this.G = G;
		this.sk = sk.toBytes();
	}
}
