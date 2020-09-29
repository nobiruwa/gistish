package spnegocheck.utils;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class ReachableTest {
   public static boolean isReachable(String hostname) {
      boolean bReturn = false;

      try {
         InetAddress address = InetAddress.getByName(hostname);
         bReturn = address.isReachable(3000);
         System.out.println("Reachable? " + bReturn);
      } catch (UnknownHostException var3) {
         System.err.println("Unable to lookup " + hostname);
      } catch (IOException var4) {
         System.err.println("Unable to reach " + hostname);
      }

      return bReturn;
   }

   public static boolean isReachable(String hostname, int port) {
      try {
         Socket t = new Socket(hostname, port);
         new DataInputStream(t.getInputStream());
         PrintStream ps = new PrintStream(t.getOutputStream());
         ps.println("Hello");
         t.close();
         System.out.println("Connection seems to have succeeded.");
         return true;
      } catch (IOException var5) {
         var5.printStackTrace();
         return false;
      }
   }
}
