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
import org.lorislab.clingo4j.api.c.clingo_ast_show_term;
import org.lorislab.clingo4j.api.c.clingo_ast_statement;
import org.lorislab.clingo4j.util.ASTObject;
import static org.lorislab.clingo4j.util.ASTObject.printBody;

/**
 *
 * @author Andrej Petras
 */
public class ShowTerm implements ASTObject<clingo_ast_show_term>, StatementData {

    private final Term term;
    private final List<BodyLiteral> body;
    private final boolean csp;

    public ShowTerm(clingo_ast_show_term t) {
        this(new Term(t.term()), BodyLiteral.list(t.body(), t.size()), t.csp());
    }
    
    public ShowTerm(Term term, List<BodyLiteral> body, boolean csp) {
        this.term = term;
        this.body = body;
        this.csp = csp;
    }

    public List<BodyLiteral> getBody() {
        return body;
    }

    public Term getTerm() {
        return term;
    }

    public boolean isCsp() {
        return csp;
    }

    @Override
    public String toString() {
        return  "#show " + (csp ? "$" : "") + term + printBody(body);
    }

    @Override
    public clingo_ast_show_term create() {
        clingo_ast_show_term ret = new clingo_ast_show_term();
        ret.csp(csp);
        ret.term(term.create());
        ret.body(ASTObject.array(body));
        ret.size(ASTObject.size(body));
        return ret;
    }

    @Override
    public void updateStatement(clingo_ast_statement ret) {
        ret.field1().show_term(createPointer());
    }

    @Override
    public StatementType getStatementType() {
        return StatementType.SHOW_TERM;
    }
    
}
