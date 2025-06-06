package system_design_techniques.consistent_hashing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ConsistentHashingApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConsistentHashingApplication.class, args);

		ConsistentHashing ch = new ConsistentHashing();
		ch.addServer("ServerA");
		ch.addServer("ServerB");

		String[] keys = {"user_1", "user_2", "order_55", "product_99"};
		for (String key : keys) {
			ch.putKey(key);
			System.out.println("Key '" + key + "' is assigned to " + ch.getServer(key));
		}

		System.out.println("Danh sách keys lúc đầu trước khi add/remove ServerC:");
		ch.getAllKeys();

		// Thêm serverC và gọi remap để ánh xạ lại nếu có thay đổi
		ch.addServer("ServerC");
		ch.remapKeys();
		System.out.println("Danh sách keys sau khi add ServerC:");
		ch.getAllKeys();

		// Remove ServerB và remap
		ch.removeServer("ServerA");
		ch.removeServer("ServerC");
		ch.remapKeys();
		System.out.println("Danh sách keys sau khi remove ServerA & ServerC:");
		ch.getAllKeys();

	}

}
