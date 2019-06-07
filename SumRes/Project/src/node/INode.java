package node;

public class INode extends Node{
	public int lowChild;
	public int highChild;
	public String var;
//	public double _min; // maintain during Reduce operation, display when you show graph
//	public double _max;
	
	public INode(int ID, String var, int lowChild, int highChild) {
		super(ID);
		this.var = var;
		this.lowChild = lowChild;
		this.highChild = highChild;
		this._min = Double.NaN;
		this._max = Double.NaN;
		// TODO Auto-generated constructor stub
	}
	
	public int getLowChild() {
		return this.lowChild;
	}
	
	public int getHighChild() {
		return this.highChild;
	}
	
	public String getVar() {
		return this.var;
	}
	
	public String toString() {
		return this.getId() + ":" + this.var + "(" + _min + "," + _max +")";
	}
}
