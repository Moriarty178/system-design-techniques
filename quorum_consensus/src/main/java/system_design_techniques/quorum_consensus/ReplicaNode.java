package system_design_techniques.quorum_consensus;

public class ReplicaNode {
    String name;
    String value = "";
    int version = 0;

    public ReplicaNode(String name) {
        this.name = name;
    }

    public boolean write(String value, int version) {
        try {
            this.value = value;
            this.version = version;
            return true;
        }
        catch (Exception e) {
            return false;
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
}
