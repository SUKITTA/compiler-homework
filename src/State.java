import java.util.ArrayList;
import java.util.HashMap;

public class State {
    private String nameOfState;
    private HashMap<String, ArrayList<State>> transitionMap;

    public State(String nameOfState) {
        this.nameOfState = nameOfState;
        this.transitionMap = new HashMap<>();
    }

    public void addStateToTransitionMap(String symbol, State state) {
        if (!this.transitionMap.containsKey(symbol)){
            ArrayList<State> targetStateList = new ArrayList<>();
            targetStateList.add(state);
            this.transitionMap.put(symbol, targetStateList);
        } else {
            this.transitionMap.get(symbol).add(state);
        }
    }

    public String getName() {
        return nameOfState;
    }
    public ArrayList<State> getTransitionBy(String symbol) {
        return transitionMap.get(symbol);
    }

    public String toString() {
        String result = this.nameOfState;
        for ( String symbol : transitionMap.keySet() ) {
            result += "\t" + symbol + "->{";
            ArrayList<State> states = transitionMap.get(symbol);
            for ( int i = 0; i < states.size(); i++) {
                result += (i==0 ? "" : ",") + states.get(i).getName();
            }
            result += "}\n";
        }
        return result;
    }
}
