package alma.irm;

import junit.framework.Test;
import junit.framework.TestSuite;

public class ICT7041 {

  public static Test suite() {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(ICT7041Login.class);
    suite.addTestSuite(ICT7041TestCase01.class);
    suite.addTestSuite(ICT7041Logout.class);
    return suite;
  }

  public static void main(String[] args) {
    junit.textui.TestRunner.run(suite());
  }
}
