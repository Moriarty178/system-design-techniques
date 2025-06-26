package system_design_techniques.quorum_consensus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReplicaNode {
    private final String name;
    private String value = "";
    private int version = 0;
    private boolean alive = true;

    private final Map<String, HintedData> hintedStorage = new HashMap<>(); // lưu thông tin mà node hiện tại ghi hộ các node gốc đang offline

    public ReplicaNode(String name) {
        this.name = name;
    }

    public boolean write(String value, int version) {
        if (!alive)  return false;
        if (version >= this.version) {
            this.value = value;
            this.version = version;
            return true;
        }
        return false;
    }

    // ghi nhờ dữ liệu hộ node, khi node gốc offline
    public void writeHint(String intendedNodeName, String value, int version) {
        if (!alive) return;
        System.out.printf("- [Hinted] %s stores value for %s\n", name, intendedNodeName);
        hintedStorage.put(intendedNodeName, new HintedData(value, version));
    }

    // Đẩy lại dữ liệu đã ghi hộ cho node gốc, khi node gốc online lại
    public void handoff(ReplicaNode target) {
        if (!alive) return;
        if (hintedStorage.containsKey(target.getName())) {
            HintedData data = hintedStorage.get(target.getName());
            boolean success = target.write(data.value, data.version);
            if (success) {
                System.out.printf(" => [Handoff] %s -> %s (v%d: %s)\n", this.name, target.getName(), data.version, data.value);
                hintedStorage.remove(target.getName());
            } else {
                System.out.printf(" => [Handoff Failed] %s -> %s\n", this.name, target.getName());
            }
        }
    }

    // khi 1 node online lại -> thực hiện "đòi" lại dữ liệu mà các node khác ghi hộ khi nó off
    public void receiveHandoffFromAll(List<ReplicaNode> allNodes) {
        for (ReplicaNode node: allNodes) {
            node.handoff(this); // các node call handoff với target = this - node vừa online lại và đòi dữ liệu
        }
    }

    public String read() {
        return value + " (v" + version + ")";
    }

    public int getVersion() {
        return version;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean value) {
        this.alive = value;
        if (alive) {
            System.out.printf("[Recovery] %s is back online\n", name);
        } else {
            System.out.printf("[Failure] %s is now offline\n", name);
        }
    }

    private static class HintedData {
        String value;
        int version;

        public HintedData(String value, int version) {
            this.value = value;
            this.version = version;
        }
    }

}

