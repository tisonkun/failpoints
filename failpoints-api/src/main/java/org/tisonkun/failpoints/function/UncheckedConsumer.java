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

package org.tisonkun.failpoints.function;

import java.util.function.Consumer;
import lombok.SneakyThrows;

@FunctionalInterface
public interface UncheckedConsumer<T, E extends Throwable> extends Consumer<T> {
    void acceptUnchecked(T t) throws E;

    @Override
    @SneakyThrows
    default void accept(T t) {
        acceptUnchecked(t);
    }
}
