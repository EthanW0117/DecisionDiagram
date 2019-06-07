package algDecDia;

import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.io.ComponentNameProvider;
import org.jgrapht.io.DOTExporter;
import org.jgrapht.io.ExportException;
import org.jgrapht.io.GraphExporter;

import node.INode;
import node.Node;
import node.TNode;

public class NewADD {
	
	private HashMap<Double,TNode> terminal;
	private HashMap<Triple,INode> internal;
	private HashMap<Integer,Node> nodeMap;
	private HashMap<Integer,Integer> reduceCache;
	//private HashMap<Integer,Double> maxMap;
	//private HashMap<Integer,Double>	minMap;
	private int idCounter;
	
	public NewADD() {
		
		terminal = new HashMap<Double,TNode>();
		internal = new HashMap<Triple,INode>();
		nodeMap = new HashMap<Integer, Node>();
		reduceCache = new HashMap<Integer,Integer>();
		//maxMap = new HashMap<Integer,Double>();
		//minMap = new HashMap<Integer,Double>();
		idCounter = 0;
	}
	
	
	public int getTNode(double value) {
		
		int id;
		if(!terminal.containsKey(value)) {
			id = idCounter;
			TNode newTNode = new TNode(idCounter, value);
			terminal.put(value, newTNode);
			nodeMap.put(id, newTNode);
			//maxMap.put(id,value);
			//minMap.put(id,value);
			idCounter++;
		} else {
			id = terminal.get(value).getId();
		}
		return id;
	}
	
	public int createINode(String var, int lowId, int highId) {
		int id = idCounter;
		Triple identifier = new Triple(var, lowId, highId);
		INode newINode = new INode(id, var, lowId, highId);
		internal.put(identifier, newINode);
		nodeMap.put(id, newINode);
		idCounter++;
		return id;
		
	}

	public int getINode(String var, int lowId, int highId) {
		
		if(lowId == highId) {
			return lowId;
		}
		Triple identifier = new Triple(var, lowId, highId);
		if (!internal.containsKey(identifier)) {
			return createINode(var, lowId, highId);
		}else {
			return internal.get(identifier).getId();
		}
	}
	
	public int reduce(int id){
		
		if(!reduceCache.containsKey(id)) {	
			Node n = (Node) nodeMap.get(id);
			if (n instanceof TNode) {
				TNode t = (TNode) n;
				reduceCache.put(id, id);
				return id;
				// Do whatever
			} else {
				INode i = (INode) nodeMap.get(id);
				//System.out.println(id);
				int highChild = reduce(i.getHighChild());
	//			System.out.println("high " + highChild);
				int lowChild = reduce(i.getLowChild());
	//			System.out.println("low " + lowChild);
	//			System.out.println(i.getVar() + lowChild + highChild);
				int reducedId = getINode(i.getVar(), lowChild, highChild);
	//			System.out.println("reduced " + reducedId);
				if(id != reducedId) {
					Triple identifier = new Triple(i.getVar(), i.getLowChild(), i.getHighChild());
					internal.remove(identifier);
					nodeMap.remove(id);
				}
				reduceCache.put(id,reducedId);
				return reducedId;
			}
		} else {
			return reduceCache.get(id);
		}
	}
	public static void main(String[] args) throws ExportException {
			
			NewADD algDD = new NewADD();
			algDD.getTNode(1.0);//1
			algDD.getTNode(7.0);//2
			algDD.getTNode(3.5);//3
			algDD.getTNode(-1.5);//4
			algDD.createINode("A", 0, 3);//5
			algDD.createINode("B", 2, 1);//6
			algDD.createINode("D",1,1);//7
			algDD.createINode("C", 6, 5);//8
			algDD.createINode("E", 7, 7);
			Graph<String, DefaultEdge> directedGraph1 = 
					new DefaultDirectedGraph<String, DefaultEdge>(DefaultEdge.class);
			for (int i : algDD.nodeMap.keySet()) {
				directedGraph1.addVertex(algDD.nodeMap.get(i).toString());
			}
			for (int i : algDD.nodeMap.keySet()) {
				if (algDD.nodeMap.get(i) instanceof INode) {
					INode e = (INode) algDD.nodeMap.get(i);
					directedGraph1.addEdge(e.toString(),algDD.nodeMap.get(e.getLowChild()).toString());
					directedGraph1.addEdge(e.toString(),algDD.nodeMap.get(e.getHighChild()).toString());
				}
			}
			System.out.println(directedGraph1);
			GraphExporter<String, DefaultEdge> exporter1 = new DOTExporter<>();
			Writer writer1 = new StringWriter();
			exporter1.exportGraph(directedGraph1, writer1);
			//System.out.println(writer1.toString());
			//System.out.println(algDD.terminal.keySet());
			System.out.println(algDD.nodeMap);
			System.out.println(algDD.internal.keySet());
			algDD.reduce(8);
//			System.out.println(algDD.reduce(7));
			System.out.println(algDD.internal.keySet());
			System.out.println(algDD.nodeMap);
			System.out.println(algDD.reduceCache);
			
			
			
			Graph<String, DefaultEdge> directedGraph = 
					new DefaultDirectedGraph<String, DefaultEdge>(DefaultEdge.class);
			for (int i : algDD.nodeMap.keySet()) {
				directedGraph.addVertex(algDD.nodeMap.get(i).toString());
			}
			for (int i : algDD.nodeMap.keySet()) {
				if (algDD.nodeMap.get(i) instanceof INode) {
					INode e = (INode) algDD.nodeMap.get(i);
					directedGraph.addEdge(e.toString(),algDD.nodeMap.get(e.getLowChild()).toString());
					directedGraph.addEdge(e.toString(),algDD.nodeMap.get(e.getHighChild()).toString());
				}
			}
			//System.out.println(directedGraph);
			GraphExporter<String, DefaultEdge> exporter = new DOTExporter<>();
			Writer writer = new StringWriter();
			exporter.exportGraph(directedGraph, writer);
			//System.out.println(writer.toString());
			
			
			Graph<Node, DefaultEdge> NewdirectedGraph =
					new DefaultDirectedGraph<Node, DefaultEdge>(DefaultEdge.class);
			for(int i : algDD.nodeMap.keySet()) {
				NewdirectedGraph.addVertex(algDD.nodeMap.get(i));
			}
			for (int i : algDD.nodeMap.keySet()) {
				if (algDD.nodeMap.get(i) instanceof INode) {
					INode e = (INode) algDD.nodeMap.get(i);
					NewdirectedGraph.addEdge(e,algDD.nodeMap.get(e.getLowChild()));
					NewdirectedGraph.addEdge(e,algDD.nodeMap.get(e.getHighChild()));
				}
			}
			ComponentNameProvider<Node> idProvider = new ComponentNameProvider<Node>() {
				public String getName(Node node) {
					return "id"+node.toString().replace(":", "_").replace(".", "_").replace("-", "m");
				}
			};
			ComponentNameProvider<Node> IDProvider = new ComponentNameProvider<Node>() {
				public String getName(Node node) {
					return node.toString();
				}
			};
//	        
			GraphExporter<Node, DefaultEdge> Newexporter = new DOTExporter<>(idProvider,null,null);
			Writer Newriter = new StringWriter();
			Newexporter.exportGraph(NewdirectedGraph, Newriter);
			System.out.print(Newriter.toString());
		}
}

