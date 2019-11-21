package alma.irm;

import junit.framework.Test;
import junit.framework.TestSuite;

public class IRM {

  public static Test suite() {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(ICT6212Login.class);
    suite.addTestSuite(ICT6212TestCase01.class);
    suite.addTestSuite(ICT6212TestCase02.class);
    suite.addTestSuite(ICT6212TestCase03.class);
    suite.addTestSuite(ICT6212Logout.class);
    return suite;
  }

  public static void main(String[] args) {
    junit.textui.TestRunner.run(suite());
  }
}
