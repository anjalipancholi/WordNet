import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;

public class SAP {
    private final Digraph digraph;

    public SAP(Digraph G) {
        if (G == null) {
            throw new IllegalArgumentException("Invalid argument");
        }
        digraph = new Digraph(G);
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        int[] result = sap(v, w);
        return result[0];
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        int[] result = sap(v, w);
        return result[1];
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        int[] result = sap(v, w);
        return result[0];
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        int[] result = sap(v, w);
        return result[1];
    }

    private int[] sap(int v, int w) {
        validVertex(v);
        validVertex(w);
        BreadthFirstDirectedPaths vPath = new BreadthFirstDirectedPaths(digraph, v);
        BreadthFirstDirectedPaths wPath = new BreadthFirstDirectedPaths(digraph, w);
        int[] result = new int[2];
        int distance = Integer.MAX_VALUE;
        int ancestor = -1;
        for (int i = 0; i < digraph.V(); i++) {
            if (wPath.hasPathTo(i) && vPath.hasPathTo(i)) {
                int path = vPath.distTo(i) + wPath.distTo(i);
                if (distance > path) {
                    distance = path;
                    ancestor = i;
                }
            }
        }
        if (ancestor == -1) {
            result[0] = -1;
            result[1] = -1;
        } else {
            result[0] = distance;
            result[1] = ancestor;
        }
        return result;

    }

    private int[] sap(Iterable<Integer> v, Iterable<Integer> w) {
        validVertices(v);
        validVertices(w);
        BreadthFirstDirectedPaths vPath = new BreadthFirstDirectedPaths(digraph, v);
        BreadthFirstDirectedPaths wPath = new BreadthFirstDirectedPaths(digraph, w);
        int[] result = new int[2];
        int distance = Integer.MAX_VALUE;
        int ancestor = -1;
        for (int i = 0; i < digraph.V(); i++) {
            if (vPath.hasPathTo(i) && vPath.hasPathTo(i)) {
                int path = vPath.distTo(i) + wPath.distTo(i);
                if (distance > path) {
                    distance = path;
                    ancestor = i;
                }
            }
        }
        if (ancestor == -1) {
            result[0] = -1;
            result[1] = -1;
        } else {
            result[0] = distance;
            result[1] = ancestor;

        }
        return result;
    }

    private void validVertex(int v) {
        int n = digraph.V();
        if (v < 0 || v >= n) {
            throw new IllegalArgumentException("Invalid input");
        }
    }

    private void validVertices(Iterable<Integer> vertices) {
        if (vertices == null) {
            throw new IndexOutOfBoundsException("Invalid input");
        }
        int n = digraph.V();
        for (int i : vertices) {
            if (i < 0 || i >= n) {
                throw new IndexOutOfBoundsException("Invalid input");
            }
        }
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}

