import java.io.IOException;
import java.util.TreeMap;

public class Driver {

	public static void main(String[] args) throws IOException {
		String url = "http://scores.espn.go.com/nhl/boxscore?gameId=400552569";
		String charset = "utf-8";
		
		URLParser urlParser = new URLParser(url, charset);
		
		// Get the website
		String website = urlParser.doGet();
		// Get the teams
		String[] teams = urlParser.parseTeams(website);
		
		TreeMap<String, String> statistics = new TreeMap<String, String>();
		TreeMap<String, TreeMap<String, String>> players = new TreeMap<String, TreeMap<String,String>>();
		TreeMap<String, TreeMap<String, TreeMap<String, String>>> teamsStats = new TreeMap<String, TreeMap<String, TreeMap<String, String>>>();
		
		
		
		  
	}

}