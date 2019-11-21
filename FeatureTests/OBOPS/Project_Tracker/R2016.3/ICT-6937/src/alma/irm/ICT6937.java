package alma.irm;

import junit.framework.Test;
import junit.framework.TestSuite;

public class ICT6937 {

  public static Test suite() {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(ICT6937Login.class);
    suite.addTestSuite(ICT6937TestCase01.class);
    return suite;
  }

  public static void main(String[] args) {
    junit.textui.TestRunner.run(suite());
  }
}
