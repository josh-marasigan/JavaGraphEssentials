package JavaGraphEssentials;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class GraphTraversals {
	
	private Set<Integer> nodes; // set of nodes in the graph
	private Map<Integer, List<Integer>> edges;
	
	/* map between a node and a list of nodes that are 
	connected to it by outgoing edges
	 class invariant: fields "nodes" and "edges" are non-null;
	 "this" graph has no node that is not in "nodes" */
	public GraphTraversals() {
		nodes = new HashSet<Integer>();
		edges = new HashMap<Integer, List<Integer>>();
	}
	
	public String toString() {return "nodes=" + nodes + "; " + "edges=" + edges;}
	
	// postcondition: adds the node "n" to this graph
	public void addNode(int n) {nodes.add(n);}
	
	// Encapsulation: Getters/Setters
	public Set<Integer> getNodes() {return this.nodes;}
	public Map<Integer, List<Integer>> getEdges() {return this.edges;}
	public void setNodes(Set<Integer> newNodes) {this.nodes = newNodes;}
	public void setEdges(Map<Integer, List<Integer>> newEdges) {this.edges = newEdges;}
	
	// postcondition: adds a directed edge "from" -> "to" to this graph
	/* Class Invariant: 
	1. Fields "nodes" and "edges" are non-null
	2. "this" graph has no node that is not in "nodes" */
	public void addEdge(int from, int to) {
		// Class Invariant 1.
		if(nodes == null) {
			Set<Integer> newNodes = new HashSet<Integer>();
			this.setNodes(newNodes);
		}
		
		if(edges == null) {
			Map<Integer, List<Integer>> newEdges 
				= new HashMap<Integer, List<Integer>>();
			this.setEdges(newEdges);
		}
		
		// Get the current node from graph/map
		Map<Integer, List<Integer>> currentNode = this.getEdges();
		
		// Make sure node is already in map/Class Invariant 2.
		if(!currentNode.containsKey(from)) {
			Set<Integer> nodeList = this.getNodes();
			nodeList.add(from);
			this.setNodes(nodeList);
			
			// Add to map
			currentNode.put(from, new LinkedList<Integer>());
		}
		
		if(!currentNode.containsKey(to)) {
			Set<Integer> nodeList = this.getNodes();
			nodeList.add(to);
			this.setNodes(nodeList);
		}
		
		// Get the current nodes edges and add edge to destination node
		List<Integer> currentEdges = currentNode.get(from);
		
		// Add edge only if not there already
		if(!currentEdges.contains(to)) {
			currentEdges.add(to);
		}

		// Update the nodes edges and map
		currentNode.put(from, currentEdges);
		setEdges(currentNode);
	}
	
	/* postcondition: returns true if 
	
	(1) "sources" is a subset of "nodes", 
	
	(2) "targets" is a subset of "nodes", and 
	
	(3) for each node "m" in set "targets", there is NO node "n" in set "sources" 
	such that there IS a directed path that starts at "n" and ends at "m" in "this";
		
	and false otherwise */
	public boolean unreachable(Set<Integer> sources, Set<Integer> targets) {
		if (sources == null || targets == null) {
			throw new IllegalArgumentException(); 
		}
		
		// (1)
		Set<Integer> nodeSet = this.getNodes();
		for(Integer s_node : sources) {
			if(!nodeSet.contains(s_node)) {return true;}
		}
		
		// (2)
		for(Integer t_node : targets) {
			if(!nodeSet.contains(t_node)) {return true;}
		}
		
		// (3) Algorithm Gods forgive me for the terrible Time Complexity
		for(Integer target : targets) {
			for(Integer source : sources) {
				// If a directed path has been found, return false
				if(BFS(source,target)) {
					return false;
				}
			}
		}
		
		// BFS from any source to all target has no path. ie. unreachable
		return true;
	}
	
	/* Utility Breadth First Search Function for Graph Traversal */
	private boolean BFS(Integer source, Integer target) {
		
		// List all nodes as unvisited and later visited
		Set<Integer> nodes = this.getNodes();
		HashMap<Integer, Boolean> visited = new HashMap<Integer, Boolean>();
		for(Integer node : nodes) {
			visited.put(node, new Boolean(false));
		}
		
		// Queue for nodes to be visited
		LinkedList<Integer> queue = new LinkedList<Integer>();
		
		// Set 'source' as visited
		visited.put(source, new Boolean(true));
		
		// Start search at 'source'
		queue.add(source);
		
		// Adjacency list/Map of nodes w edges
		Map<Integer, List<Integer>> map = this.getEdges();
		
		// Search the graph by breadth
		boolean foundNode = false;
		while (queue.size() > 0) {
			
			// 'visit' next node in queue and its edges
			Integer current = queue.poll();
			
			// If node has been reached, flag as found
			if(current == target) {foundNode = true;}
			
			// If not found, check its child nodes from edges
			List<Integer> currentEdges = new LinkedList<Integer>();
			if(map.get(current) != null) {
				currentEdges = map.get(current);
			}
			else {
				continue;
			}
			
			// Go through current node edges
			for(Integer child : currentEdges) {
				
				// Child of node has NOT been visited
				if(!visited.get(child)) {
					visited.put(child, new Boolean(true));
					
					// Queue child node to be visited
					queue.add(child);
				}
			}
		}
		
		return foundNode;
	}
}
