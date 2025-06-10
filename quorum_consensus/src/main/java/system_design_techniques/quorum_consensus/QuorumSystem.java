package system_design_techniques.quorum_consensus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class QuorumSystem {
    private final AtomicInteger versionCounter = new AtomicInteger();
    List<ReplicaNode> replicas;

    public QuorumSystem(int n) {
        replicas = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            replicas.add(new ReplicaNode("Node" + i));
        }
    }

    public void write(String value, int W) {
//        long timestamp  = System.currentTimeMillis(); // simulate version
        int version = versionCounter.incrementAndGet();
        System.out.println("Writing '" + value + "' to W=" + W + " replicas");
        // shuffle replicas: giả lập ghi ngẫu nhiên vào W nodes
        List<ReplicaNode> shuffleReplicas = new ArrayList<>(replicas);
        Collections.shuffle(shuffleReplicas);
        for (int i = 0; i < W; i++) {
            shuffleReplicas.get(i).write(value, version);
            System.out.println(" - Wrote to " + shuffleReplicas.get(i).getName());
        }
    }

    public String read(int R) {
        System.out.println("Reading from R=" + R + " replicas");

        // giả lập đọc ngẫu nhiên từ R nodes
        List<ReplicaNode> shuffleReplicas = new ArrayList<>(replicas);
        Collections.shuffle(shuffleReplicas);
        List<ReplicaNode> readNodes = shuffleReplicas.subList(0, R);

        ReplicaNode latest = readNodes.get(0);

        for (ReplicaNode node : readNodes) {
            System.out.println(" - " + node.getName() + ": " + node.read());
            if (node.getVersion() > latest.getVersion()) {
                latest = node;
            }
        }

        return latest.read();
    }
}
