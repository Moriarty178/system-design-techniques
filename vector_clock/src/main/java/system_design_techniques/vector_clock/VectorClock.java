package system_design_techniques.vector_clock;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class VectorClock {
    private final Map<String, Integer> clock = new HashMap<>();

    public void increment(String nodeId) {
        clock.put(nodeId, clock.getOrDefault(nodeId, 0) + 1);
    }

    public void merge(VectorClock other) {
        for (Map.Entry<String, Integer> entry : other.clock.entrySet()) {
            clock.put(entry.getKey(), Math.max(clock.getOrDefault(entry.getKey(), 0), entry.getValue()));
        }
    }

    public int compare(VectorClock other) {
        boolean greater = false, less = false;
        Set<String> keys = new HashSet<>(clock.keySet());
        keys.addAll(other.clock.keySet());

        for (String key : keys) {
            int thisVal = clock.getOrDefault(key, 0);
            int otherVal = other.clock.getOrDefault(key, 0);
            if (thisVal > otherVal) greater = true;
            else if (thisVal < otherVal) less = true;
        }

        if (greater && !less) return 1; // all versions of key other > versions of key present
        if (!greater && less) return -1; // all vers other > all vers present
        if (!greater && !less) return 0; // all vers other = present
        return 2; // có key "node" mà ver other > present và có cả key"node" mà version other < present => greater = true & less = true
    }

    public VectorClock copy() {
        VectorClock copy = new VectorClock();
        copy.clock.putAll(this.clock);
        return copy;
    }

    public String toString() {
        return clock.toString();
    }
}
