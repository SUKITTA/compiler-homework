import java.util.*;

public class DfaConverter {
    private HashMap<String, ArrayList<String>> comparator;
    private HashMap<String, State> states;
    private char charCount, charMax;
    private String nameOfStartState;
    private ArrayList<String> nameOfAcceptStates;
    private ArrayList<String> alphabet;
    private NFA nfa;

    public DfaConverter(NFA nfa) {
        this.charCount = 'A';
        this.nameOfStartState = 'A'+"";
        this.charMax = 'A';

        this.alphabet = nfa.getAlphabet();
        this.alphabet.remove(alphabet.indexOf("ε"));

        this.comparator = new HashMap<>();
        this.states = new HashMap<>();

        this.nfa = nfa;

        ArrayList<String> nameOfStates =  new ArrayList<>();
        epsilonClosure(nfa.getStartState(),nameOfStates);
        createDfaState(nameOfStates);

        this.nameOfStartState = "A";
        this.nameOfAcceptStates = new ArrayList<>();
    }

    public void convert() {
        while (charCount < charMax) {
            for (String symbol : this.alphabet){
                ArrayList<String> statesFromTran = new ArrayList<>();
                moveBy(symbol, states.get(charCount+""), statesFromTran);
                if (statesFromTran.isEmpty()) {
                    this.states.get(charCount+"").addStateToTransitionMap(symbol, createEmptyState());
                }else{
                    ArrayList<String> nameOfStates = new ArrayList<>();
                    for (String state : statesFromTran) {
                        epsilonClosure(nfa.getState(state), nameOfStates);
                    }
                    this.states.get(charCount+"").addStateToTransitionMap(symbol, createDfaState(nameOfStates));
                }
            }
            charCount++;
        }
        System.out.println("===== DFA =====\n");
        System.out.println(this.comparator.toString()
                .replaceAll("[{}]", "")
                .replaceAll("\\[", "{")
                .replaceAll("]", "}")
                .replaceAll("}, ", "\\},\n")
                .replaceAll("=", " = ")
                + "\n"
        );
        for (State state : this.states.values()){
            System.out.println(state.toString());
        }
        System.out.println("Start State: " + this.nameOfStartState);
        System.out.print("Accept State: {");
        for (int i = 0; i < this.nameOfAcceptStates.size(); i++){
            System.out.print((i==0 ? "" : ",") +  this.nameOfAcceptStates.get(i));
        }
        System.out.println("}\n");
    }

    private void epsilonClosure(State state, ArrayList<String> nameOfStates) {
        if (!nameOfStates.contains(state.getName())){
            nameOfStates.add(state.getName());
        }
        ArrayList<State> epsilonStates = state.getTransitionBy("ε");
        if (epsilonStates!=null){
            for (State s : epsilonStates) {
                epsilonClosure(s, nameOfStates);
            }
        }
    }

    private void moveBy(String symbol, State dfaState, ArrayList<String> nameOfStates) {
        for (String nameOfState : this.comparator.get(dfaState.getName())){
            ArrayList<State> tranStates = nfa.getState(nameOfState).getTransitionBy(symbol);
            if (tranStates != null){
                for (State s : tranStates){
                    if (!nameOfStates.contains(s.getName()))
                        nameOfStates.add(s.getName());
                }
            }
        }
    }

    private State createDfaState(ArrayList<String> nameOfNfaStates) {
        if (isInComparator(nameOfNfaStates)){
            for (String nameOfDfaState : this.comparator.keySet()){
                if (equalArrayLists(this.comparator.get(nameOfDfaState), nameOfNfaStates)){
                    return this.states.get(nameOfDfaState);
                }
            }
            return null;
        }
        this.comparator.put(charMax+"", nameOfNfaStates);
        State tmpState = new State(charMax+"");
        this.states.put(charMax+"", tmpState);
        for (String acceptState : nfa.getNameOfAcceptStates()){
            if (nameOfNfaStates.contains(acceptState)) {
                this.nameOfAcceptStates.add(charMax + "");
                break;
            }
        }
        charMax++;
        return tmpState;
    }
    private State createEmptyState() {
        this.comparator.put("∅", null);
        State emptyState = new State("∅");
        for (String symbol : this.alphabet) {
            emptyState.addStateToTransitionMap(symbol, emptyState);
        }
        this.states.put("∅", emptyState);
        return emptyState;
    }

    private boolean isInComparator(ArrayList<String> nameOfStates) {
        for (ArrayList<String> nameOfStatesInList : this.comparator.values()){
            if (equalArrayLists( nameOfStates, nameOfStatesInList)){
                return true;
            }
        }
        return false;
    }
    private boolean equalArrayLists(ArrayList<String> one, ArrayList<String> two){
        if (one == null && two == null){
            return true;
        }

        if((one == null && two != null) ||
            one != null && two == null ||
            one.size() != two.size()){
            return false;
        }

        Collections.sort(one);
        Collections.sort(two);
        return one.equals(two);
    }
}
