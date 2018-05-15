import hometask02.LogParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.*;
import org.junit.rules.TestName;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

public class TestBase {
    public static Logger log = LogManager.getLogger(LogParser.class);

    @Rule
    public TestName testName = new TestName();

    @ClassRule
    public static TestRule classWatchman = new TestWatcher() {
        @Override
        protected void finished(Description desc) {
            log.info("Suite completed.", desc);
        }

        @Override
        protected void failed(Throwable t, Description desc) {
            log.error(desc, t);
        }

        @Override
        protected void succeeded(Description desc) {
            log.info(desc);
        }
    };

//    @Before
//    public void showStartedTestName() {
//        log = LogManager.getLogger(testName.getMethodName());
//        log.info(testName.getMethodName().toString() + " test is started" );
//    }
//
//    @After
//    public void after() {
//        log.info(testName.getMethodName().toString() + " test finished" );
//    }

}
