package alma.irm;

import junit.framework.Test;
import junit.framework.TestSuite;

public class ICT6614 {

  public static Test suite() {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(ICT6614Home.class);
    return suite;
  }

  public static void main(String[] args) {
    junit.textui.TestRunner.run(suite());
  }
}
