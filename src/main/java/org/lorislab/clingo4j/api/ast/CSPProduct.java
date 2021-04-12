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
import java.util.Optional;
import org.bridj.Pointer;
import org.lorislab.clingo4j.api.Location;
import org.lorislab.clingo4j.api.c.clingo_ast_csp_product_term;
import org.lorislab.clingo4j.util.ASTObject;
import static org.lorislab.clingo4j.util.ASTObject.pointerList;

/**
 *
 * @author Andrej Petras
 */
public class CSPProduct implements ASTObject<clingo_ast_csp_product_term> {

    private final Location location;
    private final Term coefficient;
    private final Optional<Term> variable;

    public CSPProduct(clingo_ast_csp_product_term term) {
        Term t = null;
        if (term.variable() != null && term.variable().get() != null) {
            t = new Term(term.variable().get());
        }
        location = new Location(term.location());
        coefficient = new Term(term.coefficient());
        variable = Optional.ofNullable(t);
    }

    public CSPProduct(Location location, Term coefficient, Optional<Term> variable) {
        this.location = location;
        this.coefficient = coefficient;
        this.variable = variable;
    }

    public Term getCoefficient() {
        return coefficient;
    }

    public Location getLocation() {
        return location;
    }

    public Optional<Term> getVariable() {
        return variable;
    }

    @Override
    public String toString() {
        if (variable.isPresent()) {
            return "" + coefficient + "$*$" + variable.get();
        }
        return "" + coefficient;
    }

    @Override
    public clingo_ast_csp_product_term create() {
        clingo_ast_csp_product_term ret = new clingo_ast_csp_product_term();
        ret.location(location.getStructObject());
        ret.variable(ASTObject.optionalPointer(variable));
        ret.coefficient(coefficient.create());
        return ret;
    }

    public static List<CSPProduct> list(Pointer<clingo_ast_csp_product_term> pointer, long size) {
        return pointerList(CSPProduct::new, pointer, size);
    }

}
