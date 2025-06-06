package system_design_techniques.consistent_hashing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ConsistentHashingApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConsistentHashingApplication.class, args);

		ConsistentHashing ch = new ConsistentHashing();
		ch.addServer("Server-A");
		ch.addServer("Server-B");
		ch.addServer("Server-C");

		String[] keys = {"user_1", "user_2", "order_55", "product_99"};
		for (String key : keys) {
			String server = ch.getServer(key);
			System.out.println("Key [" + key + "] is assigned to " + server);
		}
	}

}
