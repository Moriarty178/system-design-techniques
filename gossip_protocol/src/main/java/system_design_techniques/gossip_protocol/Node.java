package system_design_techniques.gossip_protocol;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Node implements Runnable{
    private String name;
    private Set<String> liveNodes; // lưu danh sách nút còn sống
    private Map<String, Integer> suspicionCounter; // lưu danh sách nút bị nghi ngờ chết và số lần nghi ngờ
    private List<Node> cluster; // danh tập nodes để random -> gossip
    private Random random = new Random();
    private volatile boolean alive = true;

    public Node(String name) {
        this.name = name;
        this.liveNodes = ConcurrentHashMap.newKeySet();
        this.suspicionCounter = new ConcurrentHashMap<>();
    }

    public void setCluster(List<Node> cluster) {
        this.cluster = cluster;
        for (Node n : cluster) {
            if (!n.getName().equals(this.name)) {
                liveNodes.add(n.getName());
                suspicionCounter.put(n.getName(), 0);
            }
        }
    }

    public void setAlive(boolean value) {
        this.alive = value;
    }

    public String getName() {
        return name;
    }

    // giả lập phản hồi
    public boolean receiveGossip(String from) {
        if (!alive) {
            throw new RuntimeException(String.format("Node %s is Down!\n", this.getName()));
        }
        System.out.printf("[%s] Received gossip from %s\n", name, from);
        return true;
    }

    private Node getRandomNode() {
        List<Node> others = new ArrayList<>(cluster);
        others.removeIf(n -> n.getName().equals(this.name));
        return others.get(random.nextInt(others.size()));
    }

    @Override
    public void run() {
        while(alive) {
            try {
                Node target = getRandomNode();
                boolean responded = false;
                try {
                    responded = target.receiveGossip(this.name);
                } catch (Exception e) {
                    responded = false;
                }

                if (!responded) {
                    int count = suspicionCounter.merge(target.getName(), 1, Integer::sum); // tăng đếm nghi ngờ chết nếu ko phàn hồi
                    System.out.printf("%s Count = %s on Suspicion Counter %s\n", target.name, count, this.name);
                    if (count >= 3) {
                        liveNodes.remove(target.getName()); // remove khỏi danh sách node sống
                        System.out.printf("[%s] Detected node failure: %s \n", name, target.getName());
                    }
                } else {
                    suspicionCounter.put(target.getName(), 0); // reset nếu có phàn hồis
                }

                Thread.sleep(1000 + random.nextInt(1000));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}






















