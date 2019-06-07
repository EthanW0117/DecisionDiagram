package Nodes;

public class INode extends Node{
	private int lowChild;
	private int highChild;
	private String var;
	
	public INode(int ID, String var, int lowChild, int highChild) {
		super(ID);
		this.var = var;
		this.lowChild = lowChild;
		this.highChild = highChild;
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
		return this.getId() + ":  " + this.var;
	}
}
