package system_design_techniques.anti_entropy_n_merkle_tree;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

public class MerkleTree {
    MerkleNode root;
    List<String> keys;
    Map<String, String> data;

    public MerkleTree(Map<String, String> data) {
        this.data = data;
        this.keys = new ArrayList<>(data.keySet());
        this.root = buildTree(0, keys.size() - 1);
    }

    public MerkleNode buildTree(int l, int r) {
        if (l > r) return null;
        if (l == r) { // TH node lá
            String key = keys.get(l);
            String val = data.get(key);
            String hash = MerkleTree.hash(key + ":" + val);
            return new MerkleNode(hash);
        }

        int mid = (l + r) / 2;
        MerkleNode left = buildTree(l, mid);
        MerkleNode right = buildTree(mid + 1, r);
        return new MerkleNode(left, right);
    }

    public static String hash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(input.getBytes());
            return Base64.getEncoder().encodeToString(hash).substring(0, 8); // rút gọn
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // So sánh cây con và in ra phần khác biệt
    public void compareAndSync(MerkleNode other, MerkleNode current, String indent) {
        if (other == null && current == null) return;

        if (other == null || current == null || !other.hash.equals(current.hash)) {
            if (current.left == null && current.right == null) {
                System.out.println(indent + "❗ Different leaf -> Cần đồng bộ.");
            } else {
                System.out.println(indent + "🔄 Different branch -> đi sâu hơn:");
                compareAndSync(other != null ? other.left : null, current.left, indent + " ");
                compareAndSync(other != null ? other.right : null, current.right, indent + " ");
            }
        } else {
            System.out.println(indent + "✅ Đồng bộ tai mức này.");
        }
    }
}





























