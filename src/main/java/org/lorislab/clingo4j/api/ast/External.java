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
import java.util.List;
import org.lorislab.clingo4j.api.ast.Statement.StatementData;
import org.lorislab.clingo4j.api.c.clingo_ast_external;
import org.lorislab.clingo4j.api.c.clingo_ast_statement;
import org.lorislab.clingo4j.util.ASTObject;
import static org.lorislab.clingo4j.util.ASTObject.printBody;

/**
 *
 * @author Andrej Petras
 */
public class External implements ASTObject<clingo_ast_external>, StatementData {

    private final Term atom;
    private final List<BodyLiteral> body;

    public External(clingo_ast_external e) {
        this(new Term(e.atom()), BodyLiteral.list(e.body(), e.size()));
    }
    
    public External(Term atom, List<BodyLiteral> body) {
        this.atom = atom;
        this.body = body;
    }

    public Term getAtom() {
        return atom;
    }

    public List<BodyLiteral> getBody() {
        return body;
    }

    @Override
    public String toString() {
        return "#external " + atom + printBody(body);
    }

    @Override
    public clingo_ast_external create() {
        clingo_ast_external ret = new clingo_ast_external();
        ret.atom(atom.create());
        ret.body(ASTObject.array(body));
        ret.size(ASTObject.size(body));
        return ret;
    }

    @Override
    public void updateStatement(clingo_ast_statement ret) {
        ret.field1().external(createPointer());
    }

    @Override
    public StatementType getStatementType() {
        return StatementType.EXTERNAL;
    }
    
}
