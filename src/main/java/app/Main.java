package app;

import static spark.Spark.*;
import com.google.gson.Gson;
import app.store.*;
import app.model.Student;

public class Main {
    public static void main(String[] args) {
        port(8080);
        threadPool(200, 50, 30000);
        Gson gson = new Gson();

        System.out.println("=== NoSQL Lab Starting ===");
        System.out.println("Initializing databases...");

        RedisStore.init();
        HazelcastStore.init();
        MongoStore.init();

        System.out.println("All databases initialized!");
        System.out.println("Server running on http://localhost:8080");
        System.out.println();
        System.out.println("Endpoints:");
        System.out.println("  Redis:     http://localhost:8080/nosql-lab-rd/?student_no=2025000001");
        System.out.println("  Hazelcast: http://localhost:8080/nosql-lab-hz/?student_no=2025000001");
        System.out.println("  MongoDB:   http://localhost:8080/nosql-lab-mon/?student_no=2025000001");
        System.out.println();

        // Redis endpoint
        get("/nosql-lab-rd/", (req, res) -> {
            res.type("application/json");
            String studentNo = req.queryParams("student_no");
            if (studentNo == null) {
                res.status(400);
                return "{\"error\":\"student_no parameter required\"}";
            }
            Student student = RedisStore.get(studentNo);
            if (student == null) {
                res.status(404);
                return "{\"error\":\"Student not found\"}";
            }
            return gson.toJson(student);
        });

        // Hazelcast endpoint
        get("/nosql-lab-hz/", (req, res) -> {
            res.type("application/json");
            String studentNo = req.queryParams("student_no");
            if (studentNo == null) {
                res.status(400);
                return "{\"error\":\"student_no parameter required\"}";
            }
            Student student = HazelcastStore.get(studentNo);
            if (student == null) {
                res.status(404);
                return "{\"error\":\"Student not found\"}";
            }
            return gson.toJson(student);
        });

        // MongoDB endpoint
        get("/nosql-lab-mon/", (req, res) -> {
            res.type("application/json");
            String studentNo = req.queryParams("student_no");
            if (studentNo == null) {
                res.status(400);
                return "{\"error\":\"student_no parameter required\"}";
            }
            Student student = MongoStore.get(studentNo);
            if (student == null) {
                res.status(404);
                return "{\"error\":\"Student not found\"}";
            }
            return gson.toJson(student);
        });
    }
}
