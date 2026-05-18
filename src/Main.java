public class Main {
    public static void main(String[] args) {
        TaskAProcessor processor = new TaskAProcessor();

        TaskAResult resultA = processor.processDataset("Dataset A", "candidates_A.csv");
        TaskAResult resultB = processor.processDataset("Dataset B", "candidates_B.csv");
        TaskAResult resultC = processor.processDataset("Dataset C", "candidates_C.csv");

        printResult(resultA);
        printResult(resultB);
        printResult(resultC);

        printSummaryTable(resultA, resultB, resultC);

        runTaskB(resultA, resultB, resultC);
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

    private static void runTaskB(TaskAResult resultA, TaskAResult resultB, TaskAResult resultC) {
        Graph graph = new Graph();
        graph.loadFromCSV("paths.csv");

        Dijkstra dijkstra = new Dijkstra(graph);

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
    }

    private static void printPathCase(String caseName, String start, String destination, String waypoint, PathResult result) {
        System.out.println(caseName);
        System.out.println("Start Node: " + start);
        System.out.println("Destination Node: " + destination);
        System.out.println("Waypoint(s): " + waypoint);

        if (result.getTotalCost() == Integer.MAX_VALUE) {
            System.out.println("Shortest Path: No path found");
            System.out.println("Total Cost: INF");
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
}