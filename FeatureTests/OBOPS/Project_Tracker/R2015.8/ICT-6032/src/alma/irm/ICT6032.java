import junit.framework.Test;
import junit.framework.TestSuite;

public class ICT6032 {

  public static Test suite() {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(ICT6032Login.class);
    suite.addTestSuite(ICT6032TestCase01.class);
    suite.addTestSuite(ICT6032Logout.class);
    return suite;
  }

  public static void main(String[] args) {
    junit.textui.TestRunner.run(suite());
  }
}
