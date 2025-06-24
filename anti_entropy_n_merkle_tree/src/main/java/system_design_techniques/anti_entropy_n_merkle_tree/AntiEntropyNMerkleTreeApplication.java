package system_design_techniques.anti_entropy_n_merkle_tree;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Map;
import java.util.TreeMap;

@SpringBootApplication
public class AntiEntropyNMerkleTreeApplication {

	public static void main(String[] args) {
		SpringApplication.run(AntiEntropyNMerkleTreeApplication.class, args);

		// Giả lập dữ liệu 2 node
		Map<String, String> nodeAData = new TreeMap<>();
		Map<String, String> nodeBData = new TreeMap<>();

		nodeAData.put("k1", "v1");
		nodeAData.put("k2", "v2");
		nodeAData.put("k3", "v3");
		nodeAData.put("k4", "v4");

		nodeBData.put("k1", "v1");
		nodeBData.put("k2", "v2");
		nodeBData.put("k3", "v3_CHANGED"); // khác
		nodeBData.put("k4", "v4");

		MerkleTree treeA = new MerkleTree(nodeAData);
		MerkleTree treeB = new MerkleTree(nodeBData);

		System.out.println("==> Root Hash A: " + treeA.root.hash);
		System.out.println("==> Root Hash B: " + treeB.root.hash);

		System.out.println("\n=== So sánh & đồng bộ ===");
		treeA.compareAndSync(treeB.root, treeA.root, "");
	}

}











































