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

import java.util.List;
import org.bridj.Pointer;
import org.lorislab.clingo4j.api.c.clingo_ast_head_aggregate_element;
import org.lorislab.clingo4j.util.ASTObject;
import static org.lorislab.clingo4j.util.ASTObject.pointerList;
import static org.lorislab.clingo4j.util.ASTObject.print;

/**
 *
 * @author Andrej Petras
 */
public class HeadAggregateElement implements ASTObject<clingo_ast_head_aggregate_element> {

    private final List<Term> tuple;
    private final ConditionalLiteral condition;
    
    public HeadAggregateElement(clingo_ast_head_aggregate_element e) {
        this(Term.list(e.tuple(), e.tuple_size()), new ConditionalLiteral(e.conditional_literal()));
    }
    
    public HeadAggregateElement(List<Term> tuple, ConditionalLiteral condition) {
        this.tuple = tuple;
        this.condition = condition;
    }

    public ConditionalLiteral getCondition() {
        return condition;
    }

    public List<Term> getTuple() {
        return tuple;
    }

    @Override
    public String toString() {
        return print(tuple, "", ",", "", false) + " : " + condition;
    }

    @Override
    public clingo_ast_head_aggregate_element create() {
        clingo_ast_head_aggregate_element ret = new clingo_ast_head_aggregate_element();
        ret.tuple(ASTObject.array(tuple));
        ret.tuple_size(ASTObject.size(tuple));
        ret.conditional_literal(condition.create());
        return ret;
    }
    
    public static List<HeadAggregateElement> list(Pointer<clingo_ast_head_aggregate_element> pointer, long size) {
        return pointerList(HeadAggregateElement::new, pointer, size);
    }
    
}
