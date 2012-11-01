package test;

public class BoolTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("a|b|f");
		System.out.println("T|T|"+ (f(true, true)?"T":"F"));
		System.out.println("T|F|"+ (f(true, false)?"T":"F"));
		System.out.println("F|T|"+ (f(false, true)?"T":"F"));
		System.out.println("F|F|"+ (f(false, false)?"T":"F"));
		
	}

	private static boolean f(boolean a, boolean b){
		return !(!b || !a);
	}
}
