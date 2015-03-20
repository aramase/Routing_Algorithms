/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ipextracredit;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;

/**
 *
 * @author Lakshminarayan
 */
public class IPExtraCredit 
{
    static String lineRead;
    static int numNodes=0;
    static int[][] matrix=new int[10][10];
    static DatagramSocket sock=null;
    static byte[] buffer=new byte[65536];
    static int remotePort;
    static int thisPort=7000; //Changes for every node....listening port
    static int thisNode=1; //Changes for every node
    static int recNode;
    static InetAddress[] ipAddress=new InetAddress[10];
    static int[] rec=new int[10];
    static int[] d=new int[10];
    static int[] prev=new int[10];
    static int[] port=new int[10];
    static String sendData;
    
    public static void main(String[] args) throws FileNotFoundException, IOException, InterruptedException 
    {
        File input=new File("input.txt");
        BufferedReader br=new BufferedReader(new FileReader(input));

        String lineRead=br.readLine();
        numNodes=Integer.parseInt(lineRead);
        System.out.println(numNodes+" nodes");
        
        //initialize matrix
        for(int i=1;i<=numNodes;i++)
        {
            for(int j=1;j<=numNodes;j++)
                matrix[i][j]=999;
        }
        
        while((lineRead=br.readLine())!=null)
       {
        String[] fields=lineRead.split(" ");
        int node1=Integer.parseInt(fields[0]);
        int node2=Integer.parseInt(fields[1]);
        int dist=Integer.parseInt(fields[2]);
        matrix[node1][node2]=dist;
        matrix[node2][node1]=dist;
       }
       
        for(int i=1;i<=numNodes;i++)
            d[i]=matrix[thisNode][i]; //Initialize DV for thisNode
        
       d[thisNode]=0;
       port[1]=7000;
       port[2]=8000;
       port[3]=9000;
       port[4]=10000;
       
       ipAddress[1]=InetAddress.getByName("10.139.60.23"); //thisNode
       ipAddress[2]=InetAddress.getByName("10.139.58.246");
       ipAddress[3]=InetAddress.getByName("10.139.58.132");
       ipAddress[4]=InetAddress.getByName("10.139.57.233");
       
       try
       {
           sock=new DatagramSocket(thisPort);
           DatagramPacket incoming=new DatagramPacket(buffer,buffer.length);
           System.out.println("Host 1 Up and Running. Waiting for Distance Vector from other Hosts");
           boolean proceed=true;
           
           //initiate DV sending process (only for a single Node
           sendData="1";
           for(int i=1;i<=numNodes;i++)
           {
               sendData=sendData+" "+Integer.toString(d[i]);
           }
           DatagramPacket initialDatagram=new DatagramPacket(sendData.getBytes(),sendData.getBytes().length,ipAddress[3],port[3]);
           Send initial=new Send(initialDatagram,3);
           Thread initialThread=new Thread(initial);
           initialThread.start();
           initialThread.join();
           
           int iterations=0;
           //Main loop
           while(iterations<numNodes)
           {
               iterations++;
               sendData=null;
               sock.receive(incoming);
               byte[] data=incoming.getData();
               String s=new String(data,0,incoming.getLength());
               String[] elements=s.split(" ");
               recNode=Integer.parseInt(elements[0]);
               
               for(int i=1;i<=numNodes;i++)
               {
                   rec[i]=Integer.parseInt(elements[i].trim());
               }
               prev=d;
               
               for(int j=1;j<=numNodes;j++)
               {
                   if(matrix[thisNode][j]!=999)
                   {
                       if(d[j]>(matrix[thisNode][recNode]+rec[j]))
                       {
                           d[j]=matrix[thisNode][recNode]+rec[j];
                       }
                   }
               }
               
                 //formulate the data to be sent to other nodes  
                 sendData=Integer.toString(thisNode);
                 for(int i=1;i<=numNodes;i++)
                 {
                     sendData=sendData+" "+Integer.toString(d[i]);
                 }
                 
                 Send send[]=new Send[numNodes+1];
                 Thread t[]=new Thread[numNodes+1];
                 int[] flags=new int[numNodes+1];
                 
                 //send DV to all neighbour nodes of thisNode
                 for(int i=1;i<=numNodes;i++)
                 {
                     if(matrix[thisNode][i]!=999)
                     {
                             DatagramPacket dp=new DatagramPacket(sendData.getBytes(),sendData.getBytes().length,ipAddress[i],port[i]);
                             send[i]=new Send(dp,i);
                             t[i]=new Thread(send[i]);
                             t[i].start();
                             t[i].join();
                             Thread.sleep(10);
                             flags[i]=1;
                     }
                 }
           }
           sock.close();
           for(int i=1;i<=numNodes;i++)
               System.out.println("The distance from Node "+thisNode+" to Node "+i+" is "+d[i]);
       }
       catch(IOException ex)
       {
           System.out.println(ex.getMessage());
       }
        
        
    }
    
}
