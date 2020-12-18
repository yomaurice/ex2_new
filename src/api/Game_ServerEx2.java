package api;

import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Server.Game_Server_Ex2;
import gameClient.Arena;
import gameClient.CL_Agent;
import gameClient.CL_Pokemon;
import gameClient.Ex2_Client;
import java.util.Date;


public class Game_ServerEx2 implements game_service {

	private long time_of_game;
	private dw_graph_algorithms graph;
	private List<CL_Agent> agents;
	private List<CL_Pokemon> pokemons;
	long start_game_time;
	long end_game_time = 0;
	//private game_service game;
	private int num_of_agents;
	private int level;
	public Game_ServerEx2() {
		////////////time_of_game=///////////////////////////
		//game = Game_Server_Ex2.getServer(11);
		Date now = new Date();
		start_game_time = now.getTime();
	}


	@Override
	public String getGraph() {


		JSONObject f = (JSONObject) Game_Server_Ex2.getServer(level);
		String file = f.toString();


		if (graph.save(file)) {
			return file;
		}
		return null;


	}


	@Override
	public String getPokemons() {

		//JSONObject json_object = new JSONObject();
		JSONArray pokemon = new JSONArray();
		Iterator<CL_Pokemon> p = pokemons.iterator();

		while (p.hasNext()) {
			pokemon.put(p.next());
		}
		System.out.println(pokemon);
		System.out.println(pokemon.toString());
		return pokemon.toString();
	}

	@Override
	public String getAgents() {
		JSONArray agent = new JSONArray();
		Iterator<CL_Agent> p = agents.iterator();

		while (p.hasNext()) {
			agent.put(p.next());
		}
		System.out.println(agent);
		System.out.println(agent.toString());
		return agent.toString();
	}

	@Override
	public boolean addAgent(int start_node) {

		boolean success = true;
		Iterator<CL_Pokemon> p = pokemons.iterator();

		int i=0;
		while (i<amountOfAgents())
		{
			CL_Pokemon pikachu = p.next();
			int src = pikachu.get_edge().getSrc();
			CL_Agent agent = new CL_Agent((directed_weighted_graph) graph,src);
			agents.add(agent);
			i++;
		}
		return success;
	}

	@Override
	public long startGame() {
		end_game_time = 0;
		Date now = new Date();
		long time = now.getTime();
		start_game_time = time;
		return time;
	}

	@Override
	public boolean isRunning() {
		if (end_game_time == 0) {
			return true;
		}
		return false;
	}

	@Override
	public long stopGame() {
		Date now = new Date();
		end_game_time = now.getTime();
		return end_game_time;
	}

	@Override
	public long chooseNextEdge(int id, int next_node) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long timeToEnd() {
		long now = new Date().getTime();
		long passed_time = now - this.start_game_time;
		long time_to_end = this.time_of_game - passed_time;
		if (time_to_end < 0) {
			stopGame();
		}
		return time_to_end;
	}

	@Override
	public String move() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean login(long id) {
		if (id > 0) {
			return true;
		}
		return false;
	}
	public int amountOfAgents() {
		int n=0;
		JSONArray jsonServerReturn = (JSONArray) Game_Server_Ex2.getServer(level);
		try {
			n= (int) jsonServerReturn.get(jsonServerReturn.length() - 1);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return n;
	}

	public void level(int level){
		this.level=level;
	}

}
