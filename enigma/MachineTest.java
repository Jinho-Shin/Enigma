package enigma;

import org.junit.Test;
import java.util.Collection;
import java.util.ArrayList;

public class MachineTest {

    Permutation getNewPermutation(String cycles, Alphabet alphabet) {
        return new Permutation(cycles, alphabet);
    }

    Alphabet getNewAlphabet(String chars) {
        return new Alphabet(chars);
    }

    @Test
    public void test1() {
        Alphabet a = getNewAlphabet("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        Permutation p1 = getNewPermutation("(AELTPHQXRU) (BKNW) "
                + "(CMOY) (DFG) (IV) (JZ) (S)", a);
        Permutation p2 = getNewPermutation("(FIXVYOMW) (CDKLHUP) "
                + "(ESZ) (BJ) (GR) (NT) (A) (Q)", a);
        Permutation p3 = getNewPermutation("(ABDHPEJT) "
                + "(CFLVMZOYQIRWUKXSG) (N)", a);
        Permutation pBeta = getNewPermutation("(ALBEVFCYODJWUGNMQTZSKPR) "
                + "(HIX)", a);
        Permutation pB = getNewPermutation("(AE) (BN) (CK) (DQ) (FU) "
                + "(GY) (HW) (IJ) (LO) (MP) (RX) (SZ) (TV)", a);
        Rotor r1 = new MovingRotor("I", p1, "Q");
        Rotor r2 = new MovingRotor("II", p2, "E");
        Rotor r3 = new MovingRotor("III", p3, "V");
        Rotor r4 = new FixedRotor("Beta", pBeta);
        Rotor r5 = new Reflector("B", pB);
        Collection<Rotor> rotors = new ArrayList<Rotor>();
        rotors.add(r5);
        rotors.add(r4);
        rotors.add(r1);
        rotors.add(r2);
        rotors.add(r3);
        Machine m1 = new Machine(a, 5, 3, rotors);
        String[] rs = new String[5];
        rs[0] = "B";
        rs[1] = "Beta";
        rs[2] = "I";
        rs[3] = "II";
        rs[4] = "III";
        m1.insertRotors(rs);
        String input = "HELLO WORLD";
        String output = m1.convert(input);
        System.out.println(output);
    }
}
