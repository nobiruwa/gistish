package spnegocheck;

import javax.servlet.http.HttpServletRequest;
import java.util.Base64;

public class AuthorizationHeaderProcessor {
   private String header = null;
   private String authType = null;
   private byte[] decodedHeader = null;

   public AuthorizationHeaderProcessor() {
   }

   public AuthorizationHeaderProcessor(HttpServletRequest request) {
      this.header = request.getHeader("Authorization");
      if (null != this.header && this.header.contains(" ")) {
         this.authType = this.header.substring(0, this.header.indexOf(" "));
         System.out.println("Auth type: " + this.authType);
         String decodablePart = this.header.substring(this.header.indexOf(" ") + 1);

         try {
            Base64.Decoder decoder = Base64.getDecoder();
            System.out.println("Decoding " + decodablePart);
            this.decodedHeader = decoder.decode(decodablePart);
            System.out.println("Decoded.");
         } catch (IllegalArgumentException var4) {
             System.err.println(String.format("Error: %s", var4));
         } catch (Exception ex) {
             System.err.println(String.format("Error: %s", ex));
         }
      }

   }

   public boolean containsHeader() {
      return this.header != null;
   }

   public boolean isNTLM() {
      String s = new String(this.decodedHeader, 0, 7);
      return s.equals("NTLMSSP");
   }

   public boolean isKerberos() {
      return this.header.startsWith("Negotiate YII");
   }

   public String getAuthType() {
      return this.authType;
   }
}
