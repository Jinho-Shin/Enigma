package enigma;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Collection;
import java.util.ArrayList;

import java.util.NoSuchElementException;
import java.util.Scanner;

import static enigma.EnigmaException.*;

/** Enigma simulator.
 *  @author Jinho Shin
 */
public final class Main {

    /** Process a sequence of encryptions and decryptions, as
     *  specified by ARGS, where 1 <= ARGS.length <= 3.
     *  ARGS[0] is the name of a configuration file.
     *  ARGS[1] is optional; when present, it names an input file
     *  containing messages.  Otherwise, input comes from the standard
     *  input.  ARGS[2] is optional; when present, it names an output
     *  file for processed messages.  Otherwise, output goes to the
     *  standard output. Exits normally if there are no errors in the input;
     *  otherwise with code 1. */
    public static void main(String... args) {
        try {
            new Main(args).process();
            return;
        } catch (EnigmaException excp) {
            System.err.printf("Error: %s%n", excp.getMessage());
        }
        System.exit(1);
    }

    /** Check ARGS and open the necessary files (see comment on main). */
    Main(String[] args) {
        if (args.length < 1 || args.length > 3) {
            throw error("Only 1, 2, or 3 command-line arguments allowed");
        }

        _config = getInput(args[0]);

        if (args.length > 1) {
            _input = getInput(args[1]);
        } else {
            _input = new Scanner(System.in);
        }

        if (args.length > 2) {
            _output = getOutput(args[2]);
        } else {
            _output = System.out;
        }
    }

    /** Return a Scanner reading from the file named NAME. */
    private Scanner getInput(String name) {
        try {
            return new Scanner(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Return a PrintStream writing to the file named NAME. */
    private PrintStream getOutput(String name) {
        try {
            return new PrintStream(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Configure an Enigma machine from the contents of configuration
     *  file _config and apply it to the messages in _input, sending the
     *  results to _output. */
    private void process() {
        Machine M = readConfig();
        if (!_input.hasNext("[*]")) {
            throw error("need a setting first");
        }
        while (_input.hasNextLine()) {
            String s = _input.nextLine();
            if (s.contains("*")) {
                setUp(M, s);
            } else {
                String line = M.convert(s);
                printMessageLine(line);
            }
        }

    }


    /** Return an Enigma machine configured from the contents of configuration
     *  file _config. */
    private Machine readConfig() {
        try {
            if (_config.hasNext("\\*|\\(|\\)")) {
                throw error("wrong format for Alphabets.");
            }
            _alphabet = new Alphabet(_config.next());
            int numRotors = _config.nextInt();
            int pawls = _config.nextInt();
            if (pawls >= numRotors || pawls < 0) {
                throw error("inappropriate numbers");
            }
            Collection<Rotor> rotors = new ArrayList<Rotor>();
            while (_config.hasNext()) {
                rotors.add(readRotor());
            }
            return new Machine(_alphabet, numRotors, pawls, rotors);
        } catch (NoSuchElementException excp) {
            throw error("configuration file truncated");
        }
    }

    /** Return a rotor, reading its description from _config. */
    private Rotor readRotor() {
        try {
            String notches = "", cycles = "";
            char type = 0;
            if (_config.hasNext("\\(|\\)")) {
                throw error("wrong format for name.");
            }
            String name = _config.next();
            String info = _config.next();
            type = info.charAt(0);
            if (info.length() > 1) {
                if (type != 'M') {
                    throw error("can't have notches on non-moving");
                }
                notches = info.substring(1);
            }
            while (_config.hasNext("\\(.*\\)")) {
                cycles += _config.next();
            }
            Permutation perm = new Permutation(cycles, _alphabet);
            if (type == 'M') {
                return new MovingRotor(name, perm, notches);
            } else if (type == 'N') {
                return new FixedRotor(name, perm);
            } else if (type == 'R') {
                return new Reflector(name, perm);
            } else {
                throw error("wrong type of rotor");
            }
        } catch (NoSuchElementException excp) {
            throw error("bad rotor description");
        }
    }

    /** Set M according to the specification given on SETTINGS,
     *  which must have the format specified in the assignment. */
    private void setUp(Machine M, String settings) {
        Scanner line = new Scanner(settings);
        if (!line.next().equals("*")) {
            throw error("must start with an asterisk");
        }
        String[] rotors = new String[M.numRotors()];
        for (int i = 0; i < M.numRotors(); i++) {
            String r = line.next();
            if (M.hasRotor(r)) {
                throw error("rotor not in the machine");
            }
            rotors[i] = r;
        }
        M.insertRotors(rotors);
        if (!line.hasNext()) {
            throw error("need rotors settings");
        }
        String s = line.next();
        M.setRotors(s);
        String pCycle = "";
        while (line.hasNext("\\(.*\\)")) {
            String n = line.next();
            pCycle += n;
        }
        if (line.hasNext()) {
            throw error("Wrong setup format.");
        }
        String pString = "";
        String[] pList = pCycle.split("\\(|\\)");
        for (String p: pList) {
            pString += p;
        }
        Alphabet pAlphabet = new Alphabet(pString);
        Permutation plug = new Permutation(pCycle, pAlphabet);
        M.setPlugboard(plug);

    }

    /** Print MSG in groups of five (except that the last group may
     *  have fewer letters). */
    private void printMessageLine(String msg) {
        String output = "";
        int i = 0;
        while (i < msg.length()) {
            String s = String.valueOf(msg.charAt(i));
            output += msg.charAt(i);
            if ((i + 1) % 5 == 0) {
                output += " ";
            }
            i += 1;
        }
        if (_output != null) {
            _output.println(output);
        } else {
            System.out.println(output);
        }
    }

    /** Alphabet used in this machine. */
    private Alphabet _alphabet;

    /** Source of input messages. */
    private Scanner _input;

    /** Source of machine configuration. */
    private Scanner _config;

    /** File for encoded/decoded messages. */
    private PrintStream _output;
}
