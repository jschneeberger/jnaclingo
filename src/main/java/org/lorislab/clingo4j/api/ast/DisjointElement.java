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
import org.lorislab.clingo4j.api.Location;
import org.lorislab.clingo4j.api.c.clingo_ast_disjoint_element;
import org.lorislab.clingo4j.util.ASTObject;
import static org.lorislab.clingo4j.util.ASTObject.pointerList;
import static org.lorislab.clingo4j.util.ASTObject.print;

/**
 *
 * @author Andrej Petras
 */
public class DisjointElement implements ASTObject<clingo_ast_disjoint_element> {

    private final Location location;
    private final List<Term> tuple;
    private final CSPSum term;
    private final List<Literal> condition;

    public DisjointElement(clingo_ast_disjoint_element e) {
        this(new Location(e.location()),Term.list(e.tuple(), e.tuple_size()), new CSPSum(e.term()), Literal.list(e.condition(), e.condition_size()));
    }
    
    public DisjointElement(Location location, List<Term> tuple, CSPSum term, List<Literal> condition) {
        this.location = location;
        this.tuple = tuple;
        this.term = term;
        this.condition = condition;
    }

    public List<Literal> getCondition() {
        return condition;
    }

    public Location getLocation() {
        return location;
    }

    public CSPSum getTerm() {
        return term;
    }

    public List<Term> getTuple() {
        return tuple;
    }

    @Override
    public String toString() {
        return print(tuple, "", ",", "", false) + " : " + term + " : " + print(condition, "", ",", "", false);
    }

    @Override
    public clingo_ast_disjoint_element create() {
        clingo_ast_disjoint_element ret = new clingo_ast_disjoint_element();
        ret.location(location.getStructObject());
        ret.tuple(ASTObject.array(tuple));
        ret.tuple_size(ASTObject.size(tuple));
        ret.term(term.create());
        ret.condition(ASTObject.array(condition));
        ret.condition_size(ASTObject.size(condition));
        return ret;
    }
    
    public static List<DisjointElement> list(Pointer<clingo_ast_disjoint_element> pointer, long size) {
        return pointerList(DisjointElement::new, pointer, size);
    }
    
}
