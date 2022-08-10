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

package org.tisonkun.failpoints;

import java.util.Comparator;
import java.util.ServiceLoader;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.tisonkun.failpoints.driver.NoopFailpointDriver;
import org.tisonkun.failpoints.function.UncheckedConsumer;
import org.tisonkun.failpoints.function.UncheckedSupplier;
import org.tisonkun.failpoints.spi.FailpointDriver;

public class Failpoints {

    private static final FailpointDriver DRIVER;

    static {
        final ServiceLoader<FailpointDriver> loader = ServiceLoader.load(FailpointDriver.class);
        final Stream<FailpointDriver> drivers = StreamSupport.stream(loader.spliterator(), false);
        DRIVER = drivers.max(Comparator.comparing(FailpointDriver::priority)).orElse(new NoopFailpointDriver());
    }

    public static String driverName() {
        return DRIVER.name();
    }

    public static <T> Failpoint<T> enable(String name, UncheckedSupplier<T, ?> supplier) {
        return DRIVER.enable(name, supplier);
    }

    public static <T> T eval(String name) {
        return DRIVER.eval(name);
    }

    public static void inject(String name, UncheckedConsumer<?, ?> consumer) {
        DRIVER.inject(name, consumer);
    }

}
