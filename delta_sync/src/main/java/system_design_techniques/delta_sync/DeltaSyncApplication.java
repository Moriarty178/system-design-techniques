package system_design_techniques.delta_sync;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SpringBootApplication
public class DeltaSyncApplication {

	public static void main(String[] args) {
		SpringApplication.run(DeltaSyncApplication.class, args);

		String originalFile = "A quick brown for jumps over the lazy dog duck cat tiger lion cao";
		String modifiedFile = "A quick black for jumps over the lazy dog";

		int blockSize = 8; // độ dài mỗi block ký tự

		System.out.println("==> Gốc:");
		List<Block> originalBlocks = splitToBlocks(originalFile, blockSize);
		for (Block b : originalBlocks) System.out.println(b);

		System.out.println("\n==> Sau khi sửa:");
		List<Block> modifiedBlocks = splitToBlocks(modifiedFile, blockSize);
		for (Block b : modifiedBlocks) System.out.println(b);

		System.out.println("\n==> Delta cần đồng bộ:");
		Map<String, List<Block>> mapComputeDelta = computeDelta(originalBlocks, modifiedBlocks);

		for (int i = 0; i < mapComputeDelta.get("delta").size(); i++) {
			String oldData = mapComputeDelta.get("originalData").get(i) == null ? "" : mapComputeDelta.get("originalData").get(i).data;
			String newData = mapComputeDelta.get("delta").get(i) == null ? "" : mapComputeDelta.get("delta").get(i).data;

			System.out.printf("\n🟧\u001B[31m\u001B[1m [old data]:%s \u001B[0m\n ", oldData);
			System.out.printf("\t🟩\u001B[34m\u001B[1m\u001B[3m [new data]: %s \u001B[0m", newData);
		}
	}

	// Hàm chia chuỗi thành block có độ dài blockSize - trong thực tế như google drive thì đó là file với blcok được chia khác đi
	static List<Block> splitToBlocks(String content, int blockSize) {
		List<Block> blocks = new ArrayList<>();
		for (int i = 0; i < content.length(); i += blockSize) {
			int end = Math.min(i + blockSize, content.length());
			String data = content.substring(i, end);
			blocks.add(new Block(i / blockSize, data));
		}
		return blocks;
	}

	// So sánh chuỗi block trước và sau khi sủa của file => tìm ra phần khác nhau
	static Map<String, List<Block>> computeDelta(List<Block> oldBlocks, List<Block> newBlocks) {
		List<Block> originalData = new ArrayList<>();
		List<Block> delta = new ArrayList<>();
		int len = Math.max(oldBlocks.size(), newBlocks.size());

		for (int i = 0; i < len; i++) {
			Block oldB = i < oldBlocks.size() ? oldBlocks.get(i) : null;
			Block newB = i <  newBlocks.size() ? newBlocks.get(i) : null;

            if (oldB == null || newB == null || !oldB.hash.equals(newB.hash)) {
				delta.add(newB);
				originalData.add(oldB);
			}
		}

		return Map.of(
				"originalData", originalData,
				"delta", delta
		);
	}

}
