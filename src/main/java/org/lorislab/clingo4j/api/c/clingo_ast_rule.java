package org.lorislab.clingo4j.api.c;
import org.bridj.Pointer;
import org.bridj.StructObject;
import org.bridj.ann.Field;
import org.bridj.ann.Library;
import org.bridj.ann.Name;
import org.bridj.ann.Ptr;
/**
 * <i>native declaration : src/main/clingo/lib/c/clingo.h</i><br>
 * This file was autogenerated by <a href="http://jnaerator.googlecode.com/">JNAerator</a>,<br>
 * a tool written by <a href="http://ochafik.com/">Olivier Chafik</a> that <a href="http://code.google.com/p/jnaerator/wiki/CreditsAndLicense">uses a few opensource projects.</a>.<br>
 * For help, please visit <a href="http://nativelibs4java.googlecode.com/">NativeLibs4Java</a> or <a href="http://bridj.googlecode.com/">BridJ</a> .
 */
@Name("clingo_ast_rule") 
@Library("clingo") 
public class clingo_ast_rule extends StructObject {
	/** C type : clingo_ast_head_literal_t */
	@Field(0) 
	public clingo_ast_head_literal head() {
		return this.io.getNativeObjectField(this, 0);
	}
	/** C type : clingo_ast_head_literal_t */
	@Field(0) 
	public clingo_ast_rule head(clingo_ast_head_literal head) {
		this.io.setNativeObjectField(this, 0, head);
		return this;
	}
	/** C type : const clingo_ast_body_literal_t* */
	@Field(1) 
	public Pointer<clingo_ast_body_literal > body() {
		return this.io.getPointerField(this, 1);
	}
	/** C type : const clingo_ast_body_literal_t* */
	@Field(1) 
	public clingo_ast_rule body(Pointer<clingo_ast_body_literal > body) {
		this.io.setPointerField(this, 1, body);
		return this;
	}
	@Ptr 
	@Field(2) 
	public long size() {
		return this.io.getSizeTField(this, 2);
	}
	@Ptr 
	@Field(2) 
	public clingo_ast_rule size(long size) {
		this.io.setSizeTField(this, 2, size);
		return this;
	}
	public clingo_ast_rule() {
		super();
	}
	public clingo_ast_rule(Pointer pointer) {
		super(pointer);
	}
}
