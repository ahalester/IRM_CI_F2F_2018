package alma.irm;

import junit.framework.Test;
import junit.framework.TestSuite;

public class ICT8566 {

  public static Test suite() {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(ICT8566Login.class);
    suite.addTestSuite(ICT8566TestCase01.class);
    suite.addTestSuite(ICT8566Logout.class);
    return suite;
  }

  public static void main(String[] args) {
    junit.textui.TestRunner.run(suite());
  }
}
