import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;


public class LoadQueryBuilderGeoSPARQL {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {

		File f = new File("D:/MyFiles/SampleDataLayers");
		BufferedWriter br = new BufferedWriter(new FileWriter(new File("D:/MyFiles/queryTextGS.txt")));
		for(File stateFolder : f.listFiles()){
			for(File f2 : stateFolder.listFiles(
					new FilenameFilter() {
						public boolean accept(File dir, String name) {
							String lowercaseName = name.toLowerCase();
							if (lowercaseName.equals("n3")) {
								return true;
							} else {
								return false;
							}
						}
					})){
				for(File f3 : f2.listFiles()){
					String tmp = "DB.DBA.TTLP (file_to_string_output ('/usr/local/virtuoso-opensource/share/virtuoso/vad/"
						+f3.getAbsolutePath().substring(11).replace('\\', '/')
						+"'), '', 'http://cegis.usgs.gov/rdf/ontologytest/', 0, 0);";
					br.write(tmp);
					br.newLine();
					System.out.println(tmp);
				}
			}
		}
		br.close();
		//DB.DBA.TTLP (file_to_string_output ('.\tmp\data.ttl'), '', 'http://my_graph', 0);
	}

}
