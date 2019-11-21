package alma.irm;

import junit.framework.Test;
import junit.framework.TestSuite;

public class ICT6676 {

  public static Test suite() {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(ICT6676Home.class);
    suite.addTestSuite(ICT6676Utils.class);
    return suite;
  }

  public static void main(String[] args) {
    junit.textui.TestRunner.run(suite());
  }
}
