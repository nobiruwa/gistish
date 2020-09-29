package spnegocheck;

import javax.servlet.jsp.JspWriter;

public class ErrorReporter {
   public static void reportError(JspWriter out, String text, String backURL, String backLinkText, String forwardURL, String forwardText) {
      try {
         out.println("<B>" + text + "</B><BR/>");
         if (null != backURL) {
            out.println("<a href=" + backURL + ">" + backLinkText + "</a><BR/>");
            out.println("or<BR/>");
         }

         out.println("<a href=" + forwardURL + ">" + forwardText + "</a><BR>");
      } catch (Exception var7) {
      }

   }

   public static void reportError(JspWriter out, String text, String forwardURL, String forwardText) {
      reportError(out, text, (String)null, (String)null, forwardURL, forwardText);
   }
}
