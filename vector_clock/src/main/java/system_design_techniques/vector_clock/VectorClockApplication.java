package system_design_techniques.vector_clock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class VectorClockApplication {

	public static void main(String[] args) {
		SpringApplication.run(VectorClockApplication.class, args);

		Node nodeA = new Node("A");
		Node nodeB = new Node("B");

		// cả 2 node ghi giá trị khác nhau cho cùng 1 key (conflict) -> add cả 2
		nodeA.put("user_1", "Alice");
		nodeB.put("user_1", "Bob");

		System.out.println("\n --- Trước khi đồng bộ ---");
		nodeA.print("user_1");
		nodeB.print("user_1");

		// Giả lập đồng bộ dữ liệu giữa 2 node
		nodeA.sync("user_1", nodeB);
		System.out.println("\n --- Sau khi đồng bộ ---");
		nodeA.print("user_1");
		nodeB.print("user_1");


	}

}
