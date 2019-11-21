package alma.irm;

import junit.framework.Test;
import junit.framework.TestSuite;

public class ICT6180 {

  public static Test suite() {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(ICT6180Login.class);
    suite.addTestSuite(ICT6180TestCase01.class);
    suite.addTestSuite(ICT6180Logout.class);
    return suite;
  }

  public static void main(String[] args) {
    junit.textui.TestRunner.run(suite());
  }
}
