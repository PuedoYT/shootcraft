package gg.funkraft.mongodb;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class SpigotDatabaseManager {



    // Exemple de nom d'utilisateur et mot de passe
    static String username = "root";
    static String password = "";

    // Encodage URL
    static String encodedUsername;

    static {
        try {
            encodedUsername = URLEncoder.encode(username, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    static String encodedPassword;

    static {
        try {
            encodedPassword = URLEncoder.encode(password, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    // Construction de la chaîne de connexion
    static String connectionString = "mongodb://localhost:27017/admin";


    public static MongoClient mongoClient = MongoClients.create(connectionString);
    public static MongoDatabase getDatabase = mongoClient.getDatabase("FunKraft");

    public SpigotDatabaseManager() throws UnsupportedEncodingException {
    }

    public static void createCollection(MongoDatabase database, String collectionName) {
        database.createCollection(collectionName);
        System.out.println("Collection created successfully");
    }

    public static boolean checkKeyValueExists(MongoCollection<Document> collection, String key, String value) {
        FindIterable<Document> iterable = collection.find(Filters.eq(key, value));
        return iterable.first() != null;
    }

    public static boolean checkKeyExists(MongoCollection<Document> collection, String key) {
        Bson filter = Filters.exists(key, true);
        long count = collection.countDocuments(filter);
        return count > 0; // Retourne true si au moins un document avec la clé spécifiée existe
    }


    public static void insertDocument(MongoCollection<Document> collection, Map<String, Object> data) {
        Document document = new Document(data);
        collection.insertOne(document);
        System.out.println("Document inserted successfully");
    }


    public static void addField(MongoCollection<Document> collection, String key, String value) {

        Bson updateOperation = Updates.set(key, value); // Remplacer 25 par l'âge désiré ou approprié
        collection.updateMany(new Document(), updateOperation);
    }

}
