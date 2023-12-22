/* -- imports for auth -- */
import java.io.*;
import java.net.*;
import java.util.Random;
import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.catalina.*;
import org.apache.catalina.startup.Tomcat;

/* -- imports for token -- */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.util.Random;

/* -- end imports -- */

public class Duo {
  public static double version = 3.50;

public static void main(String [] args) {

  if (args.length < 1) { usageExit(2); }

  if (args[0].equals("V")) {
    DuoV auth = new DuoV(args[1]);
    auth.Main();
  }
  else if (args[0].equals("T")) {
    DuoT token = new DuoT(args[1], args[2]);
    token.Main();
  }
  else {
    System.out.println(" Unknown mode");
    usageExit(3);
  }
}   /* main */

public static void usageExit(int x) {
    System.out.println(
      " Usage: java Duo V <port>        ## as auth or verifier\n"
    + "  OR\n"
    + "        java Duo T <host> <port> ## as token");
    System.exit(x);
}   /* usageExit */

}   /* class Duo */

class DuoV extends HttpServlet {  /* the embedded Tomcat */

  public int  port;
  OTP         otp;

  boolean     INIT=false;  
  String      SName;       /* servlet name */
  String      SPattern;    /* url pattern */
  Tomcat      tomcat;
  String      h0 = (
     "<html><title>NJIT DUO Authenticator</title><body>"
   + "<table style=\"width:3in;"
   + "border:1px solid black;text-align:center;font-family:verdana\">"
   + "<tr bgcolor=\"#FF0000\">"
   + "<td style=\"color: #FFFFEE; font-size: 40px\">NJIT DUO <br> AUTH </td>"
   + "</tr>"
   + "<tr>"
   + "<td style=\"font-size: 20px\">"
   + "<form action=\"/go\" method=\"get\">"
   + "OTP : <input type=\"text\" name=\"myOTP\" style=\"font-size: 14pt\" /><br><br><br>"
   + "<input type=\"submit\" value=\"SUBMIT\" style=\"font-size: 14pt\" />"
   + "</td></tr>");
  String h1 = ("</table></body></html>");

public DuoV(String p) {

  port = Integer.parseInt(p);  /* port for web requests */
  SName    = "Servlet1";   // servlet name
  SPattern = "/go";        // its url-pattern for URL
  tomcat   = new Tomcat();    /* embedded Tomcat */
  tomcat.setBaseDir("temp");       /* temp dir, in current dir for temp stuff */
  tomcat.setPort( port );          /* port, usual */

  String contextPath = "/";
  String docBase = new File(".").getAbsolutePath();

  Context context = tomcat.addContext(contextPath, docBase);

  tomcat.addServlet(contextPath, SName, this); /* embed servlet into container */
  context.addServletMapping(SPattern, SName);  /* add servlet mapping */

  /* -- OTP part */
  this.otp = new OTP(this);

  System.out.println(" DUO V STARTS -- web port " + port + " T port " + (port+1) );
} // DuoV(String s)

public void Main() {

  try {

  tomcat.start();              /* start Tomcat */
  tomcat.getServer().await();  /* and wait for requests */

  } catch( Exception e ) {
    System.out.println(" V ERROR " + e.getStackTrace() );
  }
} /* main */

@Override
protected void doGet(HttpServletRequest req, HttpServletResponse resp)
   throws ServletException, IOException {

   String x;

   x = this.otp.verify( req );

   x = ("<tr><td>" + x + "</td></tr>");

   resp.getWriter().println( h0 + x + h1 );
}  /* doGet */

public void init() throws ServletException
{
  System.out.println("DLOG INIT");
}

public void destroy()
{
  System.out.println(" DLOG TERM");
}

} /* DuoV */

/* DuoT, the token */
class DuoT implements ActionListener, Runnable {

  public String host;
  public int    port;
  JLabel        label1;
  Timer         timer;
  OTP           otp;

public DuoT (String h, String p) {

   host  = h;
   port  = Integer.parseInt(p);
   otp   = new OTP(this);
}

private void show() {

   String myfont;

   if ((myfont = System.getProperty("myfont")) != null) {
      System.setProperty("swing.plaf.metal.userFont",myfont);
      System.setProperty("swing.plaf.metal.controlFont",myfont);
   }

   // Create and set up the window.
   JFrame frame = new JFrame(" Duo Token ");
   frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

   // Set up the content pane.
   frame.setLayout( new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS) );

   JLabel label0 = new JLabel( "NJIT DUO" );
   label0.setFont( UIManager.getDefaults().getFont("Label.font").deriveFont( Font.BOLD, 30f ) );
   label0.setForeground( Color.WHITE );
   JPanel p0     = new JPanel();
   p0.setBorder( new EmptyBorder( 50, 50, 50, 50 ) );
   p0.setBackground( Color.red   );
   p0.add( label0 );
   frame.add(p0);

   label1 = new JLabel( "------" );
   label1.setFont( UIManager.getDefaults().getFont("Label.font").deriveFont( 20f ) );
   JPanel p = new JPanel();
   p.setBorder( new EmptyBorder( 50, 50, 50, 50 ) );
   p.add( label1);
   frame.add( p );

   JButton button = new JButton("OTP");
   button.setAlignmentX(Component.CENTER_ALIGNMENT);
   button.setBorder( new EmptyBorder( 20, 20, 20, 20 ) );
   button.addActionListener( this );
   button.setFont( UIManager.getDefaults().getFont("Button.font").deriveFont( 20f ) );
   frame.add(button);

   // Display the window.
   frame.pack();
   frame.setVisible(true);
} // show

public void actionPerformed(ActionEvent e) {

  otp.handleEvent(e);

} // actionPerformed

@Override
public void run() {  this.show(); }

public void Main() {
  java.awt.EventQueue.invokeLater( this );
}       // main
} // class DuoT
