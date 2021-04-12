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
import org.lorislab.clingo4j.api.c.clingo_ast_statement;

/**
 *
 * @author Andrej Petras
 */
public class ProjectSignature implements StatementData {
    
    private final Signature signature;

    public ProjectSignature(long signature) {
        this(new Signature(signature));
    }
    
    public ProjectSignature(Signature signature) {
        this.signature = signature;
    }
    
    public Signature getSignature() {
        return signature;
    }

    @Override
    public String toString() {
        return "#project " + signature + ".";
    }

    @Override
    public void updateStatement(clingo_ast_statement ret) {
        ret.field1().project_signature(signature.getPointer());
    }

    @Override
    public StatementType getStatementType() {
        return StatementType.PROJECT_ATOM_SIGNATURE;
    }
    
    
}
