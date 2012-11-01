import java.io.BufferedReader;
import java.io.FileReader;
import java.util.TreeSet;


public class NameFinder {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			BufferedReader br = new BufferedReader(new FileReader("AllNames.txt"));
			System.out.println(br.readLine().trim());
			String line;
			TreeSet <String> tree = new TreeSet<String>();
			int i=0;
			while((line=br.readLine())!=null){
				if(line.contains("|Rolla|"))System.out.println(line);;
			}
			System.out.println(i);
			br.close();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
