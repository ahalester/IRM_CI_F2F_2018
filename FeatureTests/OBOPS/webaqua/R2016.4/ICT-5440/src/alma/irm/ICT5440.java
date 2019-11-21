package alma.irm;

import junit.framework.Test;
import junit.framework.TestSuite;

public class ICT5440 {

  public static Test suite() {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(ICT5440Login.class);
    suite.addTestSuite(ICT5440TestCase01.class);
    suite.addTestSuite(ICT5440Logout.class);
    return suite;
  }

  public static void main(String[] args) {
    junit.textui.TestRunner.run(suite());
  }
}
