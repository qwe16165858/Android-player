/*******************************************************
 * Class for information backup at server side per user
 */
package objects;
import it.unisa.dia.gas.jpbc.Element;

public class UserServer implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	public int totalDeposit;	//total deposit
	public byte[] pg;			//the token PG last time purchase

	//constructor
	public UserServer(int totalDeposit, Element initPG) {
		this.totalDeposit = totalDeposit;
		this.pg = initPG.toBytes();
	}

	//total deposit setter
	public void setTotalDeposit(int totalDeposit) {
		this.totalDeposit = totalDeposit;
	}

	//recharge method
	public void charge(int balance) {
		this.totalDeposit += balance;	//adding total deposit per recharged balance
	}

	//PG setter
	public void setPG(Element PG) {
		this.pg = PG.toBytes();
	}
}
