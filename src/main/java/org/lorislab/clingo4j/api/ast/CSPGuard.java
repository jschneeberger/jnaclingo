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

import org.lorislab.clingo4j.api.ast.enums.ComparisonOperator;
import java.util.List;
import org.bridj.Pointer;
import org.lorislab.clingo4j.api.c.clingo_ast_csp_guard;
import org.lorislab.clingo4j.util.ASTObject;
import static org.lorislab.clingo4j.util.ASTObject.pointerList;
import org.lorislab.clingo4j.util.EnumValue;

/**
 *
 * @author Andrej Petras
 */
public class CSPGuard implements ASTObject<clingo_ast_csp_guard> {

    private final ComparisonOperator operator;

    private final CSPSum term;

    public CSPGuard(clingo_ast_csp_guard csp) {
        this(EnumValue.valueOfInt(ComparisonOperator.class, csp.comparison()), new CSPSum(csp.term()));
    }
    
    public CSPGuard(ComparisonOperator operator, CSPSum term) {
        this.operator = operator;
        this.term = term;
    }
    
    public ComparisonOperator getOperator() {
        return operator;
    }

    public CSPSum getTerm() {
        return term;
    }

    @Override
    public String toString() {
        return "$" + operator + term;
    }

    public clingo_ast_csp_guard create() {
        clingo_ast_csp_guard ret = new clingo_ast_csp_guard();
        ret.comparison(operator.getInt());
        ret.term(term.create());
        return ret;
    }
    
    public static List<CSPGuard> list(Pointer<clingo_ast_csp_guard> pointer, long size) {
        return pointerList(CSPGuard::new, pointer, size);
    }
    
}
