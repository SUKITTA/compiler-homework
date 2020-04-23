import java.util.ArrayList;
import java.util.HashMap;

public class NFA {
    private HashMap<String, State> states;
    private String nameOfStartState;
    private ArrayList<String> nameOfAcceptStates;
    private ArrayList<String> alphabet;

    public NFA() {
        this.states = new HashMap<>();
        this.nameOfStartState = "";
        this.nameOfAcceptStates = new ArrayList<>();
        this.alphabet = new ArrayList<>();
        this.alphabet.add("ε");
    }

    public void setStartState(String nameOfStartState) {
        this.nameOfStartState = nameOfStartState;
    }
    public void addState(State state) {
        this.states.put(state.getName(), state);
    }
    public void addNameOfAcceptState(String nameOfAcceptState) {
        this.nameOfAcceptStates.add(nameOfAcceptState);
    }
    public void addAlphabet(String symbol) {
        this.alphabet.add(symbol);
    }
    public void addStateToTransitionMap(String nameOfState, String symbol, String nameOfTargetState) {
        State state = this.states.get(nameOfState);
        State targetState = this.states.get(nameOfTargetState);
        state.addStateToTransitionMap(symbol, targetState);
    }

    public State getStartState() {
        return this.states.get(this.nameOfStartState);
    }
    public ArrayList<String> getNameOfAcceptStates() {
        return nameOfAcceptStates;
    }
    public State getState(String nameOfState) {
        return this.states.get(nameOfState);
    }
    public ArrayList<String> getNameOfStates() {
        return new ArrayList<>(this.states.keySet());
    }
    public ArrayList<String> getAlphabet() {
        return this.alphabet;
    }

    public boolean isStateInList(String nameOfState) {
        return this.states.containsKey(nameOfState);
    }
    public boolean isSymbolInAlphabet(String symbol) {
        return this.alphabet.contains(symbol);
    }

    public void print() {
        System.out.println("===== NFA =====\n");
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
}
