package system_design_techniques.quorum_consensus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class QuorumConsensusApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuorumConsensusApplication.class, args);

//		QuorumSystem qs = new QuorumSystem(7); // N = 7
//		qs.write("X = 100", 4);
//		qs.write("X = 199", 4);
//		qs.write("X = 2025", 4);
//		System.out.println("Final read: " + qs.read(4));

		QuorumSystem quorum = new QuorumSystem(5);

		// Giả lập: Node2 chết
		quorum.replicas.get(2).setAlive(false);

		// Ghi với W=3 (Sloppy Quorum)
		quorum.write("User123 = John", 3);
		System.out.println("Node 2 before handoff value = " + quorum.replicas.get(2).getValue());

		// Node2 sống lại, bắt đầu quá trình handoff
		quorum.handoffAll("Node2"); // set Alive = true + đòi dữ liệu từ toàn bộ nodes

		System.out.println("Node 2 after handoff value = " + quorum.replicas.get(2).getValue());

		// Đọc dữ liệu từ R=2 node
		System.out.println("Final read result: " + quorum.read(5));
	}

}
