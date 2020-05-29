package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;

import static enigma.TestUtils.*;

/**
 * The suite of all JUnit tests for the Permutation class. For the purposes of
 * this lab (in order to test) this is an abstract class, but in proj1, it will
 * be a concrete class. If you want to copy your tests for proj1, you can make
 * this class concrete by removing the 4 abstract keywords and implementing the
 * 3 abstract methods.
 *
 *  @author Jinho Shin
 */
public class PermutationTest {

    /**
     * For this lab, you must use this to get a new Permutation,
     * the equivalent to:
     * new Permutation(cycles, alphabet)
     * @return a Permutation with cycles as its cycles and alphabet as
     * its alphabet
     * @see Permutation for description of the Permutation conctructor
     */
    Permutation getNewPermutation(String cycles, Alphabet alphabet) {
        return new Permutation(cycles, alphabet);
    }

    /**
     * For this lab, you must use this to get a new Alphabet,
     * the equivalent to:
     * new Alphabet(chars)
     * @return an Alphabet with chars as its characters
     * @see Alphabet for description of the Alphabet constructor
     */
    Alphabet getNewAlphabet(String chars) {
        return new Alphabet(chars);
    }

    /**
     * For this lab, you must use this to get a new Alphabet,
     * the equivalent to:
     * new Alphabet()
     * @return a default Alphabet with characters ABCD...Z
     * @see Alphabet for description of the Alphabet constructor
     */
    Alphabet getNewAlphabet() {
        return new Alphabet();
    };

    /** Testing time limit. */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /** Check that PERM has an ALPHABET whose size is that of
     *  FROMALPHA and TOALPHA and that maps each character of
     *  FROMALPHA to the corresponding character of FROMALPHA, and
     *  vice-versa. TESTID is used in error messages. */
    private void checkPerm(String testId,
                           String fromAlpha, String toAlpha,
                           Permutation perm, Alphabet alpha) {
        int N = fromAlpha.length();
        assertEquals(testId + " (wrong length)", N, perm.size());
        for (int i = 0; i < N; i += 1) {
            char c = fromAlpha.charAt(i), e = toAlpha.charAt(i);
            assertEquals(msg(testId, "wrong translation of '%c'", c),
                    e, perm.permute(c));
            assertEquals(msg(testId, "wrong inverse of '%c'", e),
                    c, perm.invert(e));
            int ci = alpha.toInt(c), ei = alpha.toInt(e);
            assertEquals(msg(testId, "wrong translation of %d", ci),
                    ei, perm.permute(ci));
            assertEquals(msg(testId, "wrong inverse of %d", ei),
                    ci, perm.invert(ei));
        }
    }

    /* ***** TESTS ***** */

    @Test
    public void checkIdTransform() {
        Alphabet alpha = getNewAlphabet();
        Permutation perm = getNewPermutation("", alpha);
        checkPerm("identity", UPPER_STRING, UPPER_STRING, perm, alpha);
    }

    @Test(expected = EnigmaException.class)
    public void testNotInAlphabet() {
        Permutation p = getNewPermutation("(BACD)", getNewAlphabet("ABCD"));
        p.invert('F');
    }

    @Test
    public void testPermuteChar() {
        Permutation p = getNewPermutation("(AELTPHQXRU) (BKNW) (CMOY) (DFG) "
                + "(IV) (JZ) (S)", getNewAlphabet("ABCDEFGHIJKLM"
                + "NOPQRSTUVWXYZ"));
        assertEquals('E', p.permute('A'));
        assertEquals('K', p.permute('B'));
        assertEquals('M', p.permute('C'));
        assertEquals('F', p.permute('D'));
        assertEquals('S', p.permute('S'));
    }

    @Test
    public void testPermuteChar2() {
        Permutation p = getNewPermutation("(BACD)", getNewAlphabet("ABCD"));
        assertEquals('A', p.permute('B'));
        assertEquals('C', p.permute('A'));
        assertEquals('D', p.permute('C'));
        assertEquals('B', p.permute('D'));
    }


    @Test
    public void testInvertChar() {
        Permutation p = getNewPermutation("(BACD)", getNewAlphabet("ABCD"));
        assertEquals('B', p.invert('A'));
        assertEquals('A', p.invert('C'));
        assertEquals('D', p.invert('B'));
        assertEquals('C', p.invert('D'));
    }

    @Test
    public void testInvertChar2() {
        Permutation p = getNewPermutation("(AELTPHQXRU) (BKNW) (CMOY) (DFG) "
                + "(IV) (JZ) (S)", getNewAlphabet("ABCDEFGHIJKL"
                + "MNOPQRSTUVWXYZ"));
        assertEquals('A', p.invert('E'));
        assertEquals('B', p.invert('K'));
        assertEquals('C', p.invert('M'));
        assertEquals('D', p.invert('F'));
        assertEquals('S', p.invert('S'));
    }


    @Test
    public void testSize() {
        Permutation p = getNewPermutation("(BACD)", getNewAlphabet("ABCD"));
        assertEquals(4, p.size());
    }


    @Test
    public void testPermuteInt() {
        Permutation p = getNewPermutation("(BACD)", getNewAlphabet("ABCD"));
        assertEquals(0, p.permute(1));
        assertEquals(2, p.permute(0));
        assertEquals(3, p.permute(2));
        assertEquals(1, p.permute(3));
    }

    @Test
    public void testInvertInt() {
        Permutation p = getNewPermutation("(BACD)", getNewAlphabet("ABCD"));
        assertEquals(1, p.invert(0));
        assertEquals(0, p.invert(2));
        assertEquals(2, p.invert(3));
        assertEquals(3, p.invert(1));
    }

    @Test
    public void testAlphabet() {
        Permutation p = getNewPermutation("(BACD)", getNewAlphabet("ABCD"));
        assertTrue(p.alphabet().contains('A'));
        assertTrue(p.alphabet().contains('B'));
        assertTrue(p.alphabet().contains('C'));
        assertTrue(p.alphabet().contains('D'));
        assertFalse(p.alphabet().contains('E'));
    }

    @Test
    public void testDerangement() {
        Permutation p1 = getNewPermutation("", getNewAlphabet("ABCD"));
        assertEquals(false, p1.derangement());
        Permutation p2 = getNewPermutation("(AELTPHQXRU) (BKNW) (CMOY) (DFG)"
                + "(IV) (JZ) (S)", getNewAlphabet("ABCDEFGHIJK"
                + "LMNOPQRSTUVWXYZ"));
        assertEquals(false, p2.derangement());
    }
}
