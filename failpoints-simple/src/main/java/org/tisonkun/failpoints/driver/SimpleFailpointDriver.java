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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.tisonkun.failpoints.Failpoint;
import org.tisonkun.failpoints.FailpointGuard;
import org.tisonkun.failpoints.function.UncheckedConsumer;
import org.tisonkun.failpoints.function.UncheckedSupplier;
import org.tisonkun.failpoints.spi.FailpointDriver;

/**
 * A failpoint driver that using a global map to manage failpoints (de)registrations.
 */
@Slf4j
public class SimpleFailpointDriver implements FailpointDriver {
    public static final String NAME = "SimpleFailpointDriver";
    private final Map<String, Failpoint<?>> failpointMap = new ConcurrentHashMap<>();

    @Override
    public int priority() {
        return 100;
    }

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public <T> FailpointGuard enable(String name, UncheckedSupplier<T, ?> supplier) {
        log.debug("Enabling failpoint {}", name);

        final Failpoint<T> failpoint = new Failpoint<>(supplier);
        if (this.failpointMap.putIfAbsent(name, failpoint) != null) {
            throw new IllegalStateException("failpoint " + name + " has been already registered");
        }
        return new FailpointGuard(failpoint, ignored -> this.failpointMap.remove(name));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T eval(String name) {
        final Failpoint<?> failpoint = this.failpointMap.get(name);
        if (failpoint != null) {
            return (T) failpoint.eval();
        }
        return null;
    }

    @Override
    public void inject(String name, UncheckedConsumer<?, ?> consumer) {
        consumer.accept(eval(name));
    }
}
