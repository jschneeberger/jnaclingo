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
import org.lorislab.clingo4j.api.c.clingo_ast_heuristic;
import org.lorislab.clingo4j.api.c.clingo_ast_statement;
import org.lorislab.clingo4j.util.ASTObject;
import static org.lorislab.clingo4j.util.ASTObject.printBody;

/**
 *
 * @author Andrej Petras
 */
public class Heuristic implements ASTObject<clingo_ast_heuristic>, StatementData {
    
    private final Term atom;
    private final List<BodyLiteral> body;
    private final Term bias;
    private final Term priority;
    private final Term modifier;    

    public Heuristic(clingo_ast_heuristic h) {
        this(new Term(h.atom()), BodyLiteral.list(h.body(), h.size()), new Term(h.bias()), new Term(h.priority()), new Term(h.modifier()));
    }
    
    public Heuristic(Term atom, List<BodyLiteral> body, Term bias, Term priority, Term modifier) {
        this.atom = atom;
        this.body = body;
        this.bias = bias;
        this.priority = priority;
        this.modifier = modifier;
    }

    public Term getAtom() {
        return atom;
    }

    public Term getBias() {
        return bias;
    }

    public List<BodyLiteral> getBody() {
        return body;
    }

    public Term getModifier() {
        return modifier;
    }

    public Term getPriority() {
        return priority;
    }

    @Override
    public String toString() {
        return "#heuristic " + atom + printBody(body) + " [" + bias+ "@" + priority + "," + modifier + "]";
    }

    @Override
    public clingo_ast_heuristic create() {
        clingo_ast_heuristic ret = new clingo_ast_heuristic();
        ret.atom(atom.create());
        ret.bias(bias.create());
        ret.priority(priority.create());
        ret.modifier(modifier.create());
        ret.body(ASTObject.array(body));
        ret.size(ASTObject.size(body));
        return ret;
    }

    @Override
    public void updateStatement(clingo_ast_statement ret) {
        ret.field1().heuristic(createPointer());
    }

    @Override
    public StatementType getStatementType() {
        return StatementType.HEURISTIC;
    }
    
}
