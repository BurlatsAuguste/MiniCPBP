package minicpbp.examples;

import minicpbp.cp.Factory;
import static minicpbp.cp.Factory.makeDfs;
import minicpbp.engine.core.IntVar;
import minicpbp.engine.core.Solver;
import minicpbp.search.DFSearch;
import minicpbp.search.SearchStatistics;
import static minicpbp.cp.BranchingScheme.minEntropy;

import java.util.Arrays;

import static minicpbp.cp.BranchingScheme.*;

public class Simple {
    public static void main(String[] args) {
        Solver cp = Factory.makeSolver(false);

        IntVar[] q = Factory.makeIntVarArray(cp, 4, 1, 4);
        //IntVar[] q_different = [q[0], q[1], q[2]];
        cp.post(Factory.allDifferent(new IntVar[]{q[0], q[1], q[2]}));
        cp.post(Factory.sum(q, 7));
        cp.post(Factory.lessOrEqual(q[2], q[3]));

        DFSearch search = makeDfs(cp, minEntropy(q));
        cp.setTraceBPFlag(true);
        cp.setTraceSearchFlag(true);
        cp.setMaxIter(20);
        cp.setDynamicStopBP(true);
        cp.setVariationThreshold(0.00);

        search.onSolution(() ->
                System.out.println("solution:" + Arrays.toString(q))
        );
        SearchStatistics stats = search.solve(statistics -> statistics.numberOfSolutions() == 1000);

        System.out.format("#Solutions: %s\n", stats.numberOfSolutions());
        System.out.format("Statistics: %s\n", stats);
    }
}
