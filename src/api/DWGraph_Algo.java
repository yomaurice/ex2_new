package api;

import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
//import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
//import java.util.Map;
import java.util.Queue;
import java.util.Stack;

import org.json.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
//import org.json.parser.JSONParser;

import java.util.PriorityQueue;
//import java.util.Formatter;
import java.util.Scanner;
import java.io.*;
//import java.lang.*;
//import java.text.ParseException;
import java.io.FileWriter;

//import jdk.nashorn.internal.parser.JSONParser;
/*import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;*/

public class DWGraph_Algo implements dw_graph_algorithms {

	public directed_weighted_graph gr;
	int nodeCounter = 0;
	HashMap<node_data, node_data> fathers;
	//private Formatter x;
	Scanner scan;

	//empty constructor
	public DWGraph_Algo() {
		directed_weighted_graph gr = new DWGraph_DS();
		fathers = new HashMap<node_data, node_data>();
	}

	@Override
	public void init(directed_weighted_graph g) {
		this.gr = g;
	}

	@Override
	public directed_weighted_graph getGraph() {
		return gr;
	}

	@Override
	public directed_weighted_graph copy() {
		Collection<node_data> NewGraphVertices = ((DWGraph_DS) gr).getV();
		Collection<edge_data> edges = ((DWGraph_DS) gr).edges;
		directed_weighted_graph NewGraph = new DWGraph_DS(NewGraphVertices,edges);
		return NewGraph;
	}

	public void Dijkstra(NodeData node)// standard Dijkstra Algo. registers all tags of nodes in src branch of graph as dist from src.
	{
		fathers.put(node, null);
		//initializes all tags to infinity and set info to "unvisited"
		double inf = Double.POSITIVE_INFINITY;
		int intinf = (int) inf;
		Iterator<node_data> ite = gr.getV().iterator();
		while (ite.hasNext()) {
			node_data N = ite.next();
			N.setWeight(intinf);
			N.setInfo("unvisited");
		}
		//initializes the priority queue and strart first node
		PriorityQueue<NodeData> que = new PriorityQueue<NodeData>();
		node.setInfo("visited");
		node.setWeight(0.0);
		//System.out.println(que.size());
		que.add(node);
		//System.out.println(que.size());
		nodeCounter++;
		//standard algo for going through all nodes in graph
		while (!que.isEmpty()) {
			node_data top = que.poll();
			top.setInfo("visited");
			Collection<node_data> Nis = new ArrayList<node_data>();
			//gr.getV(top.getKey());
			Nis = ((NodeData)top).getNi();
			for (node_data nex : Nis) {

				if (nex.getInfo().equals("unvisited")) {
					que.add((NodeData) nex);
					nodeCounter++;
					nex.setInfo("visiting");
				}
				//resets tags if smaller dist
				if (nex.getWeight() > top.getWeight() + (gr.getEdge(top.getKey(), nex.getKey()).getWeight())) {
					//if (nex.getTag() > top.getTag() + (nex.getWeight())) {
					double n = top.getWeight() + (gr.getEdge(top.getKey(), nex.getKey()).getWeight());
					nex.setWeight(n);
					fathers.put(nex, top);
				}
			}

		}
	}

	//standard BFS algo. i added because it is more efficient, and could be used for isConnected function
	public void BFS(node_data node)// standard BFS Algo. registers all tags of nodes in src branch of graph as dist from src.
	{
		Iterator<node_data> ite = gr.getV().iterator();
		while (ite.hasNext()) {
			node_data N = ite.next();
			N.setTag(-1);
			N.setInfo("unvisited");
		}
		Queue<node_data> que = new LinkedList<node_data>();
		node.setInfo("visited");
		que.add(node);
		node.setTag(0);
		nodeCounter++;

		while (!que.isEmpty()) {
			node_data top = que.poll();
			top.setInfo("visited");
			Collection<node_data> Nis = new ArrayList<node_data>();
			Nis = ((NodeData)top).getNi();
			for (node_data nex : Nis) {

				if (nex.getInfo().equals("unvisited")) {
					que.add(nex);
					nodeCounter++;
					nex.setInfo("visiting");
				}
			}
		}
	}

	@Override
	public boolean isConnected() {
		if (gr.nodeSize() <= 1) return true;
		else if (gr.nodeSize() == 2 && gr.edgeSize() == 2) return true;
		else if (gr.edgeSize() < gr.nodeSize() - 1) return false;
		else {
			Iterator<node_data> ite = gr.getV().iterator();
			node_data src_node = ite.next();
			nodeCounter = 0;
			BFS(src_node);
			if (nodeCounter != gr.nodeSize()) return false;
			else
			{
				Iterator<node_data> ite1 = ReversGraph().getV().iterator();
				while (ite1.hasNext())
				{
					node_data node = ite.next();
					if (node!=src_node)
					{
						nodeCounter = 0;
						BFS(node);
						if (src_node.getInfo()!="visited")
						{
							return false;
						}
					}
				}
			}
			return true;
		}
	}

