package spnegocheck;

import java.io.File;
import java.io.IOException;
import sun.security.krb5.internal.ktab.KeyTab;
import sun.security.krb5.internal.ktab.KeyTabEntry;

public class KeytabCheck {
   private static final int MAX_ALLOWED_FILE_LENGTH = 1048576;
   private String fileName = null;
   private File file = null;

   public KeytabCheck() {
      try {
         this.locateFile();
      } catch (Exception var2) {
         System.out.println("Exception caught during location and loading of config file");
         var2.printStackTrace();
      }

   }

   private void locateFile() throws IOException, Exception {
      this.fileName = EnvironmentInfo.getEnvironmentInfo().getKeytab();
      if (null == this.fileName) {
         System.out.println("No keytab filename in Environment Info.");
      } else {
         this.file = new File(this.fileName);
         if (this.file.exists()) {
            System.out.println("Found file using '" + this.fileName + "'");
         } else {
            this.fileName = System.getenv("DOMAIN_HOME") + "/" + EnvironmentInfo.getEnvironmentInfo().getKeytab();
            this.file = new File(this.fileName);
            if (this.file.exists()) {
               System.out.println("Found file using '" + this.fileName + "'");
            } else {
               this.fileName = EnvironmentInfo.getEnvironmentInfo().getKeytab();
               this.file = null;
            }
         }
      }
   }

   public String getFileName() {
      return this.fileName;
   }

   public boolean isKeytabFound() {
      if (null == this.file) {
         return false;
      } else if (this.file.exists()) {
         return true;
      } else if (this.file.canRead()) {
         System.out.println("File is not readable");
         return false;
      } else {
         return false;
      }
   }

   public boolean isKeytabOK() {
      KeyTab kt = KeyTab.getInstance(this.file);
      KeyTab.refresh();
      KeyTabEntry[] entries = kt.getEntries();
      KeyTabEntry[] arr$ = entries;
      int len$ = entries.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         KeyTabEntry entry = arr$[i$];
         System.out.println("Keytab entry:");
         System.out.println("  Service: " + entry.getService());
         System.out.println("    EType: " + entry.getKey().getEType());
         System.out.println("     Time: " + entry.getTimeStamp().toGeneralizedTimeString());
         System.out.println();
      }

      return true;
   }
}
