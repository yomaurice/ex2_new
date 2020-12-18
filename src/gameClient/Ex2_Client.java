package gameClient;

import Server.Game_Server_Ex2;
import api.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class Ex2_Client implements Runnable{
	private static MyFrame _win;
	private static Arena _ar;
	private static ArrayList<CL_Pokemon> cl_fs = new ArrayList<CL_Pokemon>();

	public static void main(String[] a) {
		Thread client = new Thread(new Ex2_Client());
		client.start();
	}

	@Override
	public void run() {

		int scenario_num = sample.Controller.getLev();
		//  while (scenario_num < 24) {
		game_service game = Game_Server_Ex2.getServer(scenario_num); // you have [0,23] games
		//int id = sample.controller.getid;
		//game.login(id);
		//        game_service game1 = new Game_ServerEx2();
		//        game1= (Game_ServerEx2) game;
		//        game1.level(scenario_num);
		String g = game.getGraph();
		System.out.println(g);

		directed_weighted_graph gg = GraphFromJson(g);
		String pks = game.getPokemons();
		//directed_weighted_graph gg = game.getJava_Graph_Not_to_be_used();

		init(game);

		game.startGame();
		_win.setTitle("Ex2 - OOP: (NONE trivial Solution) " + game.toString());
		int ind = 0;
		long dt = 300;

		while (game.isRunning()) {
			cl_fs = Arena.json2Pokemons(game.getPokemons());
			for(int a = 0;a<cl_fs.size();a++)
			{
				Arena.updateEdge(cl_fs.get(a),gg);
			}
			moveAgants(game, gg);
			try {
				if (ind % 3 == 0) {
					_win.repaint();
				}
				Thread.sleep(dt);
				ind++;
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		String res = game.toString();
		System.out.println(res);
		System.exit(0);
		//    scenario_num++;
	}


	/**
	 * Moves each of the agents along the edge,
	 * in case the agent is on a node the next destination (next edge) is chosen (randomly).
	 * @param game
	 * @param gg
	 * @param
	 */
	private static void moveAgants(game_service game, directed_weighted_graph gg) {
		String lg = game.move();
		List<CL_Agent> log = Arena.getAgents(lg, gg);
		_ar.setAgents(log);
		//ArrayList<OOP_Point3D> rs = new ArrayList<OOP_Point3D>();
		String fs =  game.getPokemons();
		List<CL_Pokemon> ffs = Arena.json2Pokemons(fs);
		_ar.setPokemons(ffs);
		for(int i=0;i<log.size();i++) {
			CL_Agent ag = log.get(i);
			int id = ag.getID();
			int dest = ag.getNextNode();
			int src = ag.getSrcNode();
			double v = ag.getValue();
			if(dest==-1) {
				dest = nextNode(gg, src);
				game.chooseNextEdge(ag.getID(), dest);
				System.out.println("Agent: "+id+", val: "+v+"   turned to node: "+dest);
			}
		}
	}
	/**
	 * a very simple random walk implementation!
	 * @param g
	 * @param src
	 * @return
	 */
	private static int nextNode(directed_weighted_graph g, int src) {

		double closest_node_dist=100;
		//Collection<edge_data> ee = g.getE(src);
		dw_graph_algorithms gr= new DWGraph_Algo();
		gr.init(g);
		List<node_data> shp= new ArrayList<node_data>();
		for(CL_Pokemon po:cl_fs){
			int pokemon_dest = po.get_edge().getDest();
			double dist = gr.shortestPathDist(src,pokemon_dest);
			if(closest_node_dist > dist)
			{
				closest_node_dist=dist;
				shp=gr.shortestPath(src,pokemon_dest);
			}
		}
		if (shp.size()>1)
		{
			return shp.get(1).getKey();
		}
		return shp.get(0).getKey();

		/*int ans = -1;
		Collection<edge_data> ee = g.getE(src);
		Iterator<edge_data> itr = ee.iterator();
		int s = ee.size();
		int r = (int)(Math.random()*s);
		int i=0;
		while(i<r) {itr.next();i++;}
		ans = itr.next().getDest();
		//System.out.println(ans);
		return ans;*/
	}

	private void init(game_service game) {
		String g = game.getGraph();
		String fs = game.getPokemons();
		//directed_weighted_graph gg = game.getJava_Graph_Not_to_be_used();
		directed_weighted_graph gg = GraphFromJson(g);
		//gg.init(g);
		_ar = new Arena();
		_ar.setGraph(gg);
		_ar.setPokemons(Arena.json2Pokemons(fs));
		_win = new MyFrame("test Ex2");
		_win.setSize(1000, 700);
		_win.update(_ar);


		_win.show();
		String info = game.toString();
		JSONObject line;
		try {
			line = new JSONObject(info);
			JSONObject ttt = line.getJSONObject("GameServer");
			int rs = ttt.getInt("agents");
			System.out.println(info);
			System.out.println(game.getPokemons());
			int src_node = 0;  // arbitrary node, you should start at one of the pokemon
			cl_fs = Arena.json2Pokemons(game.getPokemons());
			for(int a = 0;a<cl_fs.size();a++)
			{
				Arena.updateEdge(cl_fs.get(a),gg);
			}
			for(int a = 0;a<rs;a++)
			{
				int ind = a%cl_fs.size();
				CL_Pokemon c = cl_fs.get(ind);
				int nn = c.get_edge().getDest();
				if(c.getType()<0 ) {nn = c.get_edge().getSrc();}

				game.addAgent(nn);
			}
		}
		catch (JSONException e) {e.printStackTrace();}
	}

	public directed_weighted_graph GraphFromJson (String s) {
		directed_weighted_graph g = new DWGraph_DS(); // constructing new graph DWGraph_DS
		try
		{
			JSONTokener buffer = new JSONTokener(s); //converting file to json tokenizer
			JSONObject temp = new JSONObject(); //config temp json object to manipulate the buffer readings

			temp.put("graph", buffer.nextValue()); // add buffer tokenizer and graph string as a key
			JSONObject json_object = new JSONObject(); // config an original json object
			json_object=(JSONObject) temp.get("graph"); // extracting the value that appends graph key and insert into json object and casting

			JSONArray edgesList =new  JSONArray(); // configuring edge list and node list json arrays
			JSONArray nodesList =new  JSONArray();

			edgesList=(JSONArray) json_object.get("Edges"); // inserting values into both arrays using the original jason object
			nodesList=(JSONArray) json_object.get("Nodes");

			for(int i=0;i<nodesList.length();i++) // going over nodes list creating the nodes inserting positions and adding later to new graph
			{
				node_data N=new NodeData(nodesList.getJSONObject(i).getInt("id"));
				geo_location p=new GeoLocation(nodesList.getJSONObject(i).getString("pos"));
				N.setLocation(p);
				g.addNode(N);
			}
			for(int i=0;i<edgesList.length();i++) // going over edge list and connecting existing nodes to together
			{
				g.connect(edgesList.getJSONObject(i).getInt("src"), edgesList.getJSONObject(i).getInt("dest"), edgesList.getJSONObject(i).getDouble("w"));

			}

		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}

		return g;
	}


}