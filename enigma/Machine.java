package enigma;

import java.util.Collection;



import static enigma.EnigmaException.*;

/** Class that represents a complete enigma machine.
 *  @author Jinho Shin
 */
class Machine {

    /** A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     *  and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     *  available rotors. */
    Machine(Alphabet alpha, int numRotors, int pawls,
            Collection<Rotor> allRotors) {
        _alphabet = alpha;
        _numRotors = numRotors;
        _numPawls = pawls;
        _allRotors = allRotors;
        _myRotors = new Rotor[_numRotors];
    }

    /** Return the number of rotor slots I have. */
    int numRotors() {
        return _numRotors;
    }

    /** Return the number pawls (and thus rotating rotors) I have. */
    int numPawls() {
        return _numPawls;
    }

    /** Set my rotor slots to the rotors named ROTORS from my set of
     *  available rotors (ROTORS[0] names the reflector).
     *  Initially, all rotors are set at their 0 setting. */
    void insertRotors(String[] rotors) {
        int pawlCount = 0;
        int refCount = 0;
        for (int i = 0; i < _numRotors; i++) {
            if (refCount > 1) {
                throw error("Can't have more than one reflector");
            }
            if (pawlCount > _numPawls) {
                throw error("Too many pawls");
            }
            for (Rotor R: _allRotors) {
                String s = R.name();
                if (s.equals(rotors[i])) {
                    Class c = R.getClass();
                    if (c.equals(Reflector.class)) {
                        refCount += 1;
                        if (i != 0) {
                            throw error("wrong position for reflector");
                        }
                    } else if (c.equals(FixedRotor.class)) {
                        if (i == 0 || i > (_numRotors - _numPawls - 1)) {
                            throw error("wrong position for fixed rotor");
                        }
                    } else if (c.equals(MovingRotor.class)) {
                        pawlCount += 1;
                        if (i < (_numRotors -  _numPawls) || i >= _numRotors) {
                            throw error("wrong position for moving rotor");
                        }
                    }
                    _myRotors[i] = R;

                }
            }
        }
    }

    /** Set my rotors according to SETTING, which must be a string of
     *  numRotors()-1 characters in my alphabet. The first letter refers
     *  to the leftmost rotor setting (not counting the reflector).  */
    void setRotors(String setting) {
        for (int i = 1; i < _numRotors; i++) {
            _myRotors[i].set(setting.charAt(i - 1));
        }
    }

    /** Set the plugboard to PLUGBOARD. */
    void setPlugboard(Permutation plugboard) {
        _plugboard = plugboard;
    }

    /** Returns the result of converting the input character C (as an
     *  index in the range 0..alphabet size - 1), after first advancing
     *  the machine. */
    int convert(int c) {
        for (int i = 1; i < _numRotors - 1; i++) {
            boolean prevM = _myRotors[i - 1].getClass() == MovingRotor.class;
            Rotor cur = _myRotors[i];
            Rotor next = _myRotors[i + 1];
            if (next.atNotch() || cur.atNotch() && prevM) {
                cur.advance();
            }
        }
        _myRotors[_numRotors - 1].advance();
        Alphabet plugA = _plugboard.alphabet();
        if (_plugboard != null && plugA.contains(_alphabet.toChar(c))) {
            c = _alphabet.toInt(_plugboard.permute(_alphabet.toChar(c)));
        }
        for (int i = _numRotors - 1; i > 0; i--) {
            c = _myRotors[i].convertForward(c);
        }
        for (int j = 0; j < _numRotors; j++) {
            c = _myRotors[j].convertBackward(c);
        }
        if (_plugboard != null && plugA.contains(_alphabet.toChar(c))) {
            c = _alphabet.toInt(_plugboard.permute(_alphabet.toChar(c)));
        }
        return c;
    }

    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly. */
    String convert(String msg) {
        String output = "";
        for (int i = 0; i < msg.length(); i++) {
            if (msg.charAt(i) != ' ') {
                int c = _alphabet.toInt(msg.charAt(i));
                output += _alphabet.toChar(convert(c));
            }
        }
        return output;
    }

    /** @param rotor to check @return if 'rotor' is in allRotors. */
    boolean hasRotor(String rotor) {
        for (Rotor R: _allRotors) {
            if (R.name() == rotor) {
                return true;
            }
        }
        return false;
    }

    /** Common alphabet of my rotors. */
    private final Alphabet _alphabet;

    /** Number of my rotors. */
    private int _numRotors;

    /** Number of my pawls. */
    private int _numPawls;

    /** Collection of Rotors. */
    private Collection<Rotor> _allRotors;

    /** List of Rotors. */
    private Rotor[] _myRotors;

    /** Plugboard Permutation. */
    private Permutation _plugboard;

}
