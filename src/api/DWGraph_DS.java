package api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;


public class DWGraph_DS implements directed_weighted_graph {

	private HashMap<Integer, node_data> vertices;
	//private HashMap<Integer, node_data> src_vertices;
	//private HashMap<Integer, node_data> dest_vertices;
	public Collection<node_data> GraphVertices;
	//public Collection<node_data> GraphSrcVertices;
	//public Collection<node_data> GraphDestVertices;
	public Collection<edge_data> edges;
	int nodeCounter = 0;
	int edgeCounter=0;
	int mc = 0;

	//empty constructor
	public DWGraph_DS() {
		vertices = new HashMap<Integer, node_data>();
		//src_vertices = new HashMap<Integer, node_data>();
		//dest_vertices = new HashMap<Integer, node_data>();
		GraphVertices = new ArrayList<node_data>();
		//GraphSrcVertices = new ArrayList<node_data>();
		//GraphDestVertices = new ArrayList<node_data>();
		edges=new ArrayList<edge_data>();
	}

	//constructor for the copy function creating a deep copy
	public DWGraph_DS(Collection<node_data> NewGraphVertices, Collection<edge_data> edges) {
		this.GraphVertices = NewGraphVertices;
		this.vertices = new HashMap<Integer, node_data>();
//		for (node_data verts : GraphVertices) {
//			addNode(verts);
//		}
		Iterator <node_data> ite1= GraphVertices.iterator();
		while (ite1.hasNext()){
			node_data n=ite1.next();
			addNode(n);
		}
		this.edges=new ArrayList<edge_data>();
		Iterator<edge_data> ite = edges.iterator();
		while (ite.hasNext())
		{
			edge_data edge = ite.next();
			int src = edge.getSrc();
			int dest = edge.getDest();
			double w = edge.getWeight();
			connect(src, dest, w);
		}
	}


	public node_data getNode(int key) {
		if (vertices.get(key) != null)
			return vertices.get(key);
		else
			return null;
	}
	

	public edge_data getEdge(int src, int dest) {
		if(hasEdge(src,dest))
		{
			//edge_data edge = new EdgeData();
			Iterator <edge_data> iter = edges.iterator();
			while (iter.hasNext())
			{
				edge_data e = iter.next();
				if (e.getDest() == dest && e.getSrc()==src)
					return e;
			}
			return null;
		}
		else
			return null;
	}

	public void addNode(node_data n) {
		boolean ForCountNodes = false;
		if (!vertices.containsValue(n) && n != null)
		{
//			((NodeData) n).clearSrcNeighbor();
//			((NodeData) n).clearDestNeighbor();
			vertices.put(n.getKey(), n);
			ForCountNodes = true;
		}
		if (ForCountNodes)//the node added to graph
			nodeCounter++;
	}

	public void addNode(int key)
	{
		node_data n = new NodeData(key);
		addNode(n);
	}

	public void connect(int src, int dest, double w) {
		if(vertices.containsKey(src) &&  src!=dest ){
			if(!hasEdge(src, dest)) {
				((NodeData) vertices.get(src)).addNi(vertices.get(dest));
				((NodeData) vertices.get(dest)).addDestNi(vertices.get(src));
				edgeCounter++;
				mc++;
				edge_data edge = new EdgeData(src,dest,w);
				edges.add(edge);
			}
			else {
				edge_data edge = getEdge(src, dest);
				edges.remove(edge);
				((EdgeData) edge).setWeight(w);
				edges.add(edge);
			}
		}
	}

	public Collection <node_data> getV() {
		if (vertices != null)
		{
			return vertices.values();
		}
		return null;
	}

	public Collection <edge_data> getE(int node_id) {
		NodeData src = (NodeData) vertices.get(node_id);
		Collection<edge_data> coll = new  ArrayList<edge_data>();
		Iterator<node_data> iter = src.getNi().iterator();
		while (iter.hasNext())
		{
			node_data b = iter.next();
			edge_data edge = getEdge(src.getKey(),b.getKey());
			coll.add(edge);
		}
		return coll;
	}

	//Maurice, Please check this method again/////////////////////////////
	public node_data removeNode(int key){
		if (vertices.containsKey(key))
		{
			NodeData nodeToRemove = (NodeData) vertices.get(key);
			Collection<node_data> neighborsOfDel = nodeToRemove.getNi();
			for (node_data neighbor : neighborsOfDel)
			{
				NodeData Ni = (NodeData) neighbor;
				edge_data e = getEdge (nodeToRemove.getKey(),Ni.getKey());
				Ni.removeDestNode(nodeToRemove);
				edges.remove(e);
				edgeCounter--;
			}
			Collection<node_data> neighborsOfDel2 = nodeToRemove.getDestNi();
			for (node_data neighbor : neighborsOfDel2)
			{
				NodeData Ni = (NodeData) neighbor;
				edge_data e = getEdge (Ni.getKey(),nodeToRemove.getKey());
				Ni.removeNode(nodeToRemove);
				edges.remove(e);
				edgeCounter--;
			}
			vertices.remove(key);
			nodeCounter--;
			neighborsOfDel.clear();
			neighborsOfDel2.clear();
			mc++;
			return nodeToRemove;
		}
		else return null;

	}
	/*public Collection<node_data> getSrcVertices(){
		if(vertices!=null)
		{
			return vertices.values();
		}
		return null;
	}*/
	/*public Collection<node_data> getDestVertices(){
		if(dest_vertices!=null)
		{
			return dest_vertices.values();
		}
		return null;
	}*/

	public edge_data removeEdge(int src, int dest) {
		if (src != dest && getNode(src) != null && getNode(dest) != null && hasEdge(src, dest)) {
			((NodeData) vertices.get(src)).removeNode(getNode(dest));
			((NodeData) vertices.get(dest)).removeDestNode(getNode(src));
			edge_data edge = getEdge(src,dest);
			edges.remove(edge);
			edgeCounter--;
			mc++;
		}
		return null;
	}

	public int nodeSize() {
		return nodeCounter;
	}

	public int edgeSize() {
		return edgeCounter;
	}

	public int getMC() {
		return mc;
	}

	public boolean hasEdge(int src, int dest) {
		if (vertices.get(src) == null || vertices.get(dest) == null || src == dest) return false;
//		if (((NodeData) vertices.get(src)).hasNi(dest))
		Iterator<edge_data> ed = edges.iterator();
		while (ed.hasNext()){
			edge_data edi=ed.next();
			if(edi.getSrc()==src && edi.getDest()==dest){
				return true;
			}
		}
		return false;
	}

	public Collection<edge_data> getEdges()
	{
		return edges;
	}

}