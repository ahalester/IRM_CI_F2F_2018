package alma.irm;

import junit.framework.Test;
import junit.framework.TestSuite;

public class ICT4741 {

  public static Test suite() {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(ICT4741Login.class);
    suite.addTestSuite(ICT4741TestCase01.class);
    return suite;
  }

  public static void main(String[] args) {
    junit.textui.TestRunner.run(suite());
  }
}
