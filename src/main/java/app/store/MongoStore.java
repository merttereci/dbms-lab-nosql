package app.store;

import com.mongodb.client.*;
import org.bson.Document;
import app.model.Student;
import com.google.gson.Gson;

public class MongoStore {
    static MongoClient client;
    static MongoCollection<Document> collection;
    static Gson gson = new Gson();

    public static void init() {
        client = MongoClients.create("mongodb://localhost:27017");
        collection = client.getDatabase("nosqllab").getCollection("students");
        collection.drop(); // Eski kayıtları temizle

        for (int i = 1; i <= 10000; i++) {
            String id = "2025" + String.format("%06d", i);
            Student s = new Student(id, "Ad Soyad " + i, "Bilgisayar");
            collection.insertOne(Document.parse(gson.toJson(s)));
        }

        // Index oluştur (performans için)
        collection.createIndex(new Document("student_no", 1));
        System.out.println("MongoDB: 10,000 students inserted.");
    }

    public static Student get(String id) {
        Document doc = collection.find(new Document("student_no", id)).first();
        return doc != null ? gson.fromJson(doc.toJson(), Student.class) : null;
    }
}