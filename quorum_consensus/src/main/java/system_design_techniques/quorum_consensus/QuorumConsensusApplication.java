package system_design_techniques.quorum_consensus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class QuorumConsensusApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuorumConsensusApplication.class, args);

		QuorumSystem qs = new QuorumSystem(7); // N = 7
		qs.write("X = 100", 4);
		qs.write("X = 199", 4);
		qs.write("X = 2025", 4);
		System.out.println("Final read: " + qs.read(4));
	}

}
