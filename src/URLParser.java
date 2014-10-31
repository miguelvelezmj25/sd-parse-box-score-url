import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * @author Miguel Velez
 * @version 0.1
 * 
 * Parses a URL
 */

public class URLParser {
	private String charset;
	private String url;
	
	public URLParser(String url, String charset) {
		this.charset = charset;
		this.url = url;
	}
	
	/** Does a get request to the specified URL
	 * @throws IOException 
	 */
	public String doGet() throws IOException {
		BufferedReader input = null;
		HttpURLConnection connection = null;
		StringBuilder response = new StringBuilder();
		
		try {
			// Create URL
			URL url = new URL(this.getUrl());
			
			// Open the connection
			connection = (HttpURLConnection) url.openConnection();			
			// This is a GET request
			connection.setRequestMethod("GET");
			// Connect
			connection.connect();
			
			// Get the response code
			int responseCode = connection.getResponseCode();
			System.out.println("Response code: " + responseCode);
			
			// Get the response
			input = new BufferedReader(new InputStreamReader(connection.getInputStream(), this.getCharset()));
			String line = input.readLine();
			
			// Build the response
			while(line != null) {
				response.append(line);
				line = input.readLine();
			}
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			// Close objects
			input.close();
			connection.disconnect();
		}
		
		// Return response
		return response.toString();			
	}
	
	/** Parses HTML of a website and returns and Array with the 2 teams
	 */
	public String[] parseTeams(String website) {
		String[] teams = new String[2];
		String teamDelimeter = "vs.";
		String endTeams = "- Boxscore";
		String teamLocationStart = "<title>";
		String teamLocationFinish = "</title>";
		
		// Index where teams begin and end
		int teamStartIndex = website.indexOf(teamLocationStart);
		int teamFinishIndex = website.indexOf(teamLocationFinish);
		
		// Get the title from the website
		StringBuilder title = new StringBuilder(website.substring(teamStartIndex + teamLocationStart.length(),
				teamFinishIndex));
			
		// Index where the first and second team end
		int firstTeamEndIndex = title.indexOf(teamDelimeter);
		int secondTeamEndIndex = title.indexOf(endTeams);
		
		// Get the teams
		teams[0] = title.substring(0, firstTeamEndIndex).trim();
		teams[1] = title.substring(firstTeamEndIndex + teamDelimeter.length(), secondTeamEndIndex).trim();
		
		// Return
		return teams;
	}
	
	public ArrayList<String> parseStatHeader(String website, String team1, String team2) {
		ArrayList<String> headers = new ArrayList<String>();
		String startTeam1 = "</div>" + team1;
		String startTeam2 = "</div>" + team2;
		String startHeaders = ">Player</th>";
		String endHeaders = "</thead>";
		String startHeader = "<th align=\"right\" width=\"4%\">";
		
		// Index where team1 headers start
		int team1StartIndex = website.indexOf(startTeam1);
				
		// Get the title from the website
		String team1HeadersStart = website.substring(team1StartIndex);

		int team1FinishIndex = team1HeadersStart.indexOf(endHeaders);
		StringBuilder team1Headers = new StringBuilder(team1HeadersStart.substring(0, team1FinishIndex));
		System.out.println(team1Headers);
		
		
		return headers;
	}

	// Getters
	public String getUrl() { return this.url; }
	
	public String getCharset() { return this.charset; }

	// Setters
	
}
