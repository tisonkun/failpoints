package org.tisonkun.failpoints.driver;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.tisonkun.failpoints.FailpointGuard;
import org.tisonkun.failpoints.Failpoints;

public class SimpleFailpointDriverTest {

    @Test
    public void testLoadFailpointDriver() {
        Assertions.assertEquals(SimpleFailpointDriver.NAME, Failpoints.driverName());
    }

    @Test
    public void testEvalFailpoint() {
        try (final FailpointGuard ignored = Failpoints.enable("failpoints-test", () -> true)) {
            Assertions.assertEquals(true, Failpoints.eval("failpoints-test"));
        }
        Assertions.assertNull(Failpoints.eval("failpoints-test"));
    }

    @Test
    public void testInjectFailpoint() throws Exception {
        final String failpointName = Failpoints.prepend(TestingObject.class, "testing-object");
        final FailpointGuard guard = Failpoints.enable(failpointName, () -> true);
        {
            final CountDownLatch latch = new CountDownLatch(1);
            new TestingObject().testInject(latch::countDown);
            Assertions.assertTrue(latch.await(1000, TimeUnit.MILLISECONDS));
        }
        guard.close();
        Assertions.assertTrue(guard.closed());
        {
            final CountDownLatch latch = new CountDownLatch(1);
            new TestingObject().testInject(latch::countDown);
            Assertions.assertFalse(latch.await(1000, TimeUnit.MILLISECONDS));
        }
    }

}