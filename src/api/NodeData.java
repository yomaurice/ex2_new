package api;

//import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashMap;

public class NodeData implements node_data,Comparable<NodeData> {

	private int key;
	private String info;
	private int tag;
	//keyCounter to insure uniqeness of each key;
	private static int keyCounter = 0;
	private HashMap<Integer, node_data> srcNeighbor = new HashMap<Integer, node_data>();
	private HashMap<Integer, node_data> destNeighbor = new HashMap<Integer, node_data>();
	private double weight;
	private geo_location pos = new GeoLocation();

	public NodeData() {
		tag = 0;
		info = " ";
		this.key = keyCounter;
		keyCounter++;
	}

	public NodeData(String info, int tag) {
		this.info = info;
		this.tag = tag;
		this.key = keyCounter;
		keyCounter++;
	}

	public NodeData(int key) {
		tag = 0;
		info = " ";
		this.key = key;
		keyCounter++;
	}

	public int getKey() {
		return key;
	}

	public geo_location getLocation() {
		return pos;
	}

	public void setLocation(geo_location p) {
		this.pos = p;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double w) {
		this.weight = w;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String s) {
		this.info = s;
	}

	public int getTag() {
		return tag;
	}

	public void setTag(int t) {
		this.tag = t;
	}


	public Collection<node_data> getNi() {
		return srcNeighbor.values();
	}
	public Collection<node_data> getDestNi() {
		return destNeighbor.values();
	}


	public boolean hasNi(int key) {
		if (srcNeighbor.containsKey(key)) return true;
		//if (destNeighbor.containsKey(key)) return true;
		return false;
	}


	public void addNi(node_data t) {
		if (t != null) {
			srcNeighbor.put(t.getKey(), t);
		}
	}
	public void addDestNi(node_data t) {
		if (t != null) {
			destNeighbor.put(t.getKey(), t);
		}
	}

	public void removeNode(node_data node) {
		if (hasNi(node.getKey())) {
			srcNeighbor.remove(node.getKey());
		}
	}
	public void removeDestNode(node_data node) {
		if (hasNi(node.getKey())) {
			destNeighbor.remove(node.getKey());
		}
	}

	@Override
	public int compareTo(NodeData node) {
		if (node.getWeight() > this.weight) return 1;
		else if(node.getWeight() < this.weight) return -1;
		else return 0;
	}


	public void clearSrcNeighbor() {
		srcNeighbor.clear();
	}
	public void clearDestNeighbor() {
		destNeighbor.clear();
	}

}