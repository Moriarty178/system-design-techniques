package system_design_techniques.anti_entropy_n_merkle_tree;

public class MerkleNode {
    String hash;
    MerkleNode left;
    MerkleNode right;

    public MerkleNode(String hash) {
        this.hash = hash;
    }

    public MerkleNode(MerkleNode left, MerkleNode right) {
        this.left = left;
        this.right = right;
        this.hash = MerkleTree.hash((left != null ? left.hash : "") + (right != null ? right.hash : ""));
    }
}
