package extracredit2;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class Send implements Runnable
{
    static DatagramPacket dp;
    static DatagramSocket sock;
    static int node;
    public Send(DatagramPacket dp,int i)
    {
        Send.dp=dp;
        node=i;
    }

    @Override
    public void run() 
    {
      try
      {
          sock=new DatagramSocket(5555);
          sock.send(dp);
          System.out.println("DV sent to Node "+node);
      }
      catch(IOException ex)
      {
          System.out.println("Node "+node+" has converged");
      }
      sock.close();
    }
}
