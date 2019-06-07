package ADD;
import Nodes.*;
import java.util.HashMap;
import org.jgrapht.*;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

public class ADD {
	
	private HashMap<Double,TNode> terminal;
	private HashMap<Triple,INode> internal;
	private HashMap<Integer,Node> nodeMap;
	private HashMap<Integer,Integer> reduceCache;
	private HashMap<Integer,Double> maxMap;
	private HashMap<Integer,Double>	minMap;
	private int idCounter;
	
	public ADD() {
		
		terminal = new HashMap<Double,TNode>();
		internal = new HashMap<Triple,INode>();
		nodeMap = new HashMap<Integer, Node>();
		reduceCache = new HashMap<Integer,Integer>();
		maxMap = new HashMap<Integer,Double>();
		minMap = new HashMap<Integer,Double>();
		idCounter = 0;
		
	}
	
	
	public int getTNode(double value) {
		
		int id;
		if(!terminal.containsKey(value)) {
			id = idCounter;
			TNode newTNode = new TNode(idCounter, value);
			terminal.put(value, newTNode);
			nodeMap.put(id, newTNode);
			reduceCache.put(id, id);
			maxMap.put(id,value);
			minMap.put(id,value);
			idCounter++;
		} else {
			id = terminal.get(value).getId();
		}
		return id;
	}
	
	
	public int getINode(String var, int lowId, int highId) {
		
		Triple identifier = new Triple(var, lowId, highId);
		int id;
		id = internal.get(identifier).getId();
		
		if(lowId == highId) {
			id = lowId;
		}
		
		if(!internal.containsKey(identifier)) {
			id = idCounter;
			INode newINode = new INode(id, var, lowId, highId);
			internal.put(identifier, newINode);
			nodeMap.put(id, newINode);
			idCounter++;
		}
		
		return id;
	}
	
	
	public int reduce(int id){		
		if(!reduceCache.containsKey(id)) {
			INode i = (INode) nodeMap.get(id);
			int highChild = reduce(i.getHighChild());
			int lowChild = reduce(i.getLowChild());
			int reducedId = getINode(i.getVar(), highChild, lowChild);
			reduceCache.put(id,reducedId);
			
			return reducedId;
		} else {
			return reduceCache.get(id);
		}
		
//		if (nodeMap.get(id) instanceof TNode) {
//		//TNode t = (TNode) nodeMap.get(id);
//		reduceCache.put(id, id);
//		return id;
//	} else {
//		if(!reduceCache.containsKey(id)) {
//			INode i = (INode) nodeMap.get(id);
//			//Triple identifier = new Triple(i.getVar(),i.getLowChild(), i.getHighChild());
//			int highChild = reduce(i.getHighChild());				
//			int lowChild = reduce(i.getLowChild());				
//			int reducedId = getINode(i.getVar(),highChild,lowChild);
//			reduceCache.put(id, reducedId);
//			return reducedId;
//		}
//		return reduceCache.get(id);
//	}
	}
	
	public int findMax(Node node) {
		
		if(node instanceof TNode) {
			return node.getId();
		} else {
			INode e = (INode) node;
			int highChild = e.getHighChild();
			int maxId = findMax(nodeMap.get(highChild));
			maxMap.put(e.getId(),maxMap.get(maxId));
			return maxId;
		}
		
	}
	
	public int findMin(Node node) {
		if(node instanceof TNode) {
			return node.getId();
		} else {
			INode e = (INode) node;
			int lowChild = e.getLowChild();
			int minId = findMax(nodeMap.get(lowChild));
			minMap.put(e.getId(),minMap.get(minId));
			return minId;
		}
	}
	
	
//	public int computeResult(Node node1, Node node2, String operation) {
//		
//		int result = 0;
//		
//		switch (operation) {
//			case "Plus":{
//				if(node1 instanceof TNode && ((TNode)node1).getValue() == 0) {
//					result = node2.getId();
//				}else if(node2 instanceof TNode && ((TNode)node2).getValue() == 0) {
//					result = node1.getId();
//				}
//				break;
//			}
//			
//			case "Mult":{
//				if(node1 instanceof TNode && ((TNode)node1).getValue() == 1) {
//					result = node2.getId();
//				}else if(node2 instanceof TNode && ((TNode)node2).getValue() == 1) {
//					result = node1.getId();
//				}
//				break;
//			}
//			
//			case "Max":{
//				if(maxMap.get(node1.getId()) < minMap.get(node2.getId())) {
//					result = node2.getId();	
//				}else if(maxMap.get(node2.getId()) < minMap.get(node1.getId())){
//					result = node1.getId();
//				}
//				break;
//			}
//			
//			case "Min":{
//				if(maxMap.get(node1.getId()) < minMap.get(node2.getId())) {
//					result = node1.getId();	
//				}else if(maxMap.get(node2.getId()) < minMap.get(node1.getId())){
//					result = node2.getId();
//				}
//				break;
//			}
//			
//			case "Greater ":{
//				if()
//				
//			}
//			
////			case "
////		}
//		}
//		return result;
//	}
//	public int apply(int Node1, int Node2, String Operation) {
//		
//	}
	public static void main(String[] args) {
		
		ADD algDD = new ADD();
		algDD.getTNode(1.0);
		algDD.getTNode(7.0);
		algDD.getTNode(3.5);
		algDD.getTNode(-1.5);
		algDD.getINode("A", 0, 3);
		algDD.getINode("B", 2, 1);
		algDD.getINode("C", 4, 5);
		
		Graph<String, DefaultEdge> directedGraph = 
				new DefaultDirectedGraph<String, DefaultEdge>(DefaultEdge.class);
		for (int i = 0; i < algDD.idCounter; i++) {
			directedGraph.addVertex(algDD.nodeMap.get(i).toString());
		}
		for (int i = algDD.idCounter - 1; i >= 0; i--) {
			if (algDD.nodeMap.get(i) instanceof INode) {
				INode e = (INode) algDD.nodeMap.get(i);
				directedGraph.addEdge(e.toString(),algDD.nodeMap.get(e.getLowChild()).toString());
				directedGraph.addEdge(e.toString(),algDD.nodeMap.get(e.getHighChild()).toString());
			}
		}
		
	}
}
