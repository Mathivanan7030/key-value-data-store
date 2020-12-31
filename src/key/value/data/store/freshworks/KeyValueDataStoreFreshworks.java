
package key.value.data.store.freshworks;


import org.json.simple.JSONObject;

import java.io.*;
import static java.lang.System.exit;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;
public class KeyValueDataStoreFreshworks {

    
    static Scanner sc = new Scanner(System.in);
    private static Map<String, Object> map = new HashMap<>();
    private static int count = 0;
    
    public static void main(String[] args) throws IOException {
     
         String dir = fileCreation();

        while (true) {
            int choice = 0;
            do {
                try {
                    System.out.println("Select the required operations you want in this file:\n" +
                            "\t1.Press 1 for CREATE Operation\n" +
                            "\t2.Press 2 for READ Operation\n" +
                            "\t3.Press 3 for DELETE Operation\n" +
                            "\t4.press 4 for Exit\n");
                    choice = sc.nextInt();
                } catch (InputMismatchException e) {
                    System.out.println("Please enter correct input...");
                }
                sc.nextLine();
            } while (choice <= 0);
            switch (choice) {
                case 1:
                    create(dir);
                    break;
                case 2:
                    read();
                    break;
                case 3:
                    delete(dir);
                    break;
                case 4:
                    sc.close();
                    exit(0);
                default:
                    System.out.println("Please enter details correctly...\n");
            }
        
    }
    }
    public synchronized static String fileCreation() {

        String directoryName = null;
        boolean success;
        System.out.println("Enter path of directory to create file in format like this (drive name://folder name) :");
        String dir = sc.next();
        File directory = new File(dir);

        if (directory.exists()) {
            System.out.println("Directory already exists ...File will be stored in 'D://Freshworks' directory...\n");
            dir = "D:\\Freshworks";
            directory = new File(dir);
            success = directory.mkdir();
            if (success) {
                System.out.printf("Successfully created new directory : %s%n ", dir);
            }
        } else {
            System.out.println("Directory not exists, creating now...");
            success = directory.mkdir();
            if (success) {
                System.out.printf("Successfully created new directory : %s%n\n", dir);
            } else {
                System.out.printf("Failed to create new directory: %s%n \n", dir);
            }
        }
        try {

            System.out.println("Enter the file name to be created : ");
            while (true) {
                String fileName = sc.next();
                directoryName = dir + "//" + fileName + ".txt";
                File file = new File(dir + "//" + fileName + ".txt");
                boolean flag = file.createNewFile();
                if (flag) {
                    System.out.println("File has been created successfully at the specified location...\n");
                    break;
                } else {
                    System.out.println("File already present at the specified location , please enter another name\n");
                }
            }

        } catch (IOException e) {
            System.out.println("Exception occurred");
            e.printStackTrace();
        }
        return directoryName;
    }

    public synchronized static void create(String dir) {

         count = 1;
        String mapKey = null;
        int number = 0;
        do {
            try {
                System.out.println("Enter the number of details to be stored (For example you have to be store fifty details ,please enter'50' in number): ");
                number = sc.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("You entered invalid input");
            }
            sc.nextLine();
        } while (number <= 0);
        int j = 1;
        boolean flag = true;
        while (j <= number) {
            JSONObject jsonObject = new JSONObject();
            while (flag) {
                System.out.println("Enter the key name " + j + " (only 32 or below 32 characters):");
                mapKey = sc.next().toLowerCase();
                while (mapKey.length() >= 33) {
                    System.out.println("Please Enter key name under 32 char...");
                    mapKey = sc.next().toLowerCase();
                }
                if (!map.containsKey(mapKey)) {
                    break;
                } else {
                    System.out.println("You entered a key name is already present in data store...\n");
                }
            }
            int n = 0;
            do {
                try {
                    System.out.println("Enter the number of fields to be store in values (For example you have to be store fifty fields in values(json object),please enter'50' in number): ");
                    n = sc.nextInt();
                } catch (InputMismatchException e) {
                    System.out.println("You entered invalid input");
                }
                sc.nextLine();
            } while (n <= 0);
            int i = 1;
            while (i <= n) {
                File doc = new File(dir);
                if (doc.exists()) {
                    double bytes = doc.length();
                    double gigabytes = bytes / 1073741824;
                    if (gigabytes > 1) {
                        System.out.println("Your file is reached required size..\n");
                        break;
                    }
                }
                System.out.println("Enter the json key " + i + ": ");
                String jsonKey = sc.next();
                System.out.println("Enter the json value " + i + ": ");
                String jsonValue = sc.next();
                if (jsonObject.size() / 1024 > 16) {
                    System.out.println("Your json object reached size limit..\n");
                    break;
                }
                jsonObject.put(jsonKey, jsonValue);
                i++;
            }
            map.put(mapKey, jsonObject.toJSONString());
            j++;
        }
        try (FileWriter file = new FileWriter(dir)) {
            for (Map.Entry entry : map.entrySet()) {
                file.write(entry.getKey() + " = " + entry.getValue() + "\n");
            }
        } catch (Exception e) {
            System.out.println("Exception occurred");
            e.printStackTrace();
        }
    }

    public synchronized static void read() {
        if(count == 0){
            System.out.println("File is empty...");
        }else {
            boolean flag = true;
            while (flag) {
                System.out.println("Enter the key to be read:");
                String key = sc.next().toLowerCase();
                if (map.containsKey(key)) {
                    System.out.println(map.get(key));
                    System.out.println("\n");
                    break;
                } else {
                    System.out.println("You entered key is not present in data store...\n");
                    System.out.println("Available keys are...");
                    int i = 1;
                    for (Map.Entry entry : map.entrySet()) {

                        if (map.size() == i) {
                            System.out.print(entry.getKey());
                        } else {
                            System.out.print(entry.getKey() + ",");
                        }
                        i++;
                    }
                    System.out.println("\n");

                    flag = false;
                }
            }
        }
    }

    public synchronized static void delete(String dir) throws IOException {

        if(count == 0){
            System.out.println("File is empty...");
        }
        else {
            System.out.println("Enter the key to be delete:");
            String key = sc.next().toLowerCase();
            String delete = null;
            if (map.containsKey(key)) {
                delete = key + " = " + map.get(key);
                map.remove(key);
                System.out.println("You entered key is deleted successfully\n");
            } else {
                System.out.println("Enter key correctly,your response is not there in data store..\n");
                System.out.println("Available keys are...");
                int i = 1;
                for (Map.Entry entry : map.entrySet()) {

                    if (map.size() == i) {
                        System.out.print(entry.getKey());
                    } else {
                        System.out.print(entry.getKey() + ",");
                    }
                    i++;
                }
                System.out.println("\n");
            }
            String newDir = dir.substring(0, dir.length() - 4) + "1.txt";
            File inputFile = new File(dir);
            File tempFile = new File(newDir);
            tempFile.createNewFile();
            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                if (null != currentLine && !currentLine.equalsIgnoreCase(delete)) {
                    writer.write(currentLine + System.getProperty("line.separator"));
                }
            }
            writer.close();
            reader.close();
            boolean bSucceeded = false;
            try {
                if (inputFile.exists()) {
                    if (!inputFile.delete()) {
                        throw new IOException(dir + " was not successfully renamed to " + newDir);
                    }
                }
                if (!tempFile.renameTo(inputFile)) {
                    throw new IOException(dir + " was not successfully renamed to " + newDir);
                } else {
                    bSucceeded = true;
                }
            } finally {
                if (bSucceeded) {
                    tempFile.delete();
                }
            }
        }
    }
    
}
