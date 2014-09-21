import java.util.List;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.HashSet;
import java.util.Stack;
import java.util.Queue;
import java.util.LinkedList;
public class GraphAlgorithms {

	public static interface IGraph {
		Iterable<Integer> getNeighbors(int v);
		int V();
		int E();
	}
	public static class UndirectedGraph implements IGraph {
		protected static class AdjacencyList extends HashSet<Integer> {
		}

		protected final List<AdjacencyList> adjs;
		protected final int V;

		public UndirectedGraph(int V) {
			this.V = V;
			adjs = new ArrayList<AdjacencyList>();
			for(int i = 0; i < V; i ++)
				adjs.add(new AdjacencyList());
		}
		
		public int V() {
			return V;
		}
		public int E() {
			int sum = 0;
			for(AdjacencyList l : adjs) 
				sum += l.size();
			return sum / 2;
		}
		public void addEdge(int v, int w) {
			adjs.get(v).add(w);
			adjs.get(w).add(v);
		}
		public Iterable<Integer> getNeighbors(int v) {
			return adjs.get(v);
		}
	}

	public static class DirectedGraph extends UndirectedGraph {
		
		public DirectedGraph(int V) {
			super(V);
		}

		public void addEdge(int v, int w) {
			adjs.get(v).add(w);
		}

		public IGraph reverse() {
			DirectedGraph g = new DirectedGraph(this.V);
			for(int i = 0; i < this.adjs.size(); i++) {
				AdjacencyList list = adjs.get(i);
				for(int j : list) {
					g.addEdge(j, i);
				}
			}
			return g;
		}

		public Iterable<Integer> topologicalSort() {
			ISearch s = new TopologicalSearch(this);
			s.search(0);
			return s.getVisited(0);
		}

		public boolean hasCircle() {
			return false;
		}
	}
	
	private static interface ISearch {
		void search(int start);
		Iterable<Integer> getVisited(int kind);
	}

	private static class TopologicalSearch implements ISearch{
		private final IGraph g;
		private final HashSet<Integer> marked;
		private final List<Integer> allResults;

		public TopologicalSearch(DirectedGraph g) {
			this.g = g;
			this.marked = new HashSet<Integer>();
			this.allResults = new ArrayList<Integer>();
		}
		private int getNextStart() {
			for(int i = 0; i < g.V(); i++)
				if(!marked.contains(i))
					return i;
			return -1;
		}

		public void search(int start) {
			for(int s = start; s != -1; s = getNextStart()) {
				ISearch search = new DFS(this.g);
				search.search(s);
				Iterable<Integer> visited = search.getVisited(2);
				List<Integer> toAdd = new ArrayList<Integer>();
				for(int i : visited) {
					marked.add(i);
					if(!allResults.contains(i))
						toAdd.add(i);
				}
				allResults.addAll(0, toAdd);	
			}
		}

		public Iterable<Integer> getVisited(int kind) {
			return allResults;
		}
	}

	private static class BFS implements ISearch{
		
		private final IGraph g;
		private final List<Integer> list;

		public BFS(IGraph g) {
			this.g = g;
			this.list = new ArrayList<Integer>();
		}

		public void search(int s) {
			HashSet<Integer> greyNodes = new HashSet<Integer>();
			HashSet<Integer> blackNodes = new HashSet<Integer>();
			LinkedList<Integer> q = new LinkedList<Integer>();
			q.add(s);
			while(q.size() > 0) {
				int v = q.remove();
				this.list.add(v);
				blackNodes.add(v);
				Iterable<Integer> neis = g.getNeighbors(v);
				for(Integer i : neis) {
					if(!blackNodes.contains(i) && 
							!greyNodes.contains(i)) {
						q.add(i);
						greyNodes.add(i);
					}
				}
			}
		}
		
		public Iterable<Integer> getVisited(int kind) {
			return this.list;
		}

	}

	private static class DFS implements ISearch{
		private final IGraph g;
		private final List<Integer> preorder;
		private final List<Integer> postorder;
		private final List<Integer> postReverseOrder;

		public DFS(IGraph g) {
			this.g = g;
			this.preorder = new ArrayList<Integer>();
			this.postorder = new ArrayList<Integer>();
			this.postReverseOrder = new Stack<Integer>();
		}

		private void clear() {
			preorder.clear();
			postorder.clear();
			postReverseOrder.clear();
		}

		public void search(int start) {
			clear();
			HashSet<Integer> greyNodes = new HashSet();
			HashSet<Integer> blackNodes = new HashSet();
			Stack<Integer> stack = new Stack<Integer>();
			stack.push(start);
			while(!stack.empty()) {
				int p = stack.peek();
				if(!greyNodes.contains(p)) {
					preorder.add(p);
					greyNodes.add(p);
				}
				Iterable<Integer> neis = g.getNeighbors(p);
				boolean found = false;
				for(int n : neis) {
					if(!blackNodes.contains(n) &&
						!greyNodes.contains(n)) {
						stack.push(n);
						found = true;
						break;
					}
				}
				if(found) continue;
				postorder.add(p);
				postReverseOrder.add(0, p);
				greyNodes.remove(p);
				blackNodes.add(p);
				stack.pop();
			}
		}
		public Iterable<Integer> getVisited(int kind) {
			switch(kind) {
				case 0: return getPreorder();
				case 1: return getPostorder();
				case 2: return getPostReverseOrder();
				default: return null;
			}
		}

		public Iterable<Integer> getPreorder() {
			return preorder;
		}
		public Iterable<Integer> getPostorder() {
			return postorder;
		}
		public Iterable<Integer> getPostReverseOrder() {
			return postReverseOrder;
		}
	}

	private static void print(Iterable<Integer> l) {
		System.out.println("===start===");
		for(int i : l)
			System.out.println(i);
		System.out.println("===end===");
	}

	private static IGraph createUndirectedGraph() {
		int[] from = {0,0,0,0,5,5,3,4,7,9,9,9,11};
		int[] to = {1,2,6,5,3,4,4,6,8,10,11,12,12};	
		UndirectedGraph g = new UndirectedGraph(13);
		for(int i = 0; i < from.length; i++)
			g.addEdge(from[i], to[i]);
		return g;
	}
				
	private static void testDFS() {
		IGraph g = createUndirectedGraph();
		DFS s = new DFS(g);
		s.search(0);
		print(s.getPreorder());
		print(s.getPostorder());
		print(s.getPostReverseOrder());
		BFS bs = new BFS(g);
		bs.search(0);
		print(bs.getVisited(1));
	}
	
	private static void testDirectedGraph() {
		int[] from = {0,0,0,2,2,3,5,6,6,7,8,9,9,9,11};
		int[] to = {1,5,6,0,3,5,4,4,9,6,7,10,11,12,12};
		DirectedGraph g = new DirectedGraph(13);
		for(int i = 0; i < from.length; i++)
			g.addEdge(from[i], to[i]);
		print(g.topologicalSort());
	}

	public static void main(String[] args) {
		testDirectedGraph();
	}


}
	
