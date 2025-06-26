package system_design_techniques.delta_sync;

import java.security.MessageDigest;
import java.util.Base64;

public class Block {
    int index;
    String data;
    String hash;

    public Block(int index, String data) {
        this.index = index;
        this.data = data;
        this.hash = sha256(data);
    }

    static String sha256(String data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(data.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return String.format("[Block %d] \"%s\" -> hash=%s", index, data, hash);
    }
}
