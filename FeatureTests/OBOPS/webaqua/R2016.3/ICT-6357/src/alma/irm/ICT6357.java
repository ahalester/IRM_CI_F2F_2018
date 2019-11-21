package alma.irm;

import junit.framework.Test;
import junit.framework.TestSuite;

public class ICT6357 {

  public static Test suite() {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(ICT6357Login.class);
    suite.addTestSuite(ICT6357TestCase01.class);
    suite.addTestSuite(ICT6357TestCase02.class);
    suite.addTestSuite(ICT6357TestCase03.class);
    suite.addTestSuite(ICT6357TestCase04.class);
    suite.addTestSuite(ICT6357TestCase05.class);
    suite.addTestSuite(ICT6357TestCase06.class);
    suite.addTestSuite(ICT6357TestCase07.class);
    suite.addTestSuite(ICT6357Logout.class);
    return suite;
  }

  public static void main(String[] args) {
    junit.textui.TestRunner.run(suite());
  }
}
