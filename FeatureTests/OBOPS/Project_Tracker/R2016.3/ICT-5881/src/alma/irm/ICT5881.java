package alma.irm;

import junit.framework.TestSuite;

public class ICT5881 {

  public static Test suite() {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(ICT5881Login.class);
    suite.addTestSuite(ICT5881TestCase01.class);
    suite.addTestSuite(ICT5881Logout.class);
    return suite;
  }

  public static void main(String[] args) {
    junit.textui.TestRunner.run(suite());
  }
}
