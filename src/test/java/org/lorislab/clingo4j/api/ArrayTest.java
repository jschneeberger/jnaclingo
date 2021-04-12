/*
 * Copyright 2018 Andrej Petras.
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
package org.lorislab.clingo4j.api;

import java.util.ArrayList;
import java.util.List;
import org.bridj.Pointer;
import org.junit.Assert;
import org.junit.Test;
import org.lorislab.clingo4j.api.ast.Definition;
import org.lorislab.clingo4j.api.ast.Term;
import org.lorislab.clingo4j.api.c.clingo_ast_definition;
import org.lorislab.clingo4j.util.ASTObject;

/**
 *
 * @author Andrej Petras
 */
public class ArrayTest {

    @Test
    public void test() {
        Clingo.init("src/main/clingo");

        try (Clingo control = Clingo.create()) {
            
            Location loc = new Location("<generated>", "<generated>", 1, 1, 1, 1);
            List<Definition> elements = new ArrayList<>(1);
            elements.add(new Definition("e", new Term(loc, Symbol.createNumber(24)), false));

            Pointer<clingo_ast_definition> tmp = ASTObject.array(elements);
            Assert.assertNotNull("The array pointer is null!", tmp);
            Assert.assertNotNull("The array IO pointer is null!", tmp.getIO());
            
        } catch (ClingoException ex) {
            ex.printStackTrace();
        }
    }
}
