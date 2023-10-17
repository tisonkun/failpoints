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

import org.tisonkun.failpoints.FailpointGuard;
import org.tisonkun.failpoints.function.UncheckedConsumer;
import org.tisonkun.failpoints.function.UncheckedSupplier;
import org.tisonkun.failpoints.spi.FailpointDriver;

/**
 * A failpoint driver that basically does nothing.
 */
public class NoopFailpointDriver implements FailpointDriver {
    public static final String NAME = "NoopFailpointDriver";

    @Override
    public int priority() {
        return 0;
    }

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public <T> FailpointGuard enable(String name, UncheckedSupplier<T, ?> supplier) {
        return null;
    }

    @Override
    public void disable(String name) {}

    @Override
    public void disableAll() {}

    @Override
    public <T> T eval(String name) {
        return null;
    }

    @Override
    public void inject(String name, UncheckedConsumer<?, ?> consumer) {}
}
