package enigma;

import static enigma.EnigmaException.*;

/** Class that represents a rotating rotor in the enigma machine.
 *  @author Jinho Shin
 */
class MovingRotor extends Rotor {

    /** A rotor named NAME whose permutation in its default setting is
     *  PERM, and whose notches are at the positions indicated in NOTCHES.
     *  The Rotor is initially in its 0 setting (first character of its
     *  alphabet).
     */
    MovingRotor(String name, Permutation perm, String notches) {
        super(name, perm);
        _notches = notches;
    }

    @Override
    boolean atNotch() {
        Permutation p = permutation();
        Alphabet a = p.alphabet();
        for (int i = 0; i < _notches.length(); i++) {
            int n = a.toInt(_notches.charAt(i));
            if (setting() == n) {
                return true;
            }
        }
        return false;
    }

    @Override
    void advance() {
        set(setting() + 1);
    }

    /** Notches for the rotor. */
    private String _notches;

}
