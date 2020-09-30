import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;

public class WordNet {
    private final HashMap<String, List<Integer>> map;
    private final HashMap<Integer, String> synset;
    private SAP sap;
    private int count;


    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) {
            throw new IllegalArgumentException("File is empty");
        }
        synset = new HashMap<>();
        map = new HashMap<>();
        count = 0;
        readS(synsets);
        readHypernyms(hypernyms);
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return map.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) {
            throw new IllegalArgumentException("No word");
        }
        return map.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException("Invalid");
        }
        return sap.length(map.get(nounA), map.get(nounB));

    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException("Invalid");
        }
        return synset.get(sap.ancestor(map.get(nounA), map.get(nounB)));
    }


    private void readS(String synsets) {
        In string = new In(synsets);
        while (string.hasNextLine()) {
            count++;
            String line = string.readLine();
            String[] lineBreak = line.split(",");
            if (lineBreak.length < 2) {
                continue;
            }
            int id = Integer.parseInt(lineBreak[0]);
            synset.put(id, lineBreak[1]);
            String[] synsetNouns = lineBreak[1].split(" ");
            for (String i : synsetNouns) {
                List<Integer> list = map.get(i);
                if (list != null) {
                    list.add(id);
                } else {
                    List<Integer> list2 = new ArrayList<>();
                    list2.add(id);
                    map.put(i, list2);
                }
            }
        }
    }

    private void readHypernyms(String hypernyms) {
        In string = new In(hypernyms);
        Digraph digraph = new Digraph(count);
        while ((string.hasNextLine())) {
            String line = string.readLine();
            String[] lineBreak = line.split(",");
            if (lineBreak.length < 2) continue;
            int id = Integer.parseInt(lineBreak[0]);
            for (int i = 1; i < lineBreak.length; i++) {
                int edges = Integer.parseInt(lineBreak[i]);
                digraph.addEdge(id, edges);
            }
        }
        DirectedCycle directedCycle = new DirectedCycle(digraph);
        if (directedCycle.hasCycle()) {
            throw new IllegalArgumentException("Input has a cycle");
        }
        int root = 0;
        for (int i = 0; i < count; i++) {
            if (digraph.outdegree(i) == 0) {
                root++;
            }
            if (root > 1) {
                throw new IllegalArgumentException("It has more than one root");
            }
        }
        sap = new SAP(digraph);
    }


    public static void main(String[] args) {
    }
}

