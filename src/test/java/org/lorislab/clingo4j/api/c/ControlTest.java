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
package org.lorislab.clingo4j.api.c;

import java.util.HashSet;
import java.util.Set;
import org.bridj.BridJ;
import org.bridj.Pointer;
import org.bridj.SizeT;
import org.junit.Assert;
import org.junit.Test;
import org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_control;
import org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_model;
import org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_show_type;
import org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_solve_handle;
import org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_solve_mode;

/**
 * The java version of the example: clingo/examples/c/control.c
 * 
 * @author Andrej Petras
 */
public class ControlTest {

        private static final Set<String> RESULTS = new HashSet<String>() {
        {
            add("q(10,1)");
            add("q(1,2)");
            add("q(6,3)");
            add("q(4,4)");
            add("q(2,5)");
            add("q(8,6)");
            add("q(3,7)");
            add("q(9,8)");
            add("q(7,9)");
            add("q(5,10)");
        }
    };
        
    static {
        // add the native library directory
        BridJ.addLibraryPath("src/main/clingo");        
        
        // initialize the native library
        BridJ.register(ClingoLibrary.class);
    }

    private static final String TIME_FORMAT = "%.3f";

    private static final String PROGRAM1 = "b :- not a. a :- not b.";

    private static final String PROGRAM2 = "#const n = 17. "
            + "1 { p(X); q(X) } 1 :- X = 1..n. "
            + "%:- not n+1 { p(1..n); q(1..n) }. ";

    private static final String PROGRAM3 = "#const n = 10.\n"
            + "n(1..n).\n"
            + "\n"
            + "q(X,Y) :- n(X), n(Y), not not q(X,Y).\n"
            + "\n"
            + "        c(r,X; c,Y) :- q(X,Y).\n"
            + "not not c(r,N; c,N) :- n(N).\n"
            + "\n"
            + "n(r,X,Y-1,X,Y; c,X-1,Y,X,Y; d1,X-1,Y-1,X,Y;     d2,X-1,Y+1,X,Y      ) :- n(X), n(Y).\n"
            + "c(r,N,0;       c,0,N;       d1,N-1,0; d1,0,N-1; d2,N-1,n+1; d2,0,N+1) :- n(N).\n"
            + "\n"
            + "c(C,XX,YY) :-     c(C,X,Y), n(C,X,Y,XX,YY), not q(XX,YY).\n"
            + "           :- not c(C,X,Y), n(C,X,Y,XX,YY),     q(XX,YY).\n"
            + "\n"
            + "#show q/2.";

    private static final ClingoLibrary LIB = new ClingoLibrary();

    @Test
    public void controlTest() {

        Pointer<Byte> name = Pointer.pointerToCString("base");
        Pointer<Byte> program = Pointer.pointerToCString(PROGRAM3);

        clingo_part p = new clingo_part();
        p.name(name);
        p.params(null);
        p.size(0);

        Pointer<clingo_part> parts = Pointer.allocate(clingo_part.class);
        parts.set(p);

        Pointer<Integer> major = Pointer.allocateInt();
        Pointer<Integer> minor = Pointer.allocateInt();
        Pointer<Integer> revision = Pointer.allocateInt();
        LIB.clingo_version(major, minor, revision);
        System.out.println("Clingo library version: " + major.getInt() + "." + minor.getInt() + "." + revision.getInt());
        
        // create a control object and pass command line arguments
        Pointer<Pointer<clingo_control>> control = Pointer.allocatePointer(clingo_control.class);
        if (!LIB.clingo_control_new(null, 0, null, null, 20, control)) {
            error(control, "Could not create controller");
        }

        long startTime = System.currentTimeMillis();

        // add a logic program to the base part
        if (!LIB.clingo_control_add(control.get(), name, null, 0, program)) {
            error(control, "Error add the program to controller");
        }

        // ground the base part
        if (!LIB.clingo_control_ground(control.get(), parts, 1, null, null)) {
            error(control, "Error ground the program");
        }

        if (!solve(control)) {
            error(control, "Error solving the program");
        }

        float endTime = (System.currentTimeMillis() - startTime) / 1000f;
        System.out.println("Time:" + String.format(TIME_FORMAT, endTime) + "s");
        
        LIB.clingo_control_free(control.get());
    }

