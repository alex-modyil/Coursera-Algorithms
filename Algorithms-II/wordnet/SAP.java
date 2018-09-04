
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.SeparateChainingHashST;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdIn;


public class SAP {
    
    private final Digraph G;
    private class Vertex {
        private final int id;
        private final int depth;
        public Vertex(int id, int depth) {
            this.id = id;
            this.depth = depth;
        }
        public int getId() {
            return id;
        }
        public int getDepth() {
            return depth;
        }
        public String toString() {
            return String.format("id:%d, depth: %d", id, depth);
        }
    }
    
    public SAP(Digraph G) {
        if (null == G) throw new java.lang.IllegalArgumentException();
        this.G = new Digraph(G);
    }
    
    public int length(int v, int w) {
        Queue<Integer> lst1 = new Queue<Integer>();
        Queue<Integer> lst2 = new Queue<Integer>();
        lst1.enqueue(v);
        lst2.enqueue(w);
        return source(lst1, lst2)[1];
    }
    
    public int ancestor(int v, int w) {
        Queue<Integer> lst1 = new Queue<Integer>();
        Queue<Integer> lst2 = new Queue<Integer>();
        lst1.enqueue(v);
        lst2.enqueue(w);
        return source(lst1, lst2)[0];
    }
    
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        return source(v, w)[1];
    }
    
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        return source(v, w)[0];
    }
    
    private int[] source(Iterable<Integer> v, Iterable<Integer> w) {
        int[] result = new int[2];
        SeparateChainingHashST<Integer, Vertex> visitedVertex1 = new SeparateChainingHashST<Integer, Vertex>();
        SeparateChainingHashST<Integer, Vertex> visitedVertex2 = new SeparateChainingHashST<Integer, Vertex>();
        Queue<Vertex> q1 = new Queue<Vertex>();
        Queue<Vertex> q2 = new Queue<Vertex>();
        int depth1 = 1;
        int depth2 = 1;
        boolean isStop1 = false;
        boolean isStop2 = false;
        
        if (null == v || null == w) throw new java.lang.IllegalArgumentException();
        for (int it : v) {
            checkRange(it);
            q1.enqueue(new Vertex(it, 0));
        }
        for (int it : w) {
            checkRange(it);
            q2.enqueue(new Vertex(it, 0));
        }
        
        int shortestAncestor = -1;
        int shortestDistance = Integer.MAX_VALUE;
        
        while (!isStop1 || !isStop2) {
            while (!isStop1) {
                if (q1.isEmpty() || depth1 > shortestDistance) {
                    isStop1 = true;
                    break;
                }
                
                if (q1.peek().depth >= depth1) {
                    depth1++;
                    break;
                }
                
                Vertex first = q1.dequeue();
                int id = first.getId();
                if (visitedVertex1.contains(id)) continue;
                
                if (visitedVertex2.contains(id)) {
                    int distance = visitedVertex2.get(id).getDepth() + first.getDepth();
                    if (distance < shortestDistance) {
                        shortestDistance = distance;
                        shortestAncestor = id;
                    }
                }
                
                for (int it : this.G.adj(id)) {
                    if (visitedVertex1.contains(it)) continue;
                    q1.enqueue(new Vertex(it, depth1));
                }
                
                visitedVertex1.put(id, first);
            }
            
            while (!isStop2) {
                if (q2.isEmpty() || depth2 > shortestDistance) {
                    isStop2 = true;
                    break;
                }
                
                if (q2.peek().depth >= depth2) {
                    depth2++;
                    break;
                }
                
                Vertex first = q2.dequeue();
                int id = first.getId();
                if (visitedVertex2.contains(id)) continue;
                if (visitedVertex1.contains(id)) {
                    int distance = visitedVertex1.get(id).getDepth() + first.getDepth();
                    if (distance < shortestDistance) {
                        shortestDistance = distance;
                        shortestAncestor = id;
                    }
                }
                
                for (int it : this.G.adj(first.id)) {
                    if (visitedVertex2.contains(it)) continue;
                    q2.enqueue(new Vertex(it, depth2));
                }
                
                visitedVertex2.put(id, first);
            }
        }
        
        if (shortestAncestor == -1) {
            result[0] = -1;
            result[1] = -1;
        } else {
            result[0] = shortestAncestor;
            result[1] = shortestDistance;
        }
        return result;
    }
    
    private void checkRange(int arg) {
        if (null == this.G) throw new java.lang.IllegalArgumentException();
        if (arg >= this.G.V() || arg < 0) {
            throw new java.lang.IllegalArgumentException();
        }
    }
    
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        System.out.println("graph constructs finished");
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sap.length(v, w);
            StdOut.printf("length = %d\n", length);
        }
    }
}