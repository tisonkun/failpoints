# Failpoints

![Maven Central Version](https://img.shields.io/maven-central/v/org.tisonkun.failpoints/failpoints-api)

An implementation of [failpoints](http://www.freebsd.org/cgi/man.cgi?query=fail) for Java. Fail points are used to add code points where errors may be injected in a user controlled fashion. Fail point is a code snippet that is only executed when the corresponding failpoint is active.

## Quick Start

1. Add failpoints library in dependencies.

```xml
<dependencies>
<dependency>
  <groupId>org.tisonkun.failpoints</groupId>
  <artifactId>failpoints-api</artifactId>
  <version>2.1.1</version>
</dependency>
<dependency>
    <groupId>org.tisonkun.failpoints</groupId>
    <artifactId>failpoints-simple</artifactId>
    <version>2.1.1</version>
    <scope>test</scope>
</dependency>
</dependencies>
```

2. Inject failpoints to your program, eg:

```java
import org.tisonkun.failpoints.Failpoints;

public class TestingObject {
    public void testInject(Runnable r) {
        Failpoints.inject(getClass(), "testing-object", v -> {
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
        try (final FailpointGuard ignored = Failpoints.enable(TestingObject, "testing-object", true)) {
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
