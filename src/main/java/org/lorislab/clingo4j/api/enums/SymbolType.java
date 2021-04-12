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
package org.lorislab.clingo4j.api.enums;
import org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_symbol_type;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_symbol_type.clingo_symbol_type_function;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_symbol_type.clingo_symbol_type_infimum;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_symbol_type.clingo_symbol_type_number;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_symbol_type.clingo_symbol_type_string;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_symbol_type.clingo_symbol_type_supremum;
import org.lorislab.clingo4j.util.EnumValue;

/**
 *
 * @author Andrej Petras
 */
public enum SymbolType implements EnumValue<clingo_symbol_type> {
    
    INFIMUM(clingo_symbol_type_infimum),
    
    NUMBER(clingo_symbol_type_number),
    
    STRING(clingo_symbol_type_string),
    
    FUNCTION(clingo_symbol_type_function),
    
    SUPREMUM(clingo_symbol_type_supremum);
    
    private clingo_symbol_type type;
        
    private SymbolType(clingo_symbol_type type) {
        this.type = type;
    }

    @Override
    public clingo_symbol_type getValue() {
        return type;
    }

}
