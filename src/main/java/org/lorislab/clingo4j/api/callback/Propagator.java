/*
 * Copyright 2017 Andrej Petras.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.lorislab.clingo4j.api.callback;

import java.util.List;
import org.lorislab.clingo4j.api.PropagateControl;
import org.lorislab.clingo4j.api.PropagateInit;

/**
 *
 * @author Andrej Petras
 */
public interface Propagator {

    default void init(PropagateInit init) {
    }

    default void propagate(PropagateControl ctl, List<Integer> changes) {
    }

    default void undo(PropagateControl ctl, List<Integer> changes) {
    }

    default void check(PropagateControl ctl) {
    }
}
