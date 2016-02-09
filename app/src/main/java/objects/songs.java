/**************************************************************
 * Object used to store the music information used in display.
 */
package objects;

import java.util.ArrayList;

public class songs implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	public int id;			//music id
	public String name;		//music name
	public String album;	//album of music
	public String artist;	//artist of music
	public ArrayList<Price> priceList;	//list of prices
	public boolean state;	//status, purchased/non-purchased
	
	//constructor
	public songs() {
	}
	
	//constructor
	public songs(int id, String name, String album, String artist) {
		this.id = id;
		this.name = name;
		this.album = album;
		this.artist = artist;
		this.state = false;
	}

	//constructor
	public songs(int id, String name, String album, String artist,
			ArrayList<Price> priceList) {
		this.id = id;
		this.name = name;
		this.album = album;
		this.artist = artist;
		this.state = false;
		this.priceList = priceList;
	}
	
	//price setter 
	public void setPrice(ArrayList<Price> priceList) {
		this.priceList = priceList;
	}

	@Override
	public String toString() {
		return "Product [id=" + id + ", name=" + name + ", price="
				+ priceList.get(0).price + "]";
	}
}