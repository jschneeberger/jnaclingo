package org.lorislab.clingo4j.api.c;
import org.bridj.Pointer;
import org.bridj.StructObject;
import org.bridj.ann.Field;
import org.bridj.ann.Library;
import org.bridj.ann.Name;
import org.bridj.ann.Ptr;
/**
 * function<br>
 * <i>native declaration : src/main/clingo/lib/c/clingo.h:2134</i><br>
 * This file was autogenerated by <a href="http://jnaerator.googlecode.com/">JNAerator</a>,<br>
 * a tool written by <a href="http://ochafik.com/">Olivier Chafik</a> that <a href="http://code.google.com/p/jnaerator/wiki/CreditsAndLicense">uses a few opensource projects.</a>.<br>
 * For help, please visit <a href="http://nativelibs4java.googlecode.com/">NativeLibs4Java</a> or <a href="http://bridj.googlecode.com/">BridJ</a> .
 */
@Name("clingo_ast_function") 
@Library("clingo") 
public class clingo_ast_function extends StructObject {
	/** C type : const char* */
	@Field(0) 
	public Pointer<Byte > name() {
		return this.io.getPointerField(this, 0);
	}
	/** C type : const char* */
	@Field(0) 
	public clingo_ast_function name(Pointer<Byte > name) {
		this.io.setPointerField(this, 0, name);
		return this;
	}
	/** C type : clingo_ast_term_t* */
	@Field(1) 
	public Pointer<clingo_ast_term > arguments() {
		return this.io.getPointerField(this, 1);
	}
	/** C type : clingo_ast_term_t* */
	@Field(1) 
	public clingo_ast_function arguments(Pointer<clingo_ast_term > arguments) {
		this.io.setPointerField(this, 1, arguments);
		return this;
	}
	@Ptr 
	@Field(2) 
	public long size() {
		return this.io.getSizeTField(this, 2);
	}
	@Ptr 
	@Field(2) 
	public clingo_ast_function size(long size) {
		this.io.setSizeTField(this, 2, size);
		return this;
	}
	public clingo_ast_function() {
		super();
	}
	public clingo_ast_function(Pointer pointer) {
		super(pointer);
	}
}
