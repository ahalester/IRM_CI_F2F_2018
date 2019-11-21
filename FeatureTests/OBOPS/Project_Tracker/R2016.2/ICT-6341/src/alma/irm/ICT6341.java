package alma.irm;

import junit.framework.Test;
import junit.framework.TestSuite;

public class ICT6341 {

  public static Test suite() {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(ICT6341Login.class);
    suite.addTestSuite(ICT6341TestCase01.class);
    suite.addTestSuite(ICT6341TestCase02.class);
    suite.addTestSuite(ICT6341Logout.class);
    return suite;
  }

  public static void main(String[] args) {
    junit.textui.TestRunner.run(suite());
  }
}
