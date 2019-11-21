import junit.framework.Test;
import junit.framework.TestSuite;

public class ICT5258 {

  public static Test suite() {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(ICT5258Login.class);
    suite.addTestSuite(ICT5258TestCase01.class);
    suite.addTestSuite(ICT5258Logout.class);
    return suite;
  }

  public static void main(String[] args) {
    junit.textui.TestRunner.run(suite());
  }
}
