package alma.irm;

import junit.framework.Test;
import junit.framework.TestSuite;

public class ICT5930 {

  public static Test suite() {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(ICT5930Login.class);
    suite.addTestSuite(ICT5930TestCase01.class);
    suite.addTestSuite(ICT5930TestCase02.class);
    suite.addTestSuite(ICT5930Logout.class);
    return suite;
  }

  public static void main(String[] args) {
    junit.textui.TestRunner.run(suite());
  }
}
