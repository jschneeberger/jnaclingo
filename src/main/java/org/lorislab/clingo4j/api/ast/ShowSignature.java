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

import org.lorislab.clingo4j.api.ast.enums.StatementType;
import org.lorislab.clingo4j.api.ast.Statement.StatementData;
import org.lorislab.clingo4j.api.c.clingo_ast_show_signature;
import org.lorislab.clingo4j.api.c.clingo_ast_statement;
import org.lorislab.clingo4j.util.ASTObject;

/**
 *
 * @author Andrej Petras
 */
public class ShowSignature implements ASTObject<clingo_ast_show_signature>, StatementData {

    private final Signature signature;
    private final boolean csp;

    public ShowSignature(clingo_ast_show_signature s) {
        this(new Signature(s.signature()), s.csp());
    }
    
    public ShowSignature(Signature signature, boolean csp) {
        this.signature = signature;
        this.csp = csp;
    }
    
    public Signature getSignature() {
        return signature;
    }

    public boolean isCsp() {
        return csp;
    }

    @Override
    public String toString() {
        return "#show " + (csp ? "$" : "") + signature + ".";
    }

    @Override
    public clingo_ast_show_signature create() {
        clingo_ast_show_signature ret = new clingo_ast_show_signature();
        ret.csp(csp);
        ret.signature(signature.getPointer());
        return ret;
    }

    @Override
    public void updateStatement(clingo_ast_statement ret) {
        ret.field1().show_signature(createPointer());
    }

    @Override
    public StatementType getStatementType() {
        return StatementType.SHOW_SIGNATURE;
    }
    
}
