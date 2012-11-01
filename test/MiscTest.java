package test;

import java.util.ArrayList;

public class MiscTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ArrayList<Integer> list = new ArrayList<Integer>();
		list.add(3);
		for(Integer i : list){
			i++;
		}
		System.out.println(list.get(0));
	}

}
