import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    
    private final WordNet wordnet;
    
    public Outcast(WordNet wordnet) {
        this.wordnet = wordnet;
    }
    
    public String outcast(String[] nouns) {
        int len = nouns.length;
        int maxIndex = -1;
        int maxValue = Integer.MIN_VALUE;
        
        int[][] distance = new int[len][len];
        for (int i = 0; i < len - 1; i++) {
            for (int j = i + 1; j < len; j++) {
                distance[i][j] = wordnet.distance(nouns[i], nouns[j]);
            }
        }
        
        for (int i = 0; i < len; i++) {
            int sum = 0;
            for (int j = 0; j < len; j++) {
                if (j < i) sum += distance[j][i];
                if (j == i) continue;
                if (j > i) sum += distance[i][j];
            }
            if (sum > maxValue) {
                maxIndex = i;
                maxValue = sum;
            }
        }
        return nouns[maxIndex];
    }
    
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
