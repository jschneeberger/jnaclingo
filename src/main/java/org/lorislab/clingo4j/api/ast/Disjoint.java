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
import org.lorislab.clingo4j.api.ast.BodyLiteral.BodyLiteralData;
import org.lorislab.clingo4j.api.c.clingo_ast_body_literal;
import org.lorislab.clingo4j.api.c.clingo_ast_disjoint;
import org.lorislab.clingo4j.util.ASTObject;
import static org.lorislab.clingo4j.util.ASTObject.print;

/**
 *
 * @author Andrej Petras
 */
public class Disjoint implements ASTObject<clingo_ast_disjoint>, BodyLiteralData {

    private final List<DisjointElement> elements;

    public Disjoint(clingo_ast_disjoint d) {
        this(DisjointElement.list(d.elements(), d.size()));
    }

    public Disjoint(List<DisjointElement> elements) {
        this.elements = elements;
    }

    public List<DisjointElement> getElements() {
        return elements;
    }

    @Override
    public String toString() {
        return "#disjoint { " + print(elements, "", "; ", "", false) + " }";
    }

    @Override
    public clingo_ast_disjoint create() {
        clingo_ast_disjoint ret = new clingo_ast_disjoint();
        ret.size(ASTObject.size(elements));
        ret.elements(ASTObject.array(elements));
        return ret;
    }

    @Override
    public void updateBodyLiteral(clingo_ast_body_literal ret) {
        ret.field1().disjoint(createPointer());
    }

    @Override
    public BodyLiteralType getBodyLiteralType() {
        return BodyLiteralType.DISJOINT;
    }

}
