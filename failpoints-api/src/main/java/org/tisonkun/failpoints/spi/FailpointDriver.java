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

package org.tisonkun.failpoints.spi;

import org.tisonkun.failpoints.FailpointGuard;
import org.tisonkun.failpoints.function.UncheckedConsumer;
import org.tisonkun.failpoints.function.UncheckedSupplier;

/**
 * A driver manages failpoints globally and properly inject them.
 *
 * @see org.tisonkun.failpoints.Failpoints
 */
public interface FailpointDriver {
    /**
     * The priority of this driver. The driver who has the highest priority will be used globally.
     *
     * @return the priority of this driver.
     */
    int priority();

    /**
     * The name of this driver.
     *
     * @return the name of this driver.
     */
    String name();

    /**
     * Enable a failpoint with a supplier that produces the result on {@link #eval(String)}ed.
     *
     * @param name the name of the failpoint.
     * @param supplier the supplier that produces the result on evaluated.
     * @return a failpoint guard during whose lifecycle this failpoint is enabled.
     *
     * @param <T> supplier's return type.
     */
    <T> FailpointGuard enable(String name, UncheckedSupplier<T, ?> supplier);

    /**
     * Disable a registered failpoint.
     *
     * @param name the name of the failpoint.
     */
    void disable(String name);

    /**
     * Disable all the registered failpoints.
     */
    void disableAll();

    /**
     * Manually evaluate a failpoint.
     *
     * @param name the name of the failpoint.
     * @return the result produced by failpoint's supplier
     * @param <T> the expected result type.
     */
    <T> T eval(String name);

    /**
     * Inject a failpoint with a consumer to consume its evaluated result.
     *
     * @param name the name of the failpoint.
     * @param consumer user-defined logic to consume failpoint's evaluated result.
     */
    void inject(String name, UncheckedConsumer<?, ?> consumer);
}
