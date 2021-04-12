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
package org.lorislab.clingo4j.api;

import org.bridj.Pointer;
import static org.lorislab.clingo4j.api.Clingo.LIB;
import org.lorislab.clingo4j.api.ast.Statement;
import org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_program_builder;
import static org.lorislab.clingo4j.api.Clingo.handleError;
import org.lorislab.clingo4j.util.PointerObject;

/**
 *
 * @author Andrej Petras
 */
public class ProgramBuilder extends PointerObject<clingo_program_builder> {
    
    public ProgramBuilder(Pointer<clingo_program_builder> pointer) {
        super(pointer);
    }

    public void begin() throws ClingoException {
        handleError(LIB.clingo_program_builder_begin(pointer), "Error program builder begin!");
    }

    public void add(Statement statement) throws ClingoException {
        handleError(LIB.clingo_program_builder_add(pointer, statement.createPointer()), "Error program builder add!");            
    }
    
    public void end() throws ClingoException {
        handleError(LIB.clingo_program_builder_end(pointer), "Error program builder begin!");
    }
        
}
