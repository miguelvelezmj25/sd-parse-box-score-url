package com.mijecu25.urlparser;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map.Entry;
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
		System.out.println("Teams: " + teams[0] + " - " + teams[1]);
		// Get stats headers
		ArrayList<String> headers = urlParser.parseStatHeader(website, teams[0]);

		TreeMap<String, TreeMap<String, TreeMap<String, String>>> teamsStats = new TreeMap<String, TreeMap<String, TreeMap<String, String>>>();
		
		// Putting the team statis in the TreeMap
		for(int i = 0; i < teams.length; i++) {					
			teamsStats.put(teams[i], urlParser.parsePlayers(website, teams[i], headers));
		}
		
		// Displaying stats
		for(Entry<String, TreeMap<String, TreeMap<String, String>>> team: teamsStats.entrySet()) {
			System.out.println("Team: " + team.getKey());
			
			// Get the players from each team
			TreeMap<String, TreeMap<String, String>> players = team.getValue();
			
			// Display the players stats
			for(Entry<String, TreeMap<String, String>> player: players.entrySet()) {
				System.out.println("\t" + player.getKey() + " - " + player.getValue());
			}

		}
			  
	}

}