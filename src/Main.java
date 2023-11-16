import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IllegalAccessException, IOException, ClassNotFoundException {
        X x = new X();
        System.out.printf("%-15s%s%n", "Saved object:", x);
        writeDataToFile(x);
        x = new X();
        System.out.printf("%-15s%s%n", "New object:", x);
        readDataFromFile(x);
        System.out.printf("%-15s%s%n", "Loaded vals:", x);
    }

    private static void readDataFromFile(Object obj) throws IOException, ClassNotFoundException, IllegalAccessException {
        FileInputStream fileInputStream
                = new FileInputStream("saved_fields.dat");
        ObjectInputStream objectInputStream
                = new ObjectInputStream(fileInputStream);

        Map<String, String> loadedFields = (Map<String, String>) objectInputStream.readObject();
        objectInputStream.close();

        Field[] fields = obj.getClass().getDeclaredFields();
        if (!loadedFields.isEmpty()) {
            for (Field field : fields) {
                if (loadedFields.containsKey(field.getName())) {
                    int mods = field.getModifiers();
                    if (!Modifier.isPublic(mods)) field.setAccessible(true);
                    field.set(obj, loadedFields.get(field.getName()));
                }
            }
        }
    }

    private static void writeDataToFile(Object obj) throws IllegalAccessException, IOException {
        Field[] fields = obj.getClass().getDeclaredFields();
        Map<String, Object> fieldsToSave = new HashMap<>();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Save.class)) {
                int mods = field.getModifiers();
                if (!Modifier.isPublic(mods)) field.setAccessible(true);
                fieldsToSave.put(field.getName(), field.get(obj));
            }
        }

        if (!fieldsToSave.isEmpty()) {
            FileOutputStream fileOutputStream
                    = new FileOutputStream("saved_fields.dat");
            ObjectOutputStream objectOutputStream
                    = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(fieldsToSave);
            objectOutputStream.flush();
            objectOutputStream.close();
        }
    }
}
