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
import org.lorislab.clingo4j.api.Location;
import org.lorislab.clingo4j.api.c.clingo_ast_csp_sum_term;
import org.lorislab.clingo4j.util.ASTObject;
import static org.lorislab.clingo4j.util.ASTObject.print;

/**
 *
 * @author Andrej Petras
 */
public class CSPSum implements ASTObject<clingo_ast_csp_sum_term> {

    private final Location location;

    private final List<CSPProduct> terms;

    public CSPSum(clingo_ast_csp_sum_term term) {
        this(new Location(term.location()), CSPProduct.list(term.terms(), term.size()));
    }

    public CSPSum(Location location, List<CSPProduct> terms) {
        this.location = location;
        this.terms = terms;
    }

    public Location getLocation() {
        return location;
    }

    public List<CSPProduct> getTerms() {
        return terms;
    }

    @Override
    public String toString() {
        if (terms == null || terms.isEmpty()) {
            return "0";
        } else {
            return print(terms, "", "$+", "", false);
        }
    }

    @Override
    public clingo_ast_csp_sum_term create() {
        clingo_ast_csp_sum_term ret = new clingo_ast_csp_sum_term();
        ret.location(location.getStructObject());
        ret.terms(ASTObject.array(terms));
        ret.size(ASTObject.size(terms));
        return ret;
    }
}
