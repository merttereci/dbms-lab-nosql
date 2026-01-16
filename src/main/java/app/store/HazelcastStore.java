package app.store;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import app.model.Student;

public class HazelcastStore {
    static HazelcastInstance hz;
    static IMap<String, Student> map;

    public static void init() {
        ClientConfig config = new ClientConfig();
        config.getNetworkConfig().addAddress("localhost:5701");

        hz = HazelcastClient.newHazelcastClient(config);
        map = hz.getMap("students");

        for (int i = 1; i <= 10000; i++) {
            String id = "2025" + String.format("%06d", i);
            Student s = new Student(id, "Ad Soyad " + i, "Bilgisayar");
            map.put(id, s);
        }

        System.out.println("Hazelcast: 10,000 students inserted.");
    }

    public static Student get(String id) {
        return map.get(id);
    }
}