import junit.framework.Test;
import junit.framework.TestSuite;

public class ICT5869 {

  public static Test suite() {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(ICT5869Login.class);
    suite.addTestSuite(ICT5869TestCase01.class);
    suite.addTestSuite(ICT5869Logout.class);
    return suite;
  }

  public static void main(String[] args) {
    junit.textui.TestRunner.run(suite());
  }
}
