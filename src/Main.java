import java.io.IOException;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException {
        Scanner input = new Scanner(System.in);
        NFA nfa = new NFA();

//      Receive States.
        System.out.println(
            "Step 1: Enter your States.\n" +
            "        When all input has been entered, please type \"DONE\".\n"
        );
        while(true) {
            System.out.print("  > Enter a state: ");
            String nameOfState = input.nextLine();
            if (nameOfState.toUpperCase().equals("DONE")) {
                if (nfa.getNameOfStates().size() != 0) {
                    System.out.println("");
                    break;
                }
                printWarning("Please enter some state.", "    ");
                continue;
            } else if (nameOfState.length() == 0){
                printWarning("Please enter a state.", "    ");
                continue;
            } else if (nfa.isStateInList(nameOfState)) {
                printWarning("This state already exists.", "    ");
                continue;
            }

            nfa.addState(new State(nameOfState));
        }
        System.out.println("------------------------------------------------------\n");

//      Receive Alphabet.
        System.out.println(
            "Step 2: Enter your Alphabet.\n" +
            "        When all input has been entered, please type \"DONE\".\n"
        );
        while (true) {
            System.out.print("  > Enter a symbol: ");
            String symbol = input.nextLine();
            if (symbol.toUpperCase().equals("DONE")) {
                if (nfa.getAlphabet().size() != 1) {
                    System.out.println("");
                    break;
                }
                printWarning("Please enter some symbol.", "    ");
                continue;
            } else if (symbol.length() != 1) {
                printWarning("Please enter a symbol.", "    ");
                continue;
            } else if (nfa.isSymbolInAlphabet(symbol)) {
                printWarning("This symbol already exists.", "    ");
                continue;
            }

            nfa.addAlphabet(symbol);
        }
        System.out.println("------------------------------------------------------\n");

//      Receive Transitions of states
        System.out.println(
            "Step 3: Enter Transitions of States.\n"
        );
        for (String nameOfState : nfa.getNameOfStates()) {
            System.out.println("  - State: " + nameOfState);
            ArrayList<String> alphabet = nfa.getAlphabet();
            for ( int i = 0; i < alphabet.size(); i++) {
                try {
                    String symbol = alphabet.get(i);
                    System.out.print("    > How many states to be move when symbol is \"" + symbol + "\": ");
                    int numberOfState = Integer.parseInt(input.nextLine());
                    if (numberOfState > nfa.getNameOfStates().size()) {
                        printWarning("Please enter a number within the states size.", "      ");
                        i--;
                        continue;
                    }
                    if (numberOfState<0)
                        throw new NumberFormatException();
                    for (int j = 1; j <= numberOfState; j++) {
                        System.out.print("      > Enter state " + j + ": ");
                        String nameOfTargetState = input.nextLine();
                        if (nfa.isStateInList(nameOfTargetState)){
                            nfa.addStateToTransitionMap(nameOfState, symbol, nameOfTargetState);
                        } else {
                            printWarning("Please enter a state in list.", "        ");
                            j--;
                        }
                    }
                }catch (NumberFormatException e) {
                    printWarning("Please enter a positive integer.", "      ");
                    i--;
                }
            }
            System.out.println("");
        }
        System.out.println("\n------------------------------------------------------\n");

//      Set Start state
        System.out.println(
            "Step 4: Enter your Start State.\n"
        );
        while(true) {
            System.out.print("  > Enter a start state: ");
            String nameOfStartState = input.nextLine();
            if (nfa.isStateInList(nameOfStartState)) {
                nfa.setStartState(nameOfStartState);
                break;
            }
            printWarning("Please enter a state in list.", "    ");
        }
        System.out.println("\n------------------------------------------------------\n");

//      Set Accept states
        System.out.println(
            "Step 5: Enter your Accept States.\n"
        );
        while(true) {
            try {
                System.out.print("  > How many accept states: ");
                int numberOfAcceptState = Integer.parseInt(input.nextLine());
                if (numberOfAcceptState > nfa.getNameOfStates().size()){
                    printWarning("Please enter a number within the states size.", "    ");
                    continue;
                }
                for (int i = 1; i <= numberOfAcceptState; i++) {
                    System.out.print("      > Enter state " + i + ": ");
                    String nameOfAcceptState = input.nextLine();
                    if (nfa.isStateInList(nameOfAcceptState)){
                        nfa.addNameOfAcceptState(nameOfAcceptState);
                    } else {
                        printWarning("Please enter a state in list.", "        ");
                        i--;
                    }
                }
                break;
            } catch (NumberFormatException e) {
                printWarning("Please enter a positive integer.", "    ");
            }
        }
        System.out.println("\n------------------------------------------------------\n");

        nfa.print();

        DfaConverter dfa = new DfaConverter(nfa);
        dfa.convert();
    }

    private static void printWarning(String message, String indent) {
        String line = "";
        for (int i = 0; i < message.length()+6; i++) {
            line += "-";
        }
        System.out.println(indent + line);
        System.out.println(indent + "|  " + message + "  |");
        System.out.println(indent + line);
    }
}
