import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * This class deals with loading tasks from the file and saving tasks in the file
 */
public class Storage {
    private String filepath;

    public Storage(String filepath) {
        this.filepath = filepath;
    }

    /**
     * Writes the tasks into a file
     *
     */
    public void writeFile(ArrayList<String> tasks) {
        File file = new File(filepath);

        createDirectoryAndFile(file);

        // Write into File
        try {
            FileWriter fileWriter = new FileWriter(file);
            for (int i = 0; i < tasks.size(); i++) {
                fileWriter.write(tasks.get(i));
            }
            fileWriter.close();
        } catch(IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Loads the existing saved file to task list
     *
     */
    public ArrayList<Task> readFile() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM uuuu");
        ArrayList<Task> tasks = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(this.filepath));
            String line = reader.readLine();
            while (line != null) {
                String[] parse = line.split(" > ");
                Task t;
                if (parse[0].equals("T")) {
                    t = new Todo(parse[2]);
                } else if (parse[0].equals("E")) {
                    t = new Event(parse[2], LocalDate.parse(parse[3], formatter));
                } else if (parse[0].equals("D")) {
                    t = new Deadline(parse[2], LocalDate.parse(parse[3], formatter));
                } else {
                    throw new DukeException(Constants.INVALID_FILE);
                }
                if (parse[1].equals("[X]")) {
                    t.setMarked();
                }
                tasks.add(t);
                line = reader.readLine();
            }
            reader.close();
        } catch (Exception e) {
            createDirectoryAndFile(new File(filepath));
            System.out.println(e.getMessage());
        }
        return tasks;
    }

    /**
     * Creates a new directory and file if the file does not exist
     * @param file
     */
    private void createDirectoryAndFile(File file) {
        // Create Directory or File if it does not exist
        try {
            if (!file.exists()) {
                File directory = new File(file.getParent());
                if (!directory.exists()) {
                    directory.mkdirs();
                }
                file.createNewFile();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