    private static boolean solve(Pointer<Pointer<clingo_control>> ctrl) {
        Pointer<Integer> result = Pointer.allocateInt();
        Pointer<Pointer<clingo_solve_handle>> handle = Pointer.allocatePointer(clingo_solve_handle.class);
        Pointer<Pointer<clingo_model>> model = Pointer.allocatePointer(clingo_model.class);

        // get a solve handle        
        if (!LIB.clingo_control_solve(ctrl.get(), (int) clingo_solve_mode.clingo_solve_mode_yield.value, null, 0, null, null, handle)) {
            error(ctrl, "Error execute control solve");
        }

        // loop over all models
        boolean run = true;
        while (run) {

            if (!LIB.clingo_solve_handle_resume(handle.get())) {
                error(ctrl, "Error solve handle resume");
            }
            if (!LIB.clingo_solve_handle_model(handle.get(), model)) {
                error(ctrl, "Error solve handle model");
            }

            // print the model
            if (model.get() != null) {
                printModel(ctrl, model);
            } else {
                // stop if there are no more models
                run = false;
            }
        }

        // close the solve handle
        if (!LIB.clingo_solve_handle_get(handle.get(), result)) {
            error(ctrl, "Error solve handle get");
        }

        return LIB.clingo_solve_handle_close(handle.get());
    }

    private static void printModel(Pointer<Pointer<clingo_control>> ctrl, Pointer<Pointer<clingo_model>> model) {
        
        Set<String> result = new HashSet<>(RESULTS);
        
        int show = (int) (clingo_show_type.clingo_show_type_shown.value);

        Pointer<SizeT> atoms_n = Pointer.allocateSizeT();

        // determine the number of (shown) symbols in the model                
        if (!LIB.clingo_model_symbols_size(model.get(), show, atoms_n)) {
            error(ctrl, "Error create symbol size");
        }

        // allocate required memory to hold all the symbols
        Pointer<Long> atoms = Pointer.allocateLongs(atoms_n.getLong());

        // retrieve the symbols in the model
        if (!LIB.clingo_model_symbols(model.get(), show, atoms, atoms_n.getLong())) {
            error(ctrl, "Error create symbol symbols");
        }

        System.out.println("Model: ");

        for (int i = 0; i < atoms_n.getLong(); i++) {

            Long atom = atoms.get(i);

            // determine size of the string representation of the next symbol in the model  
            Pointer<SizeT> size = Pointer.allocateSizeT();
            LIB.clingo_symbol_to_string_size(atom, size);

            // allocate required memory to hold the symbol's string
            Pointer<Byte> string = Pointer.allocateBytes(size.getLong());

            // retrieve the symbol's string          
            if (!LIB.clingo_symbol_to_string(atom, string, size.getLong())) {
                error(ctrl, "Error symbol to string");
            }
            String st = string.getCString();
            
            System.out.print(st);
            Assert.assertTrue("Atom " + st + " does not exists in the result set", result.remove(st));
            System.out.print(" -> ");

            Pointer<Pointer<Long>> arguments = Pointer.allocatePointer(Long.class);
            Pointer<SizeT> arguments_size = Pointer.allocateSizeT();
            LIB.clingo_symbol_arguments(atom, arguments, arguments_size);

            Pointer<Pointer<Byte>> name = Pointer.allocatePointer(Byte.class);
            LIB.clingo_symbol_name(atom, name);
            System.out.print("name=" + name.get().getCString());

            System.out.print(",args=[");
            Pointer<Integer> t = Pointer.allocateInt();
            for (int a = 0; a < arguments_size.getLong(); a++) {
                if (a > 0) {
                    System.out.print(",");
                }
                LIB.clingo_symbol_number(arguments.get().get(a), t);
                System.out.print(t.get());
            }
            System.out.print("]");

            System.out.println();
        }
        
        Assert.assertTrue("Result set is not empty!", result.isEmpty());
    }

    private static void error(Pointer<Pointer<clingo_control>> ctrl, String details) {
        System.err.println(details);
        Pointer<Byte> pmsg = LIB.clingo_error_message();
        String msg = pmsg.getCString();

        LIB.clingo_control_free(ctrl.get());

        System.err.println("ERROR: " + msg);
        System.exit(LIB.clingo_error_code());
    }

}
