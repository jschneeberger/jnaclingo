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
import org.lorislab.clingo4j.api.c.clingo_ast_theory_atom_element;
import org.lorislab.clingo4j.util.ASTObject;
import static org.lorislab.clingo4j.util.ASTObject.pointerList;
import static org.lorislab.clingo4j.util.ASTObject.print;

/**
 *
 * @author Andrej Petras
 */
public class TheoryAtomElement implements ASTObject<clingo_ast_theory_atom_element> {

    private final List<TheoryTerm> tuple;
    private final List<Literal> condition;

    public TheoryAtomElement(clingo_ast_theory_atom_element e) {
        this(TheoryTerm.list(e.tuple(), e.tuple_size()), Literal.list(e.condition(), e.condition_size()));
    }

    public TheoryAtomElement(List<TheoryTerm> tuple, List<Literal> condition) {
        this.tuple = tuple;
        this.condition = condition;
    }

    public List<Literal> getCondition() {
        return condition;
    }

    public List<TheoryTerm> getTuple() {
        return tuple;
    }

    @Override
    public clingo_ast_theory_atom_element create() {
        clingo_ast_theory_atom_element ret = new clingo_ast_theory_atom_element();
        ret.tuple(ASTObject.array(tuple));
        ret.tuple_size(ASTObject.size(tuple));
        ret.condition(ASTObject.array(condition));
        ret.condition_size(ASTObject.size(condition));
        return ret;
    }

    @Override
    public String toString() {
        return print(tuple, "", ",", "", false) + " : " + print(condition, "", ",", "", false);
    }

    public static List<TheoryAtomElement> list(Pointer<clingo_ast_theory_atom_element> pointer, long size) {
        return pointerList(TheoryAtomElement::new, pointer, size);
    }

}
