package org.potassco.api;

import org.potassco.jna.BaseClingo;

import com.sun.jna.Pointer;

public class SymbolicAtoms {

	/**
	 * Get the number of different atoms occurring in a logic program.
	 * 
	 * @return the number of atoms
	 */
	public long symbolicAtomsSize(Pointer atoms) {
		return BaseClingo.symbolicAtomsSize(atoms);
	}

	/**
	 * Get a forward iterator to the beginning of the sequence of all symbolic atoms
	 * optionally restricted to a given signature.
	 * 
	 * @param atoms     the target
	 * @param signature optional signature
	 * @return the resulting iterator
	 */
	public Pointer symbolicAtomsBegin(Pointer atoms, Pointer signature) {
		return BaseClingo.symbolicAtomsBegin(atoms, signature);
	}

	/**
	 * Iterator pointing to the end of the sequence of symbolic atoms.
	 * 
	 * @param atoms the target
	 * @return the resulting iterator
	 */
	public Pointer symbolicAtomsEnd(Pointer atoms) {
		return BaseClingo.symbolicAtomsEnd(atoms);
	}

	/**
	 * Find a symbolic atom given its symbolic representation.
	 * 
	 * @param atoms  the target
	 * @param symbol the symbol to lookup
	 * @return iterator pointing to the symbolic atom or to the end
	 */
	public Pointer symbolicAtomsFind(Pointer atoms, long symbol) {
		return BaseClingo.symbolicAtomsFind(atoms, symbol);
	}

	/**
	 * Check if two iterators point to the same element (or end of the sequence).
	 * 
	 * @param atoms     the target
	 * @param iteratorA the first iterator
	 * @param iteratorB the second iterator
	 * @return whether the two iterators are equal
	 */
	public boolean symbolicAtomsIteratorIsEqualTo(Pointer atoms, Pointer iteratorA, Pointer iteratorB) {
		return BaseClingo.symbolicAtomsIteratorIsEqualTo(atoms, iteratorA, iteratorB);
	}

	/**
	 * Get the symbolic representation of an atom.
	 * 
	 * @param atoms    the target
	 * @param iterator iterator to the atom
	 * @return the resulting symbol
	 */
	public long symbolicAtomsSymbol(Pointer atoms, Pointer iterator) {
		return BaseClingo.symbolicAtomsSymbol(atoms, iterator);
	}

	/**
	 * Check whether an atom is a fact.
	 * 
	 * @note This does not determine if an atom is a cautious consequence. The
	 *       grounding or solving component's simplifications can only detect this
	 *       in some cases.
	 * @param atoms    the target
	 * @param iterator iterator to the atom
	 * @return fact whether the atom is a fact
	 */
	public boolean symbolicAtomsIsFact(Pointer atoms, Pointer iterator) {
		return BaseClingo.symbolicAtomsIsFact(atoms, iterator);
	}

	/**
	 * Check whether an atom is external.
	 * 
	 * An atom is external if it has been defined using an external directive and
	 * has not been released or defined by a rule.
	 * 
	 * @param atoms    the target
	 * @param iterator iterator to the atom
	 * @return whether the atom is a external
	 */
	public long symbolicAtomsIsExternal(Pointer atoms, Pointer iterator) {
		return BaseClingo.symbolicAtomsIsExternal(atoms, iterator);
	}

	/**
	 * Returns the (numeric) aspif literal corresponding to the given symbolic atom.
	 * 
	 * Such a literal can be mapped to a solver literal (see the \ref Propagator
	 * module) or be used in rules in aspif format (see the \ref ProgramBuilder
	 * module).
	 * 
	 * @param atoms    the target
	 * @param iterator iterator to the atom
	 * @return the associated literal
	 */
	public Pointer symbolicAtomsLiteral(Pointer atoms, Pointer iterator) {
		return BaseClingo.symbolicAtomsLiteral(atoms, iterator);
	}

	/**
	 * Get the number of different predicate signatures used in the program.
	 * 
	 * @param atoms the target
	 * @return the number of signatures
	 */
	public long symbolicAtomsSignaturesSize(Pointer atoms) {
		return BaseClingo.symbolicAtomsSignaturesSize(atoms);
	}

	/**
	 * Get the predicate signatures occurring in a logic program.
	 * 
	 * @param atoms the target
	 * @param size  the number of signatures
	 * @return the resulting signatures
	 */
	public Pointer symbolicAtomsSignatures(Pointer atoms, long size) {
		return BaseClingo.symbolicAtomsSignatures(atoms, size);
	}

	/**
	 * Get an iterator to the next element in the sequence of symbolic atoms.
	 * 
	 * @param atoms    the target
	 * @param iterator the current iterator
	 * @return the succeeding iterator
	 */
	public Pointer symbolicAtomsNext(Pointer atoms, Pointer iterator) {
		return BaseClingo.symbolicAtomsNext(atoms, iterator);
	}

	/**
	 * Check whether the given iterator points to some element with the sequence of
	 * symbolic atoms or to the end of the sequence.
	 * 
	 * @param atoms    the target
	 * @param iterator the iterator
	 * @return whether the iterator points to some element within the sequence
	 */
	public byte symbolicAtomsIsValid(Pointer atoms, Pointer iterator) {
		return BaseClingo.symbolicAtomsIsValid(atoms, iterator);
	}

}