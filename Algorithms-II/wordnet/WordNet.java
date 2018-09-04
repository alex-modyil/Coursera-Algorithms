
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.SeparateChainingHashST;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.Graph;
import edu.princeton.cs.algs4.CC;

public class WordNet {
    
    private final SAP sap;
    private final SeparateChainingHashST<Integer, String> id2Nouns = new SeparateChainingHashST<Integer, String>();
    private final SeparateChainingHashST<String, Queue<Integer>> nouns2Ids = new SeparateChainingHashST<String, Queue<Integer>>();
    
    public WordNet(String synsets, String hypernyms) {
        In in = new In(synsets);
        String lines = null;
        while (in.hasNextLine()) {
            lines = in.readLine();
            String[] fields = lines.split(",");
            String[] nouns = fields[1].trim().split(" ");
            int id = Integer.parseInt(fields[0].trim());
            this.id2Nouns.put(Integer.valueOf(fields[0].trim()), fields[1].trim());
            for (int i = 0; i < nouns.length; i++) {
                String noun = nouns[i].trim();
                if (!nouns2Ids.contains(noun))
                    nouns2Ids.put(noun, new Queue<Integer>());
                nouns2Ids.get(noun).enqueue(id);
            }
        }        
        
        Digraph G = new Digraph(id2Nouns.size());
        
        in = new In(hypernyms);
        while (in.hasNextLine()) {
            lines = in.readLine();
            String[] fields = lines.split(",");
            int src = Integer.parseInt(fields[0].trim());
            for (int i = 1; i < fields.length; i++) {
                int dst = Integer.parseInt(fields[i].trim());
                G.addEdge(src, dst);
            }
        }
        
        checkGraph(G);            
        this.sap = new SAP(G);
    }
    
    public Iterable<String> nouns() {
        return nouns2Ids.keys();
    }
    
    public boolean isNoun(String word) {
        return nouns2Ids.contains(word);
    }
    
    public int distance(String nounA, String nounB) {
        checkNoun(nounA);
        checkNoun(nounB);
        Queue<Integer> id1 = nouns2Ids.get(nounA);
        Queue<Integer> id2 = nouns2Ids.get(nounB);
        return sap.length(id1, id2);
    }
    
    public String sap(String nounA, String nounB) {
        checkNoun(nounA);
        checkNoun(nounB);
        Queue<Integer> id1 = nouns2Ids.get(nounA);
        Queue<Integer> id2 = nouns2Ids.get(nounB);
        int ancestor = sap.ancestor(id1, id2);
        return id2Nouns.get(ancestor);
    }
    
    private void checkNoun(String noun) {
        if (null == noun) throw new java.lang.IllegalArgumentException();
        if (!isNoun(noun)) throw new java.lang.IllegalArgumentException();
    }
    
    private void checkGraph(Digraph G) {
        DirectedCycle dc = new DirectedCycle(G);
        if (dc.hasCycle()) throw new java.lang.IllegalArgumentException();
        
        int counter = 0;
        for (int i = 0; i < G.V(); i++) {
            if (G.outdegree(i) == 0) counter++;
            if (counter >= 2) throw new java.lang.IllegalArgumentException();
        }
        
        Graph g = new Graph(G.V());
        for (int i = 0; i < G.V(); i++) {
            for (int w : G.adj(i)) {
                g.addEdge(i, w);
                g.addEdge(w, i);
            }
        }
        CC cc = new CC(g);
        if (cc.count() != 1) throw new java.lang.IllegalArgumentException();
    }
    
    public static void main(String[] args) {
        WordNet wn = new WordNet(args[0], args[1]);
        while (!StdIn.isEmpty()) {
            String v = StdIn.readString();
            String w = StdIn.readString();
            int length   = wn.distance(v, w);
            String ancestor = wn.sap(v, w);
            StdOut.printf("length = %d, ancestor = %s\n", length, ancestor);
        }
    }
}