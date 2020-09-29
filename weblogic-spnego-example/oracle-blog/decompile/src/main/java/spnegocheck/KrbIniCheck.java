package spnegocheck;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import sun.security.krb5.Config;
import sun.security.krb5.KrbException;

public class KrbIniCheck {
   private static final String fileName = "c:/windows/krb5.ini";
   private File file = null;
   String fileContents = "";
   Map iniContents = null;
   String error = null;

   public KrbIniCheck() {
      try {
         this.file = new File("c:/windows/krb5.ini");
         if (this.file.exists()) {
            this.fileContents = FileUtils.getFileContents(this.file);
            this.ParseFileContents();
         }
      } catch (FileNotFoundException var2) {
      } catch (IOException var3) {
      } catch (Exception var4) {
         this.error = var4.getMessage();
      }

   }

   private void CheckConfig() throws KrbException {
      Config cfg = Config.getInstance();
      String defaultRealm = cfg.getDefaultRealm();
      String kdcList = cfg.getKDCList(cfg.getDefaultRealm());
   }

   public String getFileName() {
      return "c:/windows/krb5.ini";
   }

   public boolean DoesFileExist() {
      return this.file.exists();
   }

   public String GetFileContents() {
      return this.fileContents;
   }

   public boolean getFileParsedOK() {
      return null == this.error;
   }

   private void ParseFileContents() throws Exception {
      if (null != this.fileContents && !this.fileContents.isEmpty()) {
         String[] lines = this.fileContents.split("\\r?\\n");
         String section = "";
         String realmName = "";
         boolean bInCurly = false;
         String[] arr$ = lines;
         int len$ = lines.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            String line = arr$[i$];
            line = line.trim();
            if (!line.isEmpty() && !line.startsWith("#")) {
               if (line.startsWith("[") && line.endsWith("]")) {
                  section = line.substring(1, line.length() - 1);
                  System.out.println("In section '" + section + "'");
               } else if (!section.equals("libdefaults") && !section.equals("domain_realm") && !section.equals("appdefaults")) {
                  if (!section.equals("realms")) {
                     throw new Exception("Found a setting line in an unexpected section - " + section);
                  }

                  if (line.contains("}")) {
                     bInCurly = false;
                  } else if (!bInCurly && line.contentEquals("{")) {
                     realmName = line.substring(line.indexOf("=")).trim();
                     System.out.println("Realm name '" + realmName + "' found");
                  }
               } else {
                  if (this.iniContents.get(section) == null) {
                     this.iniContents.put(section, new Properties());
                  }

                  String[] splits = line.split("=", 1);
                  String key = splits[0].trim();
                  String value = splits[1].trim();
                  ((Properties)this.iniContents.get(section)).put(key, value);
               }
            }
         }

      }
   }
}
