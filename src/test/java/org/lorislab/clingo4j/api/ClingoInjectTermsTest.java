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
package org.lorislab.clingo4j.api;

import org.lorislab.clingo4j.api.callback.GroundCallback;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.junit.Assert;
import org.junit.Test;
import org.lorislab.clingo4j.api.ast.Definition;
import org.lorislab.clingo4j.api.ast.Statement;
import org.lorislab.clingo4j.api.ast.Term;

/**
 *
 * @author Andrej Petras
 */
public class ClingoInjectTermsTest {

    private static final Set<String> RESULTS = new HashSet<String>() {
        {
            add("p(23)");
            add("p(24)");
            add("p(42)");
            add("p(43)");
        }
    };

    @Test
    public void controlTest() {

        Set<String> result = new HashSet<>(RESULTS);

        Clingo.init("src/main/clingo");

        try (Clingo control = Clingo.create()) {

            // define a constant in string form
            Symbol number = Symbol.createNumber(23);
            control.add("base", "#const d=" + number + ".");

            // define a constant via the AST
            control.withBuilder((ProgramBuilder builder) -> {
                Location loc = new Location("<generated>", "<generated>", 1, 1, 1, 1);
                builder.add(new Statement(loc, new Definition("e", new Term(loc, Symbol.createNumber(24)), false)));
            });

            control.add("base", "p(@c()). p(d). p(e).");

            // inject terms via a callback
            control.ground("base", (Location loc, String name, List<Symbol> symbols, GroundCallback.GroundSymbolCallback callback) -> {
                if ("c".equals(name) && (symbols == null || symbols.isEmpty())) {
                    List<Symbol> tmp = new ArrayList<>(2);
                    tmp.add(Symbol.createNumber(42));
                    tmp.add(Symbol.createNumber(43));
                    callback.apply(tmp);
                }
            });

            try (SolveHandle handle = control.solve()) {
                for (Model model : handle) {
                    System.out.println("Model type: " + model.getType());
                    for (Symbol atom : model.getSymbols()) {
                        String tmp = atom.toString();
                        System.out.println(tmp);
                        Assert.assertTrue("Atom " + tmp + " does not exists in the result set", result.remove(tmp));
                    }
                }
            }

        } catch (ClingoException ex) {
            System.err.println(ex.getMessage());
        }

        Assert.assertTrue("Result set is not empty!", result.isEmpty());
    }
}
