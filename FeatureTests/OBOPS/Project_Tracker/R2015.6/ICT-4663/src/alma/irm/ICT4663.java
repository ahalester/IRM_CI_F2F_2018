package alma.irm;

import junit.framework.Test;
import junit.framework.TestSuite;

public class ICT4663 {

  public static Test suite() {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(ICT4663Login.class);
    suite.addTestSuite(ICT4663TestCase01.class);
    suite.addTestSuite(ICT4663TestCase02.class);
    suite.addTestSuite(ICT4663TestCase03.class);
    suite.addTestSuite(ICT4663Logout.class);
    return suite;
  }

  public static void main(String[] args) {
    junit.textui.TestRunner.run(suite());
  }
}
