package enigma;
import java.util.ArrayList;

/** An alphabet of encodable characters.  Provides a mapping from characters
 *  to and from indices into the alphabet.
 *  @author Jinho Shin
 */
class Alphabet {

    /** A new alphabet containing CHARS.  Character number #k has index
     *  K (numbering from 0). No character may be duplicated. */
    Alphabet(String chars) {
        _charList = new ArrayList<Character>(chars.length());
        for (int i = 0; i < chars.length(); i++) {
            if (!_charList.contains(chars.charAt(i))) {
                char c = chars.charAt(i);
                _charList.add(c);
            }
        }
    }

    /** A default alphabet of all upper-case characters. */
    Alphabet() {
        this("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    }

    /** Returns the size of the alphabet. */
    int size() {
        return _charList.size();
    }

    /** Returns true if CH is in this alphabet. */
    boolean contains(char ch) {
        return _charList.contains(ch);
    }

    /** Returns character number INDEX in the alphabet, where
     *  0 <= INDEX < size(). */
    char toChar(int index) {
        return (char) _charList.get(index);
    }

    /** Returns the index of character CH which must be in
     *  the alphabet. This is the inverse of toChar(). */
    int toInt(char ch) {
        return _charList.indexOf(ch);
    }

    /** List of characters in this Alphabet. */
    private ArrayList<Character> _charList;

}
