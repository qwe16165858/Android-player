/**********************************************************
 * the price information of a song
 */
package objects;

public class Price implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	public int pid;					//price id
	public int price;				//price
	public String conditions;		//pre-requisite items
	public String conditionNames;	//names of pre-requisite items
	public String conditionPrices;	//prices of pre-requisite items

	//construct
	public Price(int pid,int price, String conditions) {
		this.pid=pid;
		this.price = price;
		this.conditions = conditions;
		this.conditionNames="";
		this.conditionPrices="";
	}
	
	//construct
	public Price(int pid,int price, String[] conditions) {
		this.pid=pid;
		this.price = price;
		this.conditions = conditions[0];
		this.conditionNames=conditions[1];
		this.conditionPrices=conditions[2];
	}
}
