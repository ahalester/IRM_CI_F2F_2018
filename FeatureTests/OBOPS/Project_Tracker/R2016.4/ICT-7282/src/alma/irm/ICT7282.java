package alma.irm;

import junit.framework.Test;
import junit.framework.TestSuite;

public class ICT7282 {

  public static Test suite() {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(ICT7282Login.class);
    suite.addTestSuite(ICT7282TestCase01.class);
    suite.addTestSuite(ICT7282TestCase02.class);
    suite.addTestSuite(ICT7282TestCase03.class);
    return suite;
  }

  public static void main(String[] args) {
    junit.textui.TestRunner.run(suite());
  }
}
