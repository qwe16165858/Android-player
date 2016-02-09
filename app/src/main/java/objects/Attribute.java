/*************************************************************************
 * Function of attributes generating
 */
package objects;

import java.util.ArrayList;

public class Attribute {
	//function to generate attributes from item index and price
	public static ArrayList<Integer> GetAttributes(int idx, int price) {
		ArrayList<Integer> tmp = new ArrayList<Integer>();
		//generate attributes including price number of distinct number
		//here we make assumption that the price of a item should be less than 25
		for (int i = 0; i < price; i++) {
			tmp.add(idx * 25 + i);
		}
		return tmp;
	}
}
