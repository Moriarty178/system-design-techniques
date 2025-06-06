package system_design_techniques.consistent_hashing;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.SortedMap;
import java.util.TreeMap;

public class ConsistentHashing {
    private final SortedMap<Integer, String> circle = new TreeMap<>();
    /*
    * để tránh tình trạng phân bổ không đồng đều key giữa các server với nhau, ta dùng node ảo để tăng độ phụ sóng cho mỗi server qua đó giảm thiểu tình trạng phân bổ "key" không đồng đều
    * */
    private final int VIRTUAL_NODES = 100;


    public void addServer(String server) {
        for (int i = 0; i < VIRTUAL_NODES; i++) {
            int hash = hash(server + "#" + i);
            circle.put(hash, server);
        }
    }

    public void removeServer(String server) {
        for (int i = 0; i < VIRTUAL_NODES; i++) {
            int hash = hash(server + "#" + i);
            circle.remove(hash);
        }
    }

    /*
    * Hàm ánh xạ "key" vào server tương ứng trên Circle: server gần nó nhất theo chiều kim đồng hồ
    * */
    public String getServer(String key) {
        if (circle.isEmpty()) return null;

        int hash = hash(key);// Hash key ra vị trí trên vòng tròn

        if (!circle.containsKey(hash)) {
            SortedMap<Integer, String> tailMap = circle.tailMap(hash); // tailMap: trả ra các cặp (k, v) của server mà k >= hash(k)
            hash = tailMap.isEmpty() ? circle.firstKey() : tailMap.firstKey(); // Nếu không có thì lấy server đầu tiên của circle, nếu có thì lấy server đầu tiên trong danh sách trả về tailMap <=> server đầu tiên nó gặp theo chiều kim đồng hồ.
        }

        return circle.get(hash);
    }

    /*
    * Việc phân bổ các key cho các server phụ thuộc vào hàm hash( ) rất nhiều, và một phần tương đối vào virtual node, không gian vòng
    * */
    private int hash(String key) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(key.getBytes());

            return ((digest[0] & 0xFF) << 24) | ((digest[1] & 0xFF) << 16)
                 | ((digest[2] & 0xFF) << 8) | (digest[3] & 0xFF);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        //return key.hashCode() & 0x7fffffff; // hash code dương
    }
}
