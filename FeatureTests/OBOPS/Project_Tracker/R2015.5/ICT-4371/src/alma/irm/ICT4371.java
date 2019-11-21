package alma.irm;

import junit.framework.Test;
import junit.framework.TestSuite;

public class ICT4371 {

  public static Test suite() {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(ICT4371Login.class);
    suite.addTestSuite(ICT4371TestCase01.class);
    suite.addTestSuite(ICT4371Logout.class);
    return suite;
  }

  public static void main(String[] args) {
    junit.textui.TestRunner.run(suite());
  }
}
