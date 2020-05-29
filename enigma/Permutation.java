package enigma;

import static enigma.EnigmaException.*;

/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author Jinho Shin
 */
class Permutation {

    /** Set this Permutation to that specified by CYCLES, a string in the
     *  form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     *  is interpreted as a permutation in cycle notation.  Characters in the
     *  alphabet that are not included in any cycle map to themselves.
     *  Whitespace is ignored. */
    Permutation(String cycles, Alphabet alphabet) {
        _alphabet = alphabet;
        _cycles = cycles;
    }

    /** Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     *  c0c1...cm. */
    private void addCycle(String cycle) {
        _cycles += cycle;
    }

    /** Return the value of P modulo the size of this permutation. */
    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r += size();
        }
        return r;
    }

    /** Returns the size of the alphabet I permute. */
    int size() {
        return alphabet().size();
    }

    /** Return the result of applying this permutation to P modulo the
     *  alphabet size. */
    int permute(int p) {
        if (!_alphabet.contains(_alphabet.toChar(p))) {
            throw new EnigmaException("Character not in cycle");
        } else if (_cycles.equals("") || _cycles.equals(" ")) {
            return p;
        } else {
            char head = _cycles.charAt(0);
            for (int i = 0; i < _cycles.length() - 1; i++) {
                char cur = _cycles.charAt(i);
                char next = _cycles.charAt(i + 1);
                if (cur == '(') {
                    head = next;
                }
                if (_alphabet.toInt(cur) == p) {
                    if (next == ')') {
                        return _alphabet.toInt(head);
                    }
                    return _alphabet.toInt(next);
                }
            }
        }
        return p;
    }

    /** Return the result of applying the inverse of this permutation
     *  to  C modulo the alphabet size. */
    int invert(int c) {
        if (!_alphabet.contains(_alphabet.toChar(c))) {
            throw new EnigmaException("Character not in cycle");
        } else if (_cycles.equals("") || _cycles.equals(" ")) {
            return c;
        } else {
            char tail = _cycles.charAt(_cycles.length() - 1);
            for (int i = _cycles.length() - 1; i > 0; i--) {
                char cur = _cycles.charAt(i);
                char next = _cycles.charAt(i - 1);
                if (cur == ')') {
                    tail = next;
                }
                if (_alphabet.toInt(cur) == c) {
                    if (next == '(') {
                        return _alphabet.toInt(tail);
                    }
                    return _alphabet.toInt(next);
                }
            }
        }
        return c;
    }

    /** Return the result of applying this permutation to the index of P
     *  in ALPHABET, and converting the result to a character of ALPHABET. */
    char permute(char p) {
        if (!_alphabet.contains(p)) {
            throw new EnigmaException("Character not in cycle");
        } else if (_cycles.equals("") || _cycles.equals(" ")) {
            return p;
        } else {
            char head = _cycles.charAt(0);
            for (int i = 0; i < _cycles.length() - 1; i++) {
                char cur = _cycles.charAt(i);
                char next = _cycles.charAt(i + 1);
                if (cur == '(') {
                    head = next;
                }
                if (cur == p) {
                    if (next == ')') {
                        return head;
                    }
                    return next;
                }
            }
        }
        return p;
    }

    /** Return the result of applying the inverse of this permutation to C. */
    char invert(char c) {
        if (!_alphabet.contains(c)) {
            throw new EnigmaException("Character not in cycle");
        } else if (_cycles.equals("") || _cycles.equals(" ")) {
            return c;
        } else {
            char tail = _cycles.charAt(_cycles.length() - 1);
            for (int i = _cycles.length() - 1; i > 0; i--) {
                char cur = _cycles.charAt(i);
                char next = _cycles.charAt(i - 1);
                if (cur == ')') {
                    tail = next;
                }
                if (cur == c) {
                    if (next == '(') {
                        return tail;
                    }
                    return next;
                }
            }
        }
        return c;
    }

    /** Return the alphabet used to initialize this Permutation. */
    Alphabet alphabet() {
        return _alphabet;
    }

    /** Return true iff this permutation is a derangement (i.e., a
     *  permutation for which no value maps to itself). */
    boolean derangement() {
        for (int i = 0; i < _alphabet.size(); i++) {
            char c = _alphabet.toChar(i);
            char p = permute(c);
            if (c == p) {
                return false;
            }
        }
        return true;
    }

    /** Alphabet of this permutation. */
    private Alphabet _alphabet;

    /** Cycles for the permutation. */
    private String _cycles;
}
