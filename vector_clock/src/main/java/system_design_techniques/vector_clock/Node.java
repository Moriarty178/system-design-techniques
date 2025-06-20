package system_design_techniques.vector_clock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Node {
    private final String nodeId;
    private final Map<String, List<VersionedValue>> store = new HashMap<>();

    public Node(String nodeId) {
        this.nodeId = nodeId;
    }

    public void put(String key, String value) {
        VectorClock vc = new VectorClock();
        List<VersionedValue> existing = store.getOrDefault(key, new ArrayList<>());
        if (!existing.isEmpty()) {
            vc = existing.get(0).clock.copy(); // lấy clock gần nhất
        }
        vc.increment(nodeId);
        VersionedValue vv = new VersionedValue(value, vc);
        store.put(key, new ArrayList<>(List.of(vv)));
        System.out.println(nodeId + " PUT: " + key + " -> " + vv);
    }

    public List<VersionedValue> get(String key) {
        return store.getOrDefault(key, new ArrayList<>());
    }

    public void sync(String key, Node other) {
        List<VersionedValue> mine = this.get(key);
        List<VersionedValue> theirs = other.get(key);

        List<VersionedValue> merged = new ArrayList<>();

        for (VersionedValue v1 : mine) {
            boolean mergedFlag = false;
            for (VersionedValue v2 : theirs) {
                int cmp = v1.clock.compare(v2.clock);
                if (cmp == 0 || cmp == 1) {
                    merged.add(v1);
                    mergedFlag = true;
                    break;
                } else if (cmp == -1) {
                    merged.add(v2);
                    mergedFlag = true;
                    break;
                } else if (cmp == 2) {
                    // conflict
                    merged.add(v1);
                    merged.add(v2);
                    mergedFlag = true;
                    break;
                }
            }
            if (!mergedFlag) merged.add(v1);
        }

        this.store.put(key, merged);
        other.store.put(key, merged);

        System.out.println("\n SYNC DONE BETWEEN " + this.nodeId + " & " + other.nodeId);
        for (VersionedValue vv : merged) {
            System.out.println("-> " + key + " = " + vv);
        }
    }

    public void print(String key) {
        System.out.println("📦 " + nodeId + " has key [" + key + "]:");
        for (VersionedValue vv : get(key)) {
            System.out.println(" ->" + vv);
        }

    }
}
