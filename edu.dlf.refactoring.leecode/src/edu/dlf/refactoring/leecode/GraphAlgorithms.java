import java.util.List;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.HashSet;
import java.util.Stack;
import java.util.Queue;
import java.util.LinkedList;
import java.util.PriorityQueue;
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

		public DirectedGraph reverse() {
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

		public Iterable<Iterable<Integer>> strongComponents() {
			List<Iterable<Integer>> components = new ArrayList<
				Iterable<Integer>>();
			boolean[] marked = getMarked(V());
			int[] ids = new int[V()];
			DirectedGraph rg = reverse();
			Iterable<Integer> sequence = rg.topologicalSort();
			for(int s = getNextStart(marked, sequence), id = 0; s != -1;
					s = getNextStart(marked, sequence), id++) {
				DFS search = new DFS(this);
				search.search(s);
				Iterable<Integer> visited = search.getVisited(0);
				for(int v : visited) {
					if(!marked[v]) {
						ids[v] = id;
						marked[v] = true;
					}
				}
			}
			for(int id = 0; ;id++) {
				List<Integer> comp = new ArrayList<Integer>();
				for(int v = 0; v < ids.length; v++)
					if(ids[v] == id) comp.add(v);
				if(0 == comp.size())
					break;
				else
					components.add(comp);
			}
			return components;
		}

		private boolean[] getMarked(int v) {
			boolean[] marked = new boolean[v];
			for(int i = 0; i < marked.length; i ++)
				marked[i] = false;
			return marked;
		}

		private void mark(boolean[] marks, Iterable<Integer> vs) {
			for(int i : vs)
				marks[i] = true;
		}

		private int getNextStart(boolean[] marked, Iterable<Integer> 
				sequence) {
			for(int s : sequence)
				if(!marked[s]) return s;
			return -1;
		}

		private int getNextStart(boolean[] marked) {
			for(int i = 0; i < marked.length; i++)
				if(!marked[i]) return i;
			return -1;
		}

		public Iterable<Integer> getCircle() {
			boolean[] marked = getMarked(V());
			for(int s = 0; s!= -1; s = getNextStart(marked)) {
				DFS search = new DFS(this);
				search.search(s);
				if(null != search.getCircle())
					return search.getCircle();
				for(int i : search.getVisited(0)) 
					marked[i] = true;
			}
			return null;
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
		private List<Integer> circle;

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
					}else if(greyNodes.contains(n)) {
						this.circle = new ArrayList
							<Integer>();
						int st = stack.indexOf(n);
						for(int i = st; i < stack.size(); 
							    	i++) 
							circle.add(stack.elementAt(i));	
						circle.add(n);
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
		public Iterable<Integer> getCircle() {
			return circle;
		}
	}

	private static class Edge implements Comparable<Edge> {
		public final int From;
		public final int To;
		public final double Weight;
		
		public Edge(int From, int To, double Weight) {
			this.From = From;
			this.To = To;
			this.Weight = Weight;
		}
		
		public int other(int e) {
			return e == From ? To : From;
		}

		@Override
		public int compareTo(Edge that) {
			return this.Weight > that.Weight ? 1 : -1;
		}

		@Override
		public String toString() {
			return From + "->" + To;
		}	
	}
	private static class EdgeList extends ArrayList<Edge> {}

	private static class WeightedEdgeGraph implements IGraph{
		private final EdgeList[] edgeArrays;	

		public WeightedEdgeGraph(int V) {
			this.edgeArrays = new EdgeList[V];
			for(int i = 0; i < V; i ++)
				edgeArrays[i] = new EdgeList();
		}

		public int V() {
			return edgeArrays.length;
		}

		public int E() {
			int sum = 0;
			for(List<Edge> l : edgeArrays) {
				sum += l.size();
			}
			return sum/2;
		}
		public void addEdge(int f, int t, double w) {
			Edge e = new Edge(f, t, w);
			edgeArrays[f].add(e);
			edgeArrays[t].add(e);
		}	
		public Iterable<Integer> getNeighbors(int v) {
			List<Integer> results = new ArrayList<Integer>();
			for(Edge e : edgeArrays[v]) {
				results.add(e.other(v));
			}
			return results;
		}
		public Iterable<Edge> getEdges(int v) {
			return edgeArrays[v];
		}

		public static WeightedEdgeGraph createGraph() {
			WeightedEdgeGraph g = new WeightedEdgeGraph(8);
			g.addEdge(4, 5, 0.35);
			g.addEdge(4, 7, 0.37);
			g.addEdge(5, 7, 0.28);
			g.addEdge(0, 7, 0.16);
			g.addEdge(1, 5, 0.32);
			g.addEdge(0, 4, 0.38);
			g.addEdge(2, 3, 0.17);
			g.addEdge(1, 7, 0.19);
			g.addEdge(0, 2, 0.26);
			g.addEdge(1, 2, 0.36);
			g.addEdge(1, 3, 0.29);
			g.addEdge(2, 7, 0.34);
			g.addEdge(6, 2, 0.40);
			g.addEdge(3, 6, 0.52);
			g.addEdge(6, 0, 0.58);
			g.addEdge(6, 4, 0.93);
			return g;
		}
	}

	private static class MSTAlgorithms {

		public static Iterable<Edge> prim(WeightedEdgeGraph g) {
			List<Edge> mst = new ArrayList<Edge>();
			PriorityQueue<Edge> queue = new PriorityQueue<Edge>();
			boolean[] marked = new boolean[g.V()];
			for(int i = 0; i < marked.length; i++) marked[i] = false;
			primVisit(g, 0, marked, queue);
			while(queue.size() != 0) {
				Edge e = queue.poll();
				if(marked[e.From] && marked[e.To]) continue;
				mst.add(e);
				primVisit(g, marked[e.From] ? e.To : e.From, 
					marked, queue);
			}
			return mst;
		}

		private static void primVisit(WeightedEdgeGraph g, int v, 
				boolean[] marked, PriorityQueue<Edge> queue) {
			marked[v] = true;
			for(Edge e : g.getEdges(v)) {
				if(!marked[e.other(v)])
					queue.add(e);
			}
		}
	}

	private static <T> void print(Iterable<T> l) {
		System.out.println("===start===");
		for(T i : l)
			System.out.println(i.toString());
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
	
	private static DirectedGraph createDirectedGraph(int[] from, int[] to, 
			int V) {
		DirectedGraph g = new DirectedGraph(V);
		for(int i = 0; i < from.length; i++)
			g.addEdge(from[i], to[i]);
		return g;
	}
	private static DirectedGraph getDGraph() {
		int[] from = {0,0,0,2,2,3,5,6,6,7,8,9,9,9,11};
		int[] to = {1,5,6,0,3,5,4,4,9,6,7,10,11,12,12};
		return createDirectedGraph(from, to, 13);
	}
	
	private static DirectedGraph getDGraph2() {
		return createDirectedGraph(new int[]{0,3,4,5}, 
			new int[]{5,5,3,4},6);
	}
			
	private static void testDirectedGraph() {
		print(getDGraph().topologicalSort());
	}

	private static void testHasCircle() {
		print(getDGraph2().getCircle());
	}

	private static void testStrongComponents() {
		DirectedGraph g = getDGraph2();
		Iterable<Iterable<Integer>> comps = g.strongComponents();
		for(Iterable<Integer> c : comps)
			print(c);
	}

	private static void testMST() {
		WeightedEdgeGraph g = WeightedEdgeGraph.createGraph();
		print(MSTAlgorithms.prim(g));
	}

	public static void main(String[] args) {
		testMST();
	}


}
	
