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

import org.lorislab.clingo4j.api.ast.enums.AggregateFunction;
import org.lorislab.clingo4j.api.ast.enums.BodyLiteralType;
import org.lorislab.clingo4j.util.EnumValue;
import java.util.List;
import java.util.Optional;
import org.lorislab.clingo4j.api.ast.BodyLiteral.BodyLiteralData;
import org.lorislab.clingo4j.api.c.clingo_ast_body_aggregate;
import org.lorislab.clingo4j.api.c.clingo_ast_body_literal;
import org.lorislab.clingo4j.util.ASTObject;
import static org.lorislab.clingo4j.util.ASTObject.print;

/**
 *
 * @author Andrej Petras
 */
public class BodyAggregate implements ASTObject<clingo_ast_body_aggregate>, BodyLiteralData {

    private final AggregateFunction function;
    private final List<BodyAggregateElement> elements;
    private final Optional<AggregateGuard> leftGuard;
    private final Optional<AggregateGuard> rightGuard;

    public BodyAggregate(clingo_ast_body_aggregate a) {
        this(EnumValue.valueOfInt(AggregateFunction.class, a.function()), BodyAggregateElement.list(a.elements(), a.size()), ASTObject.optional(AggregateGuard::new, a.left_guard()), ASTObject.optional(AggregateGuard::new, a.right_guard()));
    }

    public BodyAggregate(AggregateFunction function, List<BodyAggregateElement> elements, Optional<AggregateGuard> leftGuard, Optional<AggregateGuard> rightGuard) {
        this.function = function;
        this.elements = elements;
        this.leftGuard = leftGuard;
        this.rightGuard = rightGuard;
    }

    public List<BodyAggregateElement> getElements() {
        return elements;
    }

    public AggregateFunction getFunction() {
        return function;
    }

    public Optional<AggregateGuard> getLeftGuard() {
        return leftGuard;
    }

    public Optional<AggregateGuard> getRightGuard() {
        return rightGuard;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (leftGuard.isPresent()) {
            sb.append(leftGuard.get().getTerm()).append(" ").append(leftGuard.get().getOperator()).append(" ");
        }
        sb.append(" { ").append(print(elements, "", "; ", "", false)).append(" }");
        if (rightGuard.isPresent()) {
            sb.append(" ").append(rightGuard.get().getOperator()).append(" ").append(rightGuard.get().getTerm());
        }
        return sb.toString();
    }

    @Override
    public clingo_ast_body_aggregate create() {
        clingo_ast_body_aggregate ret = new clingo_ast_body_aggregate();
        ret.left_guard(ASTObject.optionalPointer(leftGuard));
        ret.right_guard(ASTObject.optionalPointer(rightGuard));
        ret.function(function.getInt());
        ret.size(ASTObject.size(elements));
        ret.elements(ASTObject.array(elements));
        return ret;
    }

    @Override
    public void updateBodyLiteral(clingo_ast_body_literal ret) {
        ret.field1().body_aggregate(createPointer());
    }

    @Override
    public BodyLiteralType getBodyLiteralType() {
        return BodyLiteralType.BODY_AGGREGATE;
    }

}
