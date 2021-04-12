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
import org.lorislab.clingo4j.api.ast.enums.HeadLiteralType;
import org.lorislab.clingo4j.util.EnumValue;
import java.util.List;
import java.util.Optional;
import org.lorislab.clingo4j.api.ast.HeadLiteral.HeadLiteralData;
import org.lorislab.clingo4j.api.c.clingo_ast_head_aggregate;
import org.lorislab.clingo4j.api.c.clingo_ast_head_literal;
import org.lorislab.clingo4j.util.ASTObject;
import static org.lorislab.clingo4j.util.ASTObject.print;

/**
 *
 * @author Andrej Petras
 */
public class HeadAggregate implements ASTObject<clingo_ast_head_aggregate>, HeadLiteralData {

    private final AggregateFunction function;
    private final List<HeadAggregateElement> elements;
    private final Optional<AggregateGuard> leftGuard;
    private final Optional<AggregateGuard> rightGuard;

    public HeadAggregate(clingo_ast_head_aggregate h) {
        this(EnumValue.valueOfInt(AggregateFunction.class, h.function()), HeadAggregateElement.list(h.elements(), h.size()), ASTObject.optional(AggregateGuard::new, h.left_guard()), ASTObject.optional(AggregateGuard::new, h.right_guard()));
    }

    public HeadAggregate(AggregateFunction function, List<HeadAggregateElement> elements, Optional<AggregateGuard> leftGuard, Optional<AggregateGuard> rightGuard) {
        this.function = function;
        this.elements = elements;
        this.leftGuard = leftGuard;
        this.rightGuard = rightGuard;
    }

    public List<HeadAggregateElement> getElements() {
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
        sb.append(function).append(" { ").append(print(elements, "", ": ", "", false)).append(" }");
        if (rightGuard.isPresent()) {
            sb.append(" ").append(rightGuard.get().getOperator()).append(" ").append(rightGuard.get().getTerm());
        }
        return sb.toString();
    }

    @Override
    public void updateHeadLiteral(clingo_ast_head_literal ret) {
        ret.field1().head_aggregate(createPointer());
    }

    @Override
    public clingo_ast_head_aggregate create() {
        clingo_ast_head_aggregate head_aggregate = new clingo_ast_head_aggregate();
        head_aggregate.left_guard(ASTObject.optionalPointer(leftGuard));
        head_aggregate.right_guard(ASTObject.optionalPointer(rightGuard));
        head_aggregate.function(function.getInt());
        head_aggregate.elements(ASTObject.array(elements));
        head_aggregate.size(ASTObject.size(elements));
        return head_aggregate;
    }

    @Override
    public HeadLiteralType getHeadLiteralType() {
        return HeadLiteralType.HEAD_AGGREGATE;
    }

}
