package alma.irm;

import junit.framework.Test;
import junit.framework.TestSuite;

public class ICT5996 {

  public static Test suite() {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(ICT5996Login.class);
    suite.addTestSuite(ICT5996TestCase01.class);
    return suite;
  }

  public static void main(String[] args) {
    junit.textui.TestRunner.run(suite());
  }
}
