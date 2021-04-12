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

import org.lorislab.clingo4j.api.ast.enums.BodyLiteralType;
import java.util.List;
import org.bridj.Pointer;
import org.lorislab.clingo4j.api.ast.BodyLiteral.BodyLiteralData;
import org.lorislab.clingo4j.api.c.clingo_ast_body_literal;
import org.lorislab.clingo4j.api.c.clingo_ast_conditional_literal;
import org.lorislab.clingo4j.util.ASTObject;
import static org.lorislab.clingo4j.util.ASTObject.pointerList;
import static org.lorislab.clingo4j.util.ASTObject.print;

/**
 *
 * @author Andrej Petras
 */
public class ConditionalLiteral implements ASTObject<clingo_ast_conditional_literal>, BodyLiteralData {

    private final Literal literal;
    private final List<Literal> condition;

    public ConditionalLiteral(clingo_ast_conditional_literal lit) {
        this(new Literal(lit.literal()), Literal.list(lit.condition(), lit.size()));
    }

    public ConditionalLiteral(Literal literal, List<Literal> condition) {
        this.literal = literal;
        this.condition = condition;
    }

    public List<Literal> getCondition() {
        return condition;
    }

    public Literal getLiteral() {
        return literal;
    }

    public clingo_ast_conditional_literal create() {
        clingo_ast_conditional_literal ret = new clingo_ast_conditional_literal();
        ret.literal(literal.create());
        ret.condition(ASTObject.array(condition));
        ret.size(ASTObject.size(condition));
        return ret;
    }

    @Override
    public String toString() {
        return "" + literal + print(condition, " : ", ", ", "", true);
    }

    @Override
    public void updateBodyLiteral(clingo_ast_body_literal ret) {
        ret.field1().conditional(createPointer());
    }

    @Override
    public BodyLiteralType getBodyLiteralType() {
        return BodyLiteralType.CONDITIONAL;
    }

    public static List<ConditionalLiteral> list(Pointer<clingo_ast_conditional_literal> pointer, long size) {
        return pointerList(ConditionalLiteral::new, pointer, size);
    }

}
