package org.potassco.clingo;

import static org.junit.Assert.*;

import org.junit.Test;
import org.potassco.jclingo.NativeClingo;
import org.potassco.jclingo.types.PartT;
import org.potassco.jclingo.types.SizeT;
import org.potassco.jclingo.types.SizeTByReference;
import org.potassco.jclingo.types.SolveEventCallbackT;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;

public class ClingoTest {

	@Test
	public void test() {
        IntByReference major = new IntByReference();
        IntByReference minor = new IntByReference();
        IntByReference patch = new IntByReference();
		NativeClingo nativeLib = NativeClingo.INSTANCE;
		nativeLib.clingo_version(major, minor, patch);
		assertEquals(5, major.getValue());
		assertEquals(5, minor.getValue());
		assertEquals(0, patch.getValue());
	}

	@Test
	public void test2() {
        PointerByReference ctl = new PointerByReference();
        NativeClingo.INSTANCE.clingo_control_new(null, new SizeT(0), null, null, 20, ctl);
        // add a program
        NativeClingo.INSTANCE.clingo_control_add(ctl.getValue(), "base", null, new SizeT(0), "a. b.");
        // ground it
        PartT[] parts = new PartT [1];
        parts[0] = new PartT();
        parts[0].name = "base";
        parts[0].params = null;
        parts[0].size = new SizeT(0);
        NativeClingo.INSTANCE.clingo_control_ground(ctl.getValue(), parts, new SizeT(1), null, null);
        // solve it
        SolveEventCallbackT cb = new SolveEventCallbackT() {
            public boolean call(int type, Pointer event, Pointer goon) {
                if (type == 0) {
                    SizeTByReference num = new SizeTByReference();
                    NativeClingo.INSTANCE.clingo_model_symbols_size(event, 2, num);
                    System.out.printf("model: %d\n", num.getValue());
                    long[] symbols = new long [(int)num.getValue()];
                    NativeClingo.INSTANCE.clingo_model_symbols(event, 2, symbols, new SizeT(num.getValue()));
                    System.out.print("ANSWER:");
                    for (int i = 0; i < num.getValue(); ++i) {
                        SizeTByReference len = new SizeTByReference();
                        NativeClingo.INSTANCE.clingo_symbol_to_string_size(symbols[i], len);
                        byte[] str = new byte[(int)len.getValue()];
                        NativeClingo.INSTANCE.clingo_symbol_to_string(symbols[i], str, new SizeT(len.getValue()));
                        System.out.format(" %s", new String(str));
                    }
                    System.out.println();

                }
                return true;
            }
        };
        PointerByReference hnd = new PointerByReference();
        NativeClingo.INSTANCE.clingo_control_solve(ctl.getValue(), 0, null, new SizeT(0), cb, null, hnd);
        IntByReference res = new IntByReference();
        NativeClingo.INSTANCE.clingo_solve_handle_get(hnd.getValue(), res);
        NativeClingo.INSTANCE.clingo_solve_handle_close(hnd.getValue());
        // clean up
        NativeClingo.INSTANCE.clingo_control_free(ctl.getValue());
	}
}
