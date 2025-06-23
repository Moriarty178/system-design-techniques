package system_design_techniques.quorum_consensus;

import java.util.*;
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
        int version = versionCounter.incrementAndGet();
        System.out.println("Writing '" + value + "' to W=" + W + " replicas (v" + version + ")");

        // Giả định các node gốc là 3 node đầu
        List<ReplicaNode> homeNodes = replicas.subList(0, Math.min(W, replicas.size()));
        int success = 0;

        Set<String> usedNodes = new HashSet<>(); // tránh trùng node khi chọn backup

        for (ReplicaNode homeNode : homeNodes) {
            usedNodes.add(homeNode.getName());

            if (homeNode.isAlive()) {
                homeNode.write(value, version);
                System.out.println("- Wrote to " + homeNode.getName());
                success++;
            } else {
                // Ghi nhờ vào node khác
                ReplicaNode backup = findHealthyBackup(usedNodes);
                if (backup != null) {
                    backup.writeHint(homeNode.getName(), value, version);
                    usedNodes.add(backup.getName()); // đánh dấu đã dùng
                    success++;
                }
            }

            if (success >= W) break;
        }

        if (success < W) {
            System.out.println("❌ Write failed. Only wrote to " + success + " nodes.");
        }
    }

    public ReplicaNode findHealthyBackup(Set<String> usedNodes) {
        for (ReplicaNode node : replicas) {
            if (!usedNodes.contains(node.getName()) && node.isAlive()) {
                return node;
            }
        }
        return null;
    }

    public String read(int R) {
        System.out.println("Reading from R= " + R + " replicas");

        // giả lập đọc ngẫu nhiên từ R node
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

    // kịch hoạt "Hinted Handoff" khi một node online lại
    public void handoffAll(String revivedNodeName) {
        ReplicaNode revived = null;
        for (ReplicaNode node : replicas) {
            if (node.getName().equals(revivedNodeName)) {
                revived = node;
                break;
            }
        }

        if (revived != null) {
            revived.setAlive(true); // đánh dấu online lại
            revived.receiveHandoffFromAll(replicas);//replicas.toArray(new ReplicaNode[0])); // đòi dữ liệu từ toàn bộ nodes
        }
    }
}
