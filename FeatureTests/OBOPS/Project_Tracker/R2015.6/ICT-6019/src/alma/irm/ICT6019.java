package alma.irm;

import junit.framework.Test;
import junit.framework.TestSuite;

public class ICT6019 {

  public static Test suite() {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(ICT6019Login.class);
    suite.addTestSuite(ICT6019TestCase01.class);
    suite.addTestSuite(ICT6019TestCase02.class);
    suite.addTestSuite(ICT6019Logout.class);
    return suite;
  }

  public static void main(String[] args) {
    junit.textui.TestRunner.run(suite());
  }
}
