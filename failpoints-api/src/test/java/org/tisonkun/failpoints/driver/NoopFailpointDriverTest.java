package org.tisonkun.failpoints.driver;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.tisonkun.failpoints.Failpoints;

public class NoopFailpointDriverTest {

    @Test
    public void testLoadFailpointDriver() {
        Assertions.assertEquals(NoopFailpointDriver.NAME, Failpoints.driverName());
    }

    @Test
    public void testEvalFailpoint() {
        Assertions.assertNull(Failpoints.enable("failpoints-test", () -> true));
        Assertions.assertNull(Failpoints.eval("failpoints-test"));
        Assertions.assertNull(Failpoints.eval("failpoints-test"));
    }

    @Test
    public void testInjectFailpoint() throws Exception {
        final String failpointName = Failpoints.prepend(TestingObject.class, "testing-object");
        Assertions.assertNull(Failpoints.enable(failpointName, () -> true));
        final CountDownLatch latch = new CountDownLatch(1);
        new TestingObject().testInject(latch::countDown);
        Assertions.assertFalse(latch.await(1000, TimeUnit.MILLISECONDS));
    }

}