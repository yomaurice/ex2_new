package api;

public class EdgeData implements edge_data {

	private int src;
	private int dest;
	private double weight;
	private String info;
	private int tag;

	public EdgeData() {
		tag=0;
		info="";
		//////////////////////////////
		//////////////////////////////
	}
	public EdgeData(int src,int dest,double weight) {
		tag=0;
		info="";
		this.src=src;
		this.dest=dest;
		this.weight= weight;
	}

	public int getSrc() {
		return src;
	}

	public int getDest() {
		return dest;
	}

	public double getWeight() {
		return weight;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String s) {
		this.info=s;		
	}

	public int getTag() {
		return tag;
	}

	public void setTag(int t) {
		this.tag=t;
	}
	
	public void setWeight(double w) {
		this.weight=w;
	}
	
}