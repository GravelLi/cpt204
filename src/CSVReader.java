import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CSVReader {
    public static Location[] readLocations(String filePath) {
        Location[] locations = new Location[1000];
        int index = 0;

        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line = reader.readLine();

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");

                String locationId = parts[0].trim();
                int priorityScore = Integer.parseInt(parts[1].trim());

                locations[index] = new Location(locationId, priorityScore);
                index++;
            }

            reader.close();
        } catch (IOException e) {
            System.out.println("Error reading file: " + filePath);
            e.printStackTrace();
        }

        Location[] result = new Location[index];

        for (int i = 0; i < index; i++) {
            result[i] = locations[i];
        }

        return result;
    }
}