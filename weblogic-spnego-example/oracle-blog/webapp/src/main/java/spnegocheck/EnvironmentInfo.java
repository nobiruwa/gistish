package spnegocheck;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class EnvironmentInfo {
   private static EnvironmentInfo environmentInfo = null;
   private String hostname = "";
   private String canonicalhostname = "";
   private String adHostname = "";
   private String adUserDN = "";
   private String adPassword = "";
   private String adBaseDN = "";
   private String userDNAssociatedWithSPN = "";
   private String userSPN = "";
   private String keytab = "";
   private String krb5loginconf = "";

   public static EnvironmentInfo getEnvironmentInfo() {
      if (null == environmentInfo) {
         environmentInfo = new EnvironmentInfo();
      }

      return environmentInfo;
   }

   private EnvironmentInfo() {
      try {
         InetAddress addr = InetAddress.getLocalHost();
         this.canonicalhostname = addr.getCanonicalHostName();
         this.hostname = addr.getHostName();
      } catch (UnknownHostException var2) {
      }

      if (null != this.hostname) {
         this.adBaseDN = this.canonicalhostname.replaceAll("\\.", ",dc=");
         this.adBaseDN = this.adBaseDN.substring(this.adBaseDN.indexOf(",") + 1);
      }

      this.adUserDN = "cn=administrator,CN=Users," + this.adBaseDN;
   }

   public void setHostname(String hostname) {
      this.hostname = hostname;
   }

   public String getHostname() {
      return this.hostname;
   }

   public void setADHostname(String adHostname) {
      this.adHostname = adHostname;
   }

   public String getADHostname() {
      return this.adHostname;
   }

   public void setADPassword(String adPassword) {
      this.adPassword = adPassword;
   }

   public String getADPassword() {
      return this.adPassword;
   }

   public void setADBaseDN(String adBaseDN) {
      this.adBaseDN = adBaseDN;
   }

   public String getADBaseDN() {
      return this.adBaseDN;
   }

   public void setADUserDN(String adUserDN) {
      this.adUserDN = adUserDN;
   }

   public String getADUserDN() {
      return this.adUserDN;
   }

   public void setCanonicalHostname(String canonicalhostname) {
      this.canonicalhostname = canonicalhostname;
   }

   public String getCanonicalHostname() {
      return this.canonicalhostname;
   }

   public String getUserDNAssociatedWithSPN() {
      return this.userDNAssociatedWithSPN;
   }

   public void setUserDNAssociatedWithSPN(String userDNAssociatedWithSPN) {
      this.userDNAssociatedWithSPN = userDNAssociatedWithSPN;
   }

   public void setUserSPN(String userSPN) {
      this.userSPN = userSPN;
   }

   public String getUserSPN() {
      return this.userSPN;
   }

   public void setKeytab(String keytab) {
      this.keytab = keytab;
   }

   public String getKeytab() {
      return this.keytab;
   }

   public void setKrb5loginconf(String krb5loginconf) {
      this.krb5loginconf = krb5loginconf;
   }

   public String getKrb5loginconf() {
      return this.krb5loginconf;
   }
}
