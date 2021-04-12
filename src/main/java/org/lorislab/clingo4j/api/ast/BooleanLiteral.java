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
package org.lorislab.clingo4j.api.ast;

import org.lorislab.clingo4j.api.ast.enums.LiteralType;
import org.lorislab.clingo4j.api.ast.Literal.LiteralData;
import org.lorislab.clingo4j.api.c.clingo_ast_literal;

/**
 *
 * @author Andrej Petras
 */
public class BooleanLiteral implements LiteralData {
    
    private final boolean value;

    public BooleanLiteral(boolean value) {
        this.value = value;
    }

    public boolean isValue() {
        return value;
    }

    @Override
    public String toString() {
        return value ? "#true" : "#false";
    }

    @Override
    public void updateLiteral(clingo_ast_literal ret) {
        ret.field1().boolean$(value);
    }

    @Override
    public LiteralType getLiteralType() {
        return LiteralType.BOOLEAN;
    }
    
}
