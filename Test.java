public class Test
{
	private static Integer myInt;

	public static void main(String[] args){
		Test.setMyInt(1);
		System.out.println(Test.getMyInt());
	}
	
	public static void setMyInt(Integer myInt) {
		if(myInt==null)Test.myInt = myInt;
	}

	public static Integer getMyInt() {
		return myInt;
	}
	
}