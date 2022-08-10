/*
 * Copyright 2022 tison <wander4096@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
