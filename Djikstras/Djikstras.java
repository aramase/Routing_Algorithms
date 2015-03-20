import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Djikstras
{
 static int numNodes=0;
 static double[] distance=new double[40];
 static double[][] matrix=new double[40][40];
 static int visited[]=new int[40];
 static int nextNode=1;
 
 static int sourceNode=1;
 static int destNode=1;
 static double min=999;

 public static void main(String[] args) throws IOException, InterruptedException
{
    if(args.length!=4)
    {
        System.out.println("Usage: link_state fileName sourceNode destNode");
        System.exit(1);
    }
 StopWatch s=new StopWatch();
 //initialize matrix
 File input=new File("input.txt");
 BufferedReader br=new BufferedReader(new FileReader(input));

 sourceNode=Integer.parseInt(args[2].trim());
 destNode=Integer.parseInt(args[3].trim());
 
 String lineRead=br.readLine();
 numNodes=Integer.parseInt(lineRead);
 System.out.println(numNodes+" nodes");

//read input from file
 while((lineRead=br.readLine())!=null)
{
 String[] fields=lineRead.split(" ");
 int node1=Integer.parseInt(fields[0]);
 int node2=Integer.parseInt(fields[1]);
 double dist=Double.parseDouble(fields[2]);
 
 matrix[node1][node2]=dist;
 matrix[node2][node1]=dist;
}
 
 for(int i=1;i<=numNodes;i++)
{
 for(int j=1;j<=numNodes;j++)
 {
  if(matrix[i][j]==0)
      matrix[i][j]=999;
 }
}
s.start();
 Thread.sleep(150);
 
djikstras(sourceNode);
System.out.println("Shortest Path from Node "+sourceNode+" to Node "+destNode+" is "+distance[destNode]);	
System.out.println();

//Printing the routing Table for sourceNode
System.out.println("Routing Table for Node "+sourceNode);
for(int i = 1; i<=numNodes; i++)
{
 System.out.println("Shortest Path from Node "+sourceNode+" to Node "+i+" is "+distance[i]);		
}
System.out.println();

djikstras(destNode);
//Printing the routing Table for destNode
System.out.println("Routing Table for Node "+destNode);
for(int i = 1; i<=numNodes; i++)
{
 System.out.println("Shortest Path from Node "+destNode+" to Node "+i+" is "+distance[i]);		
}
System.out.println();

s.stop();

System.out.println("Total Time elapsed is : "+(s.getElapsedTime()-150)+" milli-seconds");
System.out.println();
}//End of main()
 
 public static void djikstras(int node)
 {
     for(int ctr=1;ctr<=1;ctr++)
{
     
    for(int k=1;k<=numNodes;k++)
    {
        visited[k]=0;
        distance[k]=0;
    }
distance=matrix[node];      //Distance from Node "n" to all other nodes is now in the array distance[]
visited[node]=1;            //Source is already visited 
distance[node]=0;           //distance of Source node from itself is 0 

for(int i=1;i<=numNodes;i++)
{
 min=999;
 for(int j=1;j<=numNodes;j++)
{
 if(visited[j]!=1&&min>distance[j])
{
 min=distance[j];
 nextNode=j;
}
} //End of j loop

visited[nextNode]=1;

for(int x=1;x<=numNodes;x++)
{
 if(visited[x]!=1)
 {
  if(min+matrix[nextNode][x]<distance[x])
  {
   distance[x]=min+matrix[nextNode][x];
   //preD[i]=nextNode;
  }
}
}//End of x loop
}//End of i loop
}//End of ctr loop
 }

}//End of class

 
 
 
 
 
 