package objects;

public class Item implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	public int id;
	public String conditions;
	public String conditionPrices;
	public String coupon;
	public int price;
	public int pid;

	public Item() {
	}

	public Item(int pid, int id, int price, String coupon, String conditions) {
		this.pid = pid;
		this.id = id;
		this.price = price;
		this.coupon = coupon;
		this.conditions = conditions;
		this.conditionPrices = "";
	}

	public Item(int pid, int id, int price, String coupon, String[] conditions) {
		this.pid = pid;
		this.id = id;
		this.price = price;
		this.coupon = coupon;
		this.conditions = conditions[0];
		this.conditionPrices = conditions[2];
	}
}