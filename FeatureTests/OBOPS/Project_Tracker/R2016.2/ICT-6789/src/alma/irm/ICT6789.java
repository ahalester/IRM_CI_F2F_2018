package alma.irm;

import junit.framework.Test;
import junit.framework.TestSuite;

public class ICT6789 {

  public static Test suite() {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(ICT6789Login.class);
    suite.addTestSuite(ICT6789TestCase01.class);
    suite.addTestSuite(ICT6789TestCase02.class);
    return suite;
  }

  public static void main(String[] args) {
    junit.textui.TestRunner.run(suite());
  }
}
