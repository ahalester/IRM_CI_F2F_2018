import junit.framework.Test;
import junit.framework.TestSuite;

public class ICT5352 {

  public static Test suite() {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(ICT5352Login.class);
    suite.addTestSuite(ICT5352TestCase01.class);
    suite.addTestSuite(ICT5352Logout.class);
    return suite;
  }

  public static void main(String[] args) {
    junit.textui.TestRunner.run(suite());
  }
}
