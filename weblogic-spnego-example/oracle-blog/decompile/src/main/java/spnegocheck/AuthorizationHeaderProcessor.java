package spnegocheck;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import sun.misc.BASE64Decoder;

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
            BASE64Decoder decoder = new BASE64Decoder();
            System.out.println("Decoding " + decodablePart);
            this.decodedHeader = decoder.decodeBuffer(decodablePart);
         } catch (IOException var4) {
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
