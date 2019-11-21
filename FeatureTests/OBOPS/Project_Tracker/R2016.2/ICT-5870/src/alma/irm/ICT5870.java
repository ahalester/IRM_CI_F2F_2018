package alma.irm;

import junit.framework.Test;
import junit.framework.TestSuite;

public class ICT5870 {

  public static Test suite() {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(ICT5870Login.class);
    suite.addTestSuite(ICT5870TestCase01.class);
    return suite;
  }

  public static void main(String[] args) {
    junit.textui.TestRunner.run(suite());
  }
}
