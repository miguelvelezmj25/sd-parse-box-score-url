import java.io.IOException;

public class Driver {

	public static void main(String[] args) throws IOException {
		String url = "http://scores.espn.go.com/nhl/boxscore?gameId=400552569";
		String charset = "utf-8";
		
		URLParser urlParser = new URLParser(url, charset);
		
		// Get the website
		String website = urlParser.doGet();
		// Get the teams
		String[] teams = urlParser.parseTeams(website);
		System.out.println(teams[0] + " - " + teams[1]);
		  
	}

}
