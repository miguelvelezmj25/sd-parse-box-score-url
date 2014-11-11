package com.mijecu.urlparser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.TreeMap;

/**
 * @author Miguel Velez
 * @version 1.0
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
	
	/** Returns an arrayList with the statistic headers
	 */
	public ArrayList<String> parseStatHeader(String website, String team) {
		ArrayList<String> headers = new ArrayList<String>();
		String startTeam = "</div>" + team;
		String endHeaders = "</thead>";
		String endHeader = "</th>";
		String startHeader = "<th align=\"right\" width=\"4%\">";
		// Getting the length of the startHeader
		int startHeaderLength = startHeader.length();
		// More generic version of startHeader
		startHeader = "<th align=\"right\"";
		
		// Index where team headers start
		int teamStartIndex = website.indexOf(startTeam);
		// Getting the string starting at the headers start
		String teamHeadersStart = website.substring(teamStartIndex);
		// Index where the headers end
		int teamFinishIndex = teamHeadersStart.indexOf(endHeaders);
		// String with only the headers
		StringBuilder teamHeaders = new StringBuilder(teamHeadersStart.substring(0, teamFinishIndex));
				
		// Loop through the string of headers adding and modifying the string
		int headerIndex = teamHeaders.indexOf(startHeader);
		
		while(headerIndex >=0) {
			teamHeaders = new StringBuilder(teamHeaders.substring(headerIndex + startHeaderLength));
			headers.add(teamHeaders.substring(0,teamHeaders.indexOf(endHeader)).trim());
			headerIndex = teamHeaders.indexOf(startHeader);
		}
		
		// Return headers
		return headers;
	}
	
	/** Returns a treeMap with a players and its statistics in another 
	 * treeMap
	 */
	public TreeMap<String, TreeMap<String, String>> parsePlayers(String website, String team, ArrayList<String> statHeaders) {
		TreeMap<String, TreeMap<String, String>> players = new TreeMap<String, TreeMap<String,String>>();
				
		String startTeam = "</div>" + team;
		String startPlayers = "<a href=\"http://espn.go.com/nhl/player/";
		String endPlayers = ">Player</th";
		
		// Index where team headers start
		int teamStartIndex = website.indexOf(startTeam);
		// Getting the string starting at the team start
		String teamStart = website.substring(teamStartIndex);
		// Index where players start
		int teamPlayertIndex = teamStart.indexOf(startPlayers);
		
		// Getting the string starting at the headers start
		StringBuilder teamPlayerStart = new StringBuilder(teamStart.substring(teamPlayertIndex));
		// End of all the players of the current team
		int teamPlayerEnd = teamPlayerStart.indexOf(endPlayers);
		teamPlayerStart = new StringBuilder(teamPlayerStart.substring(0, teamPlayerEnd));
		
		String startPlayer = ">";
		String endPlayer = "</a>";
		String startStat = "<td align=\"right\">";
		String endStat = "</td>";
		
		// Where the players start
		int startStats = teamPlayerStart.indexOf(startPlayer);
		
		 // Loop through all the players and add its stats
		while(startStats >=0) {
			// Create a new treeMap for each player
			TreeMap<String, String> statistics = new TreeMap<String, String>();
			// Get the player
			String player = teamPlayerStart.substring(startStats + startPlayer.length(), teamPlayerStart.indexOf(endPlayer));

			// Add all the stats for the player
			for(int i = 0; i < statHeaders.size(); i++) {
				// Modifying the string
				teamPlayerStart = new StringBuilder(teamPlayerStart.substring(teamPlayerStart.indexOf(startStat) + startStat.length()));
				// Get a specific stat
				statistics.put(statHeaders.get(i), teamPlayerStart.substring(0, teamPlayerStart.indexOf(endStat)));
				// Finding the start of the stats of the next player
				startStats = teamPlayerStart.indexOf(startStat);
				
			}
			
			// Add the player with their stats to the return treeMap
			players.put(player, statistics);
			
			//  Continue if there are more players
			if(teamPlayerStart.indexOf(startPlayers) >= 0) {
				teamPlayerStart = new StringBuilder(teamPlayerStart.substring(teamPlayerStart.indexOf(startPlayers)));
				startStats = teamPlayerStart.indexOf(startPlayer);		
			}			
		}
				
		return players;		
	}

	// Getters
	public String getUrl() { return this.url; }
	
	public String getCharset() { return this.charset; }

	// Setters
	
}
