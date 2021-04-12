/*
 * Copyright 2018 Andrej Petras.
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
package org.lorislab.clingo4j.api.ast.enums;

import org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_solve_mode;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_solve_mode.clingo_solve_mode_async;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_solve_mode.clingo_solve_mode_yield;
import org.lorislab.clingo4j.util.EnumValue;

/**
 *
 * @author Andrej Petras
 */
public enum SolveMode implements EnumValue<clingo_solve_mode> {

    ASYNC(clingo_solve_mode_async),
    
    YIELD(clingo_solve_mode_yield);

    private final clingo_solve_mode mode;

    private SolveMode(clingo_solve_mode mode) {
        this.mode = mode;
    }

    @Override
    public clingo_solve_mode getValue() {
        return mode;
    }

}
