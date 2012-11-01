package dot.generate;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class FeatureTypeGraphGenerator {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try{
			BufferedReader fin = new BufferedReader( new FileReader(new File("featureIn.csv")));
			BufferedWriter fout = new BufferedWriter(new FileWriter(new File("FeatureTypes")));
			fout.write("digraph features {");
			fout.newLine();
			String[] line={};
			String temp;
			while((temp=fin.readLine())!=null){
				if(temp.contains(",")){
					line=temp.split(",");
				}else{
					line=new String[1];
					line[0]=temp;
				}
				for(int i=0; i<line.length; i++){
					line[i]=line[i].trim().replaceAll(" ", "");
					
				}
				fout.write("\tgnis_FeatureClass -> gnis_"+ line[0]+" [label=\"rdfs:superClass\"]");
				fout.newLine();
				fout.write("\tgnis_"+ line[0]+" -> query_" + line[0] + " [label=\"query:queriedAs\"]");
				fout.newLine();
				for(String s : line){
					if(s.equalsIgnoreCase(line[0]))continue;//do not write that line[0] is the same as line[0]
					fout.write("\tquery_"+ line[0]+" -> query_" + s + " [label=\"rdfs:sameAs\"]");
					fout.newLine();	
				}
			}
			fout.write("}");
			fout.flush();
			fout.close();
			fin.close();
		}catch(Exception e){
			e.printStackTrace();
		}



	}

}