	public directed_weighted_graph ReversGraph()
	{
		directed_weighted_graph reverse_graph = new DWGraph_DS();
		Iterator<node_data> ite = gr.getV().iterator();
		while(ite.hasNext())
		{
			node_data n = ite.next();
			reverse_graph.addNode(n);
		}
		Iterator<edge_data> R_ite = ((DWGraph_DS) gr).getEdges().iterator();
		while(R_ite.hasNext())
		{
			edge_data edge = R_ite.next();
			int src = edge.getDest();
			int dest = edge.getSrc();
			double w = edge.getWeight();
			reverse_graph.connect(src, dest, w);
		}
		return reverse_graph;
	}

	@Override
	public double shortestPathDist(int src, int dest) {
		if (gr.getNode(src) != null)
		{
			if (src == dest) return 1;
			nodeCounter = 0;
			NodeData node = (NodeData) gr.getNode(src);
			Dijkstra(node);
			return gr.getNode(dest).getWeight();
		}
		return -1;
	}

	@Override
	public List<node_data> shortestPath(int src, int dest) {

		if (gr.getNode(src) != null)
		{
			Stack<node_data> st = new Stack<node_data>();
			NodeData node = (NodeData) gr.getNode(src);
			Dijkstra(node);
			node_data n = gr.getNode(dest);
			while (n != null) {
				st.add(n);
				n = fathers.get(n);
			}
			//flips the order to be correct
			List<node_data> temp = new ArrayList<node_data>(st.size());
			while (!st.isEmpty()) {
				temp.add(st.pop());
			}
			return temp;
		}
		return null;
	}
	/*
    @Override
    public boolean save(String file) {
        //	boolean flag=true;
        JSONObject obj = new JSONObject();
        JSONArray edges = new JSONArray();
        JSONArray nodes = new JSONArray();
        Collection<node_data> srcVertices = ((DWGraph_DS) gr).getSrcVertices();
        Iterator<node_data> ite = srcVertices.iterator();
        while (ite.hasNext()) {
            node_data n = ite.next();
            Collection<node_data> destVertices = ((NodeData) n).getNi();
            Iterator<node_data> iteN = destVertices.iterator();
            while (ite.hasNext()) {
                NodeData n1 = (NodeData) iteN.next();
                JSONArray list = new JSONArray();
                list.put(n.getKey());
                try {
					list.put(n1.getWeight());
				} catch (JSONException e) {
					e.printStackTrace();
				}
                list.put(n1.getKey());
                edges.put(list);
            }
        }
        Collection<node_data> vertices = gr.getV();
        Iterator<node_data> ite1 = vertices.iterator();
        while (ite1.hasNext()) {
            node_data n = ite1.next();
            JSONArray list1 = new JSONArray();
            list1.put(n.getLocation());
            list1.put(n.getKey());
            nodes.put(list1);
        }
        try {
			obj.put("edges", edges);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
        try {
			obj.put("nodes", nodes);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
        try (FileWriter f = new FileWriter(file)) {
            f.write(obj.toString());
//           x=new Formatter(file);
//            x= streamfile(file);
        } catch (Exception e) {
            System.out.println("you have an error");
            return false;
        }
//        x.format("%s%s%s%s%s%s%s%s%s%s%s","amountOfNodes: ",",",gr.nodeSize(),",","amountOfEdges: ",",",gr.edgeSize(),",","mc: ",",",gr.getMC());
//        x.format("%n", "");
//        Collection<node_data> col=gr.getV();
//        for(node_data node: col) {
//            x.format("%s%s%s%s%s" ,node.getKey(),",",node.getInfo(),",",node.getTag());
//            Collection<node_data> listOfNi=((NodeData)node).getNi();
//            for(node_data nis:listOfNi) {
//                x.format("%s%s",",", nis.getKey());
//                x.format("%s%s",",", gr.getEdge(node.getKey(),nis.getKey()));
//            }
//            x.format("%n", "");
//        }
        x.close();
        return true;
    }
	 */

