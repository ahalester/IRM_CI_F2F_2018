package alma.irm;

import junit.framework.Test;
import junit.framework.TestSuite;

public class ICT6773 {

  public static Test suite() {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(ICT6773Login.class);
    suite.addTestSuite(ICT6773TestCase01.class);
    suite.addTestSuite(ICT6773Logout.class);
    return suite;
  }

  public static void main(String[] args) {
    junit.textui.TestRunner.run(suite());
  }
}
