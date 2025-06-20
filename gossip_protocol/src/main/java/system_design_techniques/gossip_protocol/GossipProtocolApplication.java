package system_design_techniques.gossip_protocol;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootApplication
public class GossipProtocolApplication {

	public static void main(String[] args) {
		SpringApplication.run(GossipProtocolApplication.class, args);

		List<Node> nodes = new ArrayList<>();
		for (char c = 'A'; c <= 'E'; c++) {
			nodes.add(new Node(String.valueOf(c)));
		}

		for (Node node : nodes) {
			node.setCluster(nodes);
		}

		ExecutorService executor = Executors.newFixedThreadPool(nodes.size());
		for (Node node : nodes) {
			executor.submit(node);
		}

		for (Node node : nodes) {
			if (node.getName().equals("D")) {
				node.setAlive(false);
				break;
			}
		}

		// giả sử node D "chết" sau 5s
//		new Timer().schedule(new TimerTask() {
//			@Override
//			public void run() {
//				System.out.println("\n==> Simulating failure of node D\n");
//				for (Node node : nodes) {
//					if (node.getName().equals("D")) {
//						node.setAlive(false);
//						break;
//					}
//				}
//			}
//		}, 5000);
	}

}
