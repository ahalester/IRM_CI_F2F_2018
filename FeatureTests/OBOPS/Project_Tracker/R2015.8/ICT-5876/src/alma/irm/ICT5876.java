package alma.irm;

import junit.framework.Test;
import junit.framework.TestSuite;

public class ICT5876 {

  public static Test suite() {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(ICT5876Login.class);
    suite.addTestSuite(ICT5876TestCase01.class);
    suite.addTestSuite(ICT5876Logout.class);
    return suite;
  }

  public static void main(String[] args) {
    junit.textui.TestRunner.run(suite());
  }
}
