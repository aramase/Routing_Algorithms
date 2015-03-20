import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author Anish Ramasekar
 */

public class DistVector {

    static int numNodes = 0;
    static double[] distance = new double[40];
    static double[][] matrix = new double[40][40];
    static double[] update = new double[40];
    static double[] update1 = new double[40];
    static int min = 999;
    static int sum = 0;
    static int repeatsum = 0;
    static int iteration = 1;
    static int[] flag = new int[40];
    static double[] number = new double[40];
    static int total_iteration = 0;
    static int[] count = new int[40];
    static int value = 1;

    public static void main(String[] args) throws IOException, InterruptedException {
        if (args.length != 5) {
            System.out.println("Usage: dist_vector initial-node file-name node1 node2");
            System.exit(1);
        }

        StopWatch s = new StopWatch();
        //initialize matrix
        File input = new File(args[2]);
        BufferedReader br = new BufferedReader(new FileReader(input));

        String lineRead = br.readLine();
        numNodes = Integer.parseInt(lineRead);
        System.out.println(numNodes + " nodes");
        //read input from file

        int m = Integer.parseInt(args[3].trim());
        int n = Integer.parseInt(args[4].trim());
        int e = Integer.parseInt(args[1].trim());

        s.start();//start the timer
        //Thread.sleep(35);

        while ((lineRead = br.readLine()) != null) {
            String[] fields = lineRead.split(" ");
            int node1 = Integer.parseInt((fields[0]));
            int node2 = Integer.parseInt(fields[1]);
            double dist = Double.parseDouble(fields[2]);

            matrix[node1][node2] = dist;
            matrix[node2][node1] = dist;
        }

        Computation(m);//source node

        System.out.println("Distance from Node " + m + " to Node " + n + " is " + distance[n]);
        System.out.println();

        Computation(n);//destination node

        s.stop();
        System.out.println("Total Time elapsed is : " + (s.getElapsedTime()) + " milli-seconds");

    }//End of main()

    public static void Computation(int node) {
        for (int i = 1; i <= numNodes; i++) {
            for (int j = 1; j <= numNodes; j++) {
                if (matrix[i][j] == 0) {
                    matrix[i][j] = 999;
                }
                distance[i] = 999;
            }
        }

        for (int ctr = node; ctr <= node; ctr++)//given node
        {
            for (int z = 1; z <= numNodes; z++) {
                distance[z] = 999;
                update1[z] = distance[z];
            }
            distance[ctr] = 0;
            update1[ctr] = distance[ctr];
            flag[ctr] = 1;

            for (int k = 1; k <= numNodes - 1; k++) {
                for (int i = 1; i <= numNodes; i++) {
                    if (flag[i] != 0) {
                        for (int j = 1; j <= numNodes; j++) {
                            if (matrix[i][j] != 999) {
                                if (distance[j] > distance[i] + matrix[i][j]) {
                                    distance[j] = distance[i] + matrix[i][j];
                                }

                            }
                            update[j] = distance[j];
                        }//end of j	
                    }//end of i
                }

                //raising the flag for neighbors
                for (int r = 1; r <= numNodes; r++) {
                    if (update[r] != 999) {
                        flag[r] = 1;
                    } else {
                        flag[r] = 0;
                    }
                }

                //check the values of 2 consecutive iterations
                for (int a = 1; a <= 1; a++) {
                    for (int b = 1; b <= numNodes; b++) {
                        if (update1[b] == update[b]) {
                            sum += 1;
                        } else {
                            repeatsum += 1;
                        }
                    }
                }

                //decision to stop or continue
                if (repeatsum != 0) {
                    iteration++;
                    //flag=1;

                    for (int a = 1; a <= 1; a++) {
                        for (int b = 1; b <= numNodes; b++) {
                            update1[b] = update[b];
                        }
                    }
                    sum = 0;
                    repeatsum = 0;
                }
				//else
                //flag=0;
            }

            total_iteration += iteration;
            count[value] = iteration;
            value++;

            System.out.println("Routing table for node " + node);//for the input nodes
            for (int i = 1; i <= numNodes; i++) {
                System.out.println("distance from Node " + i + " to Node " + node + " is " + distance[i]);
            }
            System.out.println("Number of iterations: " + iteration);
            System.out.println("\n");

            iteration = 1;

        }//End of ctr loop

    }
}//End of class1
