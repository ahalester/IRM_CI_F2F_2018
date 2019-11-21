package alma.irm;

import junit.framework.TestSuite;

public class ICT5458 {

  public static Test suite() {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(ICT5458Login.class);
    suite.addTestSuite(ICT5458TestCase01.class);
    suite.addTestSuite(ICT5458Logout.class);
    return suite;
  }

  public static void main(String[] args) {
    junit.textui.TestRunner.run(suite());
  }
}
