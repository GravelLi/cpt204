import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        TaskAProcessor processor = new TaskAProcessor();

        TaskAResult resultA = processor.processDataset("Dataset A", "candidates_A.csv");
        TaskAResult resultB = processor.processDataset("Dataset B", "candidates_B.csv");
        TaskAResult resultC = processor.processDataset("Dataset C", "candidates_C.csv");

        runTaskA(resultA, resultB, resultC);

        runTaskB(resultA, resultB, resultC);
    }

    private static void runTaskA(TaskAResult resultA, TaskAResult resultB, TaskAResult resultC) {
        PrintStream originalOut = System.out;
        PrintStream fileOut = null;

        try {
            fileOut = new PrintStream("task_a_output.txt");
            System.setOut(new TeePrintStream(originalOut, fileOut));
        } catch (FileNotFoundException e) {
            System.out.println("Warning: could not open task_a_output.txt for writing. Continuing with console output only.");
        }

        try {
            printResult(resultA);
            printResult(resultB);
            printResult(resultC);

            printSummaryTable(resultA, resultB, resultC);
        } finally {
            System.out.flush();
            System.setOut(originalOut);

            if (fileOut != null) {
                fileOut.close();
            }
        }
    }

    private static void printResult(TaskAResult result) {
        System.out.println("==================================================");
        System.out.println(result.getDatasetName());
        System.out.println("==================================================");

        System.out.printf("Bubble Sort Average Time: %.3f ms%n", result.getBubbleTime());
        System.out.printf("Quick Sort Average Time:  %.3f ms%n", result.getQuickTime());
        System.out.printf("Merge Sort Average Time:  %.3f ms%n", result.getMergeTime());

        System.out.println();
        System.out.println("Top 10 Selected Locations:");

        Location[] topTenLocations = result.getTopTenLocations();

        for (int i = 0; i < topTenLocations.length; i++) {
            System.out.println((i + 1) + ". " + topTenLocations[i]);
        }

        System.out.println();
    }

    private static void printSummaryTable(TaskAResult resultA, TaskAResult resultB, TaskAResult resultC) {
        System.out.println("==================================================");
        System.out.println("Task A Summary Table");
        System.out.println("==================================================");

        System.out.printf("%-12s %-18s %-18s %-18s %-50s%n",
                "Dataset",
                "Bubble(ms)",
                "Quick(ms)",
                "Merge(ms)",
                "Top 10 Selected Locations");

        System.out.println("------------------------------------------------------------------------------------------------------------------------");

        printSummaryRow(resultA);
        printSummaryRow(resultB);
        printSummaryRow(resultC);

        System.out.println();
    }

    private static void printSummaryRow(TaskAResult result) {
        System.out.printf("%-12s %-18.3f %-18.3f %-18.3f %-50s%n",
                result.getDatasetName(),
                result.getBubbleTime(),
                result.getQuickTime(),
                result.getMergeTime(),
                topTenToString(result.getTopTenLocations()));
    }

    private static String topTenToString(Location[] topTenLocations) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < topTenLocations.length; i++) {
            builder.append(topTenLocations[i].getLocationId());

            if (i < topTenLocations.length - 1) {
                builder.append(", ");
            }
        }

        return builder.toString();
    }

    /**
     * Runs all Task B logic and mirrors every console line to
     * task_b_output.txt for inclusion in the report appendix.
     * Task A output is not duplicated to the file.
     */
    private static void runTaskB(TaskAResult resultA, TaskAResult resultB, TaskAResult resultC) {
        PrintStream originalOut = System.out;
        PrintStream fileOut = null;

        try {
            fileOut = new PrintStream("task_b_output.txt");
            System.setOut(new TeePrintStream(originalOut, fileOut));
        } catch (FileNotFoundException e) {
            System.out.println("Warning: could not open task_b_output.txt for writing. Continuing with console output only.");
        }

        try {
            executeTaskB(resultA, resultB, resultC);
        } finally {
            System.out.flush();
            System.setOut(originalOut);
            if (fileOut != null) {
                fileOut.close();
            }
        }
    }

    private static void executeTaskB(TaskAResult resultA, TaskAResult resultB, TaskAResult resultC) {
        Graph graph = new Graph();
        graph.loadFromCSV("paths.csv");

        Dijkstra dijkstra = new Dijkstra(graph);
        BFS bfs = new BFS(graph);

        Location[] topA = resultA.getTopTenLocations();
        Location[] topB = resultB.getTopTenLocations();
        Location[] topC = resultC.getTopTenLocations();

        String a1 = topA[0].getLocationId();
        String a10 = topA[9].getLocationId();

        String b1 = topB[0].getLocationId();
        String b5 = topB[4].getLocationId();

        String c1 = topC[0].getLocationId();
        String c5 = topC[4].getLocationId();

        PathResult case1 = dijkstra.findShortestPath(a1, a1);
        PathResult case2 = dijkstra.findShortestPath(a1, a10);
        PathResult case3 = dijkstra.findPathThroughWaypoints(new String[]{a1, b5, b1});
        PathResult case4 = dijkstra.findPathThroughWaypoints(new String[]{a1, b5, c5, c1});

        System.out.println("==================================================");
        System.out.println("Task B Shortest Path Results");
        System.out.println("==================================================");

        printPathCase("Case 1", a1, a1, "None", case1);
        printPathCase("Case 2", a1, a10, "None", case2);
        printPathCase("Case 3", a1, b1, b5, case3);
        printPathCase("Case 4", a1, c1, b5 + " -> " + c5, case4);

        printTaskBSummaryTable(
                "Case 1", a1, a1, "None", case1,
                "Case 2", a1, a10, "None", case2,
                "Case 3", a1, b1, b5, case3,
                "Case 4", a1, c1, b5 + " -> " + c5, case4
        );

        printBFSComparison(bfs, a1, a10, b1, b5, c1, c5, case2, case3, case4);
    }

    private static void printPathCase(String caseName, String start, String destination, String waypoint, PathResult result) {
        System.out.println(caseName);
        System.out.println("Start Node: " + start);
        System.out.println("Destination Node: " + destination);
        System.out.println("Waypoint(s): " + waypoint);

        if (result.getTotalCost() == Integer.MAX_VALUE) {
            System.out.println("Shortest Path: No path found");
            System.out.println("Total Cost: INF");
        } else if (result.hasSegments()) {
            // per-segment breakdown for waypoint queries (Case 3 / Case 4)
            ArrayList<PathResult> segments = result.getSegments();
            for (int i = 0; i < segments.size(); i++) {
                PathResult segment = segments.get(i);
                String[] segPath = segment.getPath();
                String segStart = segPath[0];
                String segEnd = segPath[segPath.length - 1];
                System.out.printf("  Segment %d (%s -> %s): %s, cost = %d%n",
                        i + 1, segStart, segEnd, segment.getPathString(), segment.getTotalCost());
            }
            System.out.println("Full path: " + result.getPathString());
            System.out.println("Total cost: " + result.getTotalCost());
        } else {
            System.out.println("Shortest Path: " + result.getPathString());
            System.out.println("Total Cost: " + result.getTotalCost());
        }

        System.out.println();
    }

    private static void printTaskBSummaryTable(
            String case1Name, String case1Start, String case1Destination, String case1Waypoint, PathResult case1Result,
            String case2Name, String case2Start, String case2Destination, String case2Waypoint, PathResult case2Result,
            String case3Name, String case3Start, String case3Destination, String case3Waypoint, PathResult case3Result,
            String case4Name, String case4Start, String case4Destination, String case4Waypoint, PathResult case4Result) {

        System.out.println("==================================================");
        System.out.println("Task B Summary Table");
        System.out.println("==================================================");

        System.out.printf("%-8s %-10s %-14s %-18s %-10s %-80s%n",
                "Case",
                "Start",
                "Destination",
                "Waypoint(s)",
                "Cost",
                "Shortest Path");

        System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------");

        printTaskBRow(case1Name, case1Start, case1Destination, case1Waypoint, case1Result);
        printTaskBRow(case2Name, case2Start, case2Destination, case2Waypoint, case2Result);
        printTaskBRow(case3Name, case3Start, case3Destination, case3Waypoint, case3Result);
        printTaskBRow(case4Name, case4Start, case4Destination, case4Waypoint, case4Result);

        System.out.println();
    }

    private static void printTaskBRow(String caseName, String start, String destination, String waypoint, PathResult result) {
        String costText;
        String pathText;

        if (result.getTotalCost() == Integer.MAX_VALUE) {
            costText = "INF";
            pathText = "No path found";
        } else {
            costText = String.valueOf(result.getTotalCost());
            pathText = result.getPathString();
        }

        System.out.printf("%-8s %-10s %-14s %-18s %-10s %-80s%n",
                caseName,
                start,
                destination,
                waypoint,
                costText,
                pathText);
    }

    /**
     * Empirical comparison block: runs BFS on each non-trivial case and
     * contrasts its weighted cost with Dijkstra's optimum. Case 1 is
     * skipped because it is a self-loop with cost 0.
     */
    private static void printBFSComparison(BFS bfs,
                                           String a1, String a10,
                                           String b1, String b5,
                                           String c1, String c5,
                                           PathResult dijkstraCase2,
                                           PathResult dijkstraCase3,
                                           PathResult dijkstraCase4) {
        System.out.println("------------------------------------------------------");
        System.out.println("BFS comparison (treating graph as unweighted):");
        System.out.println("------------------------------------------------------");

        PathResult bfsCase2 = bfs.findShortestPathByEdgeCount(a1, a10);
        PathResult bfsCase3 = bfs.findPathThroughWaypoints(new String[]{a1, b5, b1});
        PathResult bfsCase4 = bfs.findPathThroughWaypoints(new String[]{a1, b5, c5, c1});

        printBFSComparisonCase("Case 2 (" + a1 + " -> " + a10 + ")", dijkstraCase2, bfsCase2);
        printBFSComparisonCase("Case 3 (" + a1 + " -> " + b1 + " via " + b5 + ")", dijkstraCase3, bfsCase3);
        printBFSComparisonCase("Case 4 (" + a1 + " -> " + c1 + " via " + b5 + " -> " + c5 + ")", dijkstraCase4, bfsCase4);
    }

    private static void printBFSComparisonCase(String header, PathResult dijkstraResult, PathResult bfsResult) {
        System.out.println(header + ":");

        int dijkstraNodes = dijkstraResult.getPath().length;
        int dijkstraCost = dijkstraResult.getTotalCost();

        System.out.printf("  Dijkstra path length: %d nodes, weighted cost = %d%n",
                dijkstraNodes, dijkstraCost);

        if (bfsResult.getTotalCost() == Integer.MAX_VALUE) {
            System.out.println("  BFS path length:      no path found");
        } else {
            int bfsNodes = bfsResult.getPath().length;
            int bfsCost = bfsResult.getTotalCost();
            System.out.printf("  BFS path length:      %d nodes, weighted cost = %d%n",
                    bfsNodes, bfsCost);
            System.out.printf("  Cost difference:      %d%n", bfsCost - dijkstraCost);
        }

        System.out.println();
    }
}