	@Override
	public boolean save(String file) // save file using JSON format
	{
		JSONObject json_object = new JSONObject(); // original json object
		JSONArray edgesList = new  JSONArray(); // json array list for edges
		JSONArray nodesList = new  JSONArray(); // json array list for nodes

		Iterator<node_data> nodes = gr.getV().iterator(); // init iterator so I could add all the nodes.

		while(nodes.hasNext())
		{
			JSONObject json_temp = new JSONObject();
			node_data node_temp=nodes.next();

			try // adding nodes' id and pos to the nodeList array
			{
				json_temp.put("pos",((NodeData)node_temp).getLocation().toString());
				json_temp.put("id", node_temp.getKey());

				nodesList.put(json_temp);
			}
			catch (JSONException e)
			{
				e.printStackTrace();
				return false;
			}

			Iterator<node_data> node_neighbors = ((NodeData)node_temp).getNi().iterator();

			while(node_neighbors.hasNext())
			{
				node_data node_neighbors_temp=node_neighbors.next();
				json_temp = new JSONObject();

				try // adding the connections between the nodes in the graph into edgesList array.
				{
					if(node_neighbors_temp.getKey()!=node_temp.getKey())
					{
						json_temp.put("src",node_temp.getKey());
						json_temp.put("w", gr.getEdge(node_temp.getKey(), node_neighbors_temp.getKey()).getWeight());
						json_temp.put("dest",node_neighbors_temp.getKey());

						(edgesList).put(json_temp);
					}
				}
				catch (JSONException e)
				{
					e.printStackTrace();
					return false;
				}
			}
		}

		try // adding both arrays to original json object.
		{
			json_object.put("Edges", edgesList);
			json_object.put("Nodes", nodesList);
		}
		catch (JSONException e)
		{
			e.printStackTrace();
			return false;
		}

		try(FileWriter jsonFile = new FileWriter(file))
		{
			jsonFile.write(json_object.toString()); // opining a file writer to convert json object to string and add it to the file
			jsonFile.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public boolean load(String file)
	{
		JSONTokener buffer; // config json tokenizer so i can convert file to json tokenizer

		try
		{
			FileReader file_;
			try
			{
				file_ = new FileReader(file);
				buffer = new JSONTokener(file_); //converting file to json tokenizer
				JSONObject json_object_temp = new JSONObject(); //config temp json object to manipulate the buffer readings

				json_object_temp.put("graph", buffer.nextValue()); // add buffer tokenizer and graph string as a key
				JSONObject json_object = new JSONObject(); // config an original json object
				json_object=(JSONObject) json_object_temp.get("graph"); // extracting the value that appends graph key and insert into json object and casting

				JSONArray edgesList =new  JSONArray(); // configuring edge list and node list json arrays
				JSONArray nodesList =new  JSONArray();

				edgesList=(JSONArray) json_object.get("Edges"); // inserting values into both arrays using the original jason object
				nodesList=(JSONArray) json_object.get("Nodes");

				directed_weighted_graph g_copy = new DWGraph_DS(); // constructing new graph DWGraph_DS

				for(int i=0;i<nodesList.length();i++) // going over nodes list creating the nodes inserting positions and adding later to new graph
				{
					node_data N=new NodeData(nodesList.getJSONObject(i).getInt("id"));
					geo_location p=new GeoLocation(nodesList.getJSONObject(i).getString("pos"));
					N.setLocation(p);
					g_copy.addNode(N);
				}
				for(int i=0;i<edgesList.length();i++) // going over edge list and connecting existing nodes to together
				{
					g_copy.connect(edgesList.getJSONObject(i).getInt("src"), edgesList.getJSONObject(i).getInt("dest"), edgesList.getJSONObject(i).getDouble("w"));

				}

				init(g_copy); // init graph to be the new graph
				file_.close(); //closing file
			}
			catch (FileNotFoundException e2)
			{
				e2.printStackTrace();
				return false;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}
	/*
    @Override
    public boolean load(String file) {
        File f = new File(file);
        JSONParser parser = new JSONParser();
        directed_weighted_graph G = new DWGraph_DS();
        Object obj = new Object();
        try {
            obj = parser.parse(new FileReader(f));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
            }
//            catch (FileNotFoundException e) {
//
//                e.printStackTrace();
//            }
            JSONObject jsObj = (JSONObject) obj;
            edge_data[] edges = (edge_data[]) jsObj.get("Edges");
            node_data[] nodes = (node_data[]) jsObj.get("nodes");
            for (node_data i : nodes) {
                G.addNode(i);
            }
            for (edge_data i : edges) {
                G.connect(i.getSrc(), i.getDest(), i.getWeight());
            }
//        String ln=scan.nextLine();
//        String[] el=ln.split(",");
            // int mcCounter=Integer.parseInt(el[5]);
            //       for(node_data i: nodes) {
//            ln=scan.nextLine();
//            el=ln.split(",");
//            int i=0;
//            G.addNode(i.getKey());
//            node_data n=G.getNode(Integer.parseInt(el[0]));
//            n.setInfo(el[1]);
//            n.setTag(Integer.parseInt(el[2]));
//            i=3;
//            while(i<el.length) {
//                G.connect(n.getKey(),Integer.parseInt(el[i]),Double.parseDouble(el[i+1]));
//                i=i+2;
//            }
//        }
            //       scan.close();
            this.gr = G;
            if (((DWGraph_DS) gr).getSrcVertices().size() == G.nodeSize() && ((DWGraph_DS) gr).edgeCounter == G.edgeSize() && ((DWGraph_DS) gr).mc == G.getMC()) {
                return true;
            } else return false;
    }*/

}