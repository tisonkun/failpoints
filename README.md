# Failpoints

An implementation of [failpoints](http://www.freebsd.org/cgi/man.cgi?query=fail) for Java. Fail points are used to add code points where errors may be injected in a user controlled fashion. Fail point is a code snippet that is only executed when the corresponding failpoint is active.

## Quick Start

1. Add failpoints library in dependencies.
2. Inject failpoints to your program, eg:

```java
import org.tisonkun.failpoints.Failpoints;

public class MyClass {
    public void method(Runnable r) {
        Failpoints.inject(Failpoints.prepend(getClass(), "testing-object"), v -> {
            if (v != null && (boolean) v) {
                r.run();
            }
        });
    }
}
```

3. In testing code, enable the failpoint:

```java
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.tisonkun.failpoints.FailpointGuard;
import org.tisonkun.failpoints.Failpoints;

public class SimpleFailpointDriverTest {
    public void testInjectFailpoint() throws Exception {
        final String failpointName = Failpoints.prepend(MyClass.class, "testing-object");
        try (final FailpointGuard ignored = Failpoints.enable(failpointName, () -> true)) {
            final CountDownLatch latch = new CountDownLatch(1);
            new TestingObject().testInject(latch::countDown);
            Assertions.assertTrue(latch.await(1000, TimeUnit.MILLISECONDS));
        }
        final CountDownLatch latch = new CountDownLatch(1);
        new TestingObject().testInject(latch::countDown);
        Assertions.assertFalse(latch.await(1000, TimeUnit.MILLISECONDS));
    }
}
```
