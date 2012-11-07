package gnis.featuretype;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Queue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.util.Rotation;



public class LikelyTypeFinder implements ActionListener {
  JPanel areaPanel = new JPanel();
  JPanel nonTextArea = new JPanel();
	JFrame frame = new JFrame("TermFinderDemo");
	JLabel instructions = new JLabel("Enter unknown term below");
	JTextField term = new JTextField(15);
	JButton submit = new JButton("Find Likely Matches");
	JLabel results = new JLabel("Results will be displayed here.");
  JTextArea triples = new JTextArea("Triples will be displayed here.");
	JFrame chartFrame = new JFrame("Match chart");
	JScrollPane scroll = new JScrollPane(triples);
	DefaultPieDataset pieData;
	JFreeChart chart;
	ChartPanel cp;

	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		new LikelyTypeFinder();
	}

	public LikelyTypeFinder()
	{
		frame.setLayout(new GridLayout(1, 2, 30, 0));
		nonTextArea.setLayout(new FlowLayout());
		areaPanel.setLayout(new FlowLayout());

		submit.setActionCommand("look");
		submit.addActionListener(this);
		term.setActionCommand("look");
		term.addActionListener(this);
		nonTextArea.add(instructions);
		nonTextArea.add(term);
		nonTextArea.add(submit);
		nonTextArea.add(results);
		scroll.setBounds(100, 100, 50, 50);
		areaPanel.add(scroll);
		frame.add(nonTextArea);
		frame.add(areaPanel);
		frame.setBounds(200, 200, 550, 800);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void actionPerformed(ActionEvent e) {
		if ("look".equals(e.getActionCommand())) 
		{

			try
			{
				LinkedHashMap<String, Integer> map = getLikelyTypeMap(scroll, triples, term.getText());

				System.out.println(map.toString());
				results.setText("<HTML>"+
						map.toString()
						.substring(1, map.toString().length()-1)
						.replaceAll(",", ")<br>")
						.replaceAll("=", "(")+
				")</HTML>");
				frame.repaint();

				if(chartFrame.isShowing()){
					chartFrame.setVisible(false);
					chartFrame.remove(cp);
				}

				pieData = new DefaultPieDataset();

				for(Entry<String, Integer> entry : map.entrySet()){
					pieData.setValue(entry.getKey(), entry.getValue());
				}
				chart=null;
				chart = ChartFactory.createPieChart3D("Chart of probable types", pieData, true, true, false);
				PiePlot3D plot = (PiePlot3D) chart.getPlot();
		        plot.setStartAngle(290);
		        plot.setDirection(Rotation.CLOCKWISE);
		        plot.setForegroundAlpha(0.8f);
		        plot.setNoDataMessage("No data to display");
		        
		        cp=null;
		        cp = new ChartPanel(chart);
		        cp.setPreferredSize(new Dimension(830 ,590));
		        chartFrame.add(cp);
		        chartFrame.setBounds(410, 200, 840, 600);
		        chartFrame.pack();
		        chartFrame.setVisible(true);

			}
			catch(Exception ex){
				results.setText(ex.getMessage());
			}

			frame.repaint();
		}
	} 


	public static LinkedHashMap<String, Integer> getLikelyTypeMap(JScrollPane scroll, JTextArea area, String unknownType) throws Exception{
		
		try{
		    unknownType = unknownType.replaceAll("\\s","");
			HttpURLConnection conn = (HttpURLConnection)( new URL("http://www.google.com/search?hl=en&source=hp&q=define%3A+"+unknownType+"&aq=f&aqi=g10&aql=&oq=&safe=active").openConnection());
			conn.setConnectTimeout( 10000 );
			conn.setReadTimeout( 10000 );
			conn.setInstanceFollowRedirects( true );
			conn.setRequestProperty( "User-agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US; rv:1.9.2b5) Gecko/20091204 Firefox/3.6b5" );
			InputStream is = (InputStream)(conn.getContent());
			String line;
			BufferedReader in = new BufferedReader( new InputStreamReader(is));
			while(!(line=in.readLine()).contains(".mp3")){
				//sets line to the line that has the info on it.
			}
			line = line.substring(line.indexOf(".mp3"));//reduces the line to only contain the info and tags


			char[] ch=line.trim().toLowerCase().toCharArray();
			StringBuilder sb = new StringBuilder(line.length());
			boolean prevWS=false;
			for(int i=0; i<ch.length; i++){
				if(Character.isLetterOrDigit(ch[i])||(!prevWS&&Character.isWhitespace(ch[i])))
					sb.append(ch[i]);
				prevWS = Character.isWhitespace(ch[i]);
			}
			line=sb.toString();

			BufferedReader fin = new BufferedReader( new FileReader(new File("featureIn.csv")));
			BufferedReader dictionary = new BufferedReader( new FileReader(new File("featureIn.csv")));

			ArrayList<String> words = new ArrayList<String>();
			ArrayList<Integer> numWords = new ArrayList<Integer>();
			ArrayList<String> predicates = new ArrayList<String>();
			
			
			//populates features with the features to match against in the csv file
			String features;
			while((features=fin.readLine())!=null){
				words.add(null);
				numWords.add(null);
				String [] wordArray = features.split(", ");
				for(String s : wordArray){
					words.add(s);
					numWords.add(0);
				}

			}
			

			
			Queue<Integer> matches = new LinkedList<Integer>();
			int count = 0;
			for(String s : line.split(" ")){
				count++;
				for(int i = 0; i < words.size(); i++)
				{
					if(s.equalsIgnoreCase(words.get(i)))
					{
						numWords.set(i, numWords.get(i)+1);
						if(i != 0)
						{
							matches.add(count);	 
						}
					}
				}
			}
			

			
			count = 1;
			for(String s : line.split(" "))
			{

				while(matches.size() > 0 && count == matches.peek() - 1)
				{
					predicates.add(s);
					if(matches.size() > 0)
					    matches.remove();
				}
				  count++;
			}


		
			
			ArrayList<String> cWords = new ArrayList<String>();
			ArrayList<Integer> cNum = new ArrayList<Integer>();

			
			for(int i=0; i<words.size(); i++){
				if(words.get(i)==null || i==0){
					cWords.add(words.get(i+1));
					cNum.add(0);
				}
				else{
					cNum.set(cNum.size()-1, cNum.get(cNum.size()-1)+numWords.get(i));
				}
			}

			
			//sort
			ArrayList<String> sWords = new ArrayList<String>();
			ArrayList<Integer> sNum = new ArrayList<Integer>();
			ArrayList<String> combined = new ArrayList<String>();
			
			
			count = 0;
			int copies = 0;
			for(int i = 0; i < cNum.size(); i++)
			{
				if(cNum.get(i) != 0)
				{
					copies = cNum.get(i);
					while(copies > 0)
					{
						copies--;
						combined.add(unknownType + "->" + predicates.get(count) + "->" + cWords.get(i) + "\n");
						count++;
					}
				}
			}
			

			
			while(cNum.size() > 0)
			{
				int high=0;
				for(int i = 0; i < cWords.size(); i++)
				{
					if(cNum.get(i) > cNum.get(high))
					{
						high=i;
					}
				}
				sWords.add(cWords.get(high));
				sNum.add(cNum.get(high));
				cWords.remove(high);
				cNum.remove(high);
			}

			
			
			LinkedHashMap<String, Integer> results = new LinkedHashMap<String, Integer>();
			for(int i=0; i<sWords.size(); i++){
				if(sNum.get(i)!=null&&sNum.get(i)!=0)
				{
					results.put(sWords.get(i), sNum.get(i));
				}
			}

			fin.close();

			area.setText("");
			for(int i = 0; i < combined.size(); i++)
			  area.append(combined.get(i) + "\n");
			
			scroll.setPreferredSize(new Dimension(400, 600));
			
			return results;

		}catch(Exception e){
			throw e;
		}
	}
}