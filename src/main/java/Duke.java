import java.util.Scanner;
import java.util.ArrayList;

public class Duke {
    private static final String SPACE = " ";
    private static final String LINE = "________________________________________________________________\n";
    private static final String NAME = "Shanice:)";
    private static String[] segments;
    private static ArrayList<Task> tasks = new ArrayList<Task>();

    private static SaveFile saveFile = new SaveFile();

    public static void printMessage(String message) {
        System.out.println(LINE + message + LINE);
    }

    public static void endProgram() {
        printMessage("Bye. Hope to see you again soon!\n");
    }

    public static void displayList(ArrayList<Task> tasks) throws DukeException {
        if (tasks.size() != 0) {
            System.out.println(LINE + "Here are the tasks in your list: ");
            for (int i = 0; i < tasks.size(); i++) {
                System.out.println(i+1 + ". " + tasks.get(i));
            }
            System.out.println(LINE);
        }
        else {
            throw new DukeException("The list is currently empty. Please add some tasks");
        }
    }

    public static void markOrUnmarkTask(ArrayList<Task> tasks, String keyword, String description) throws DukeException {
            int index = Integer.parseInt(description);
            if (index <= tasks.size()) {
                Task t = tasks.get(index - 1);
                if (keyword.equals("mark")) {
                    t.markAsDone();
                    printMessage("Nice! I've marked this task as done: \n" + t + "\n");
                }
                else {
                    t.markAsNotDone();
                    printMessage("OK, I've marked this task as not done yet:\n" + t + "\n");
                }
            }
            else
                throw new NoSuchTaskException();

    }

    public static void createOtherTask(ArrayList<Task> tasks, String input, String keyword, String description) throws DukeException {
        Task t;
        if (keyword.equals("deadline")) {
            description = description.replace("by ", "");
            t = new Deadline(input, description);
        }
        else {
            description = description.replace("at ", "");
            t = new Events(input, description);
        }
        tasks.add(t);
        printMessage("Got it. I've added this task:\n" + t + "\nNow you have " + tasks.size() + " tasks in the list.\n");
        saveFile.writeFile(tasks);
    }

    public static void createTodoTask(ArrayList<Task> tasks, String input) throws DukeException {
        Task t;
        t = new Todo(input);
        tasks.add(t);
        printMessage("Got it. I've added this task:\n" + t + "\nNow you have " + tasks.size() + " tasks in the list.\n");
        saveFile.writeFile(tasks);
    }

    public static void deleteTask(ArrayList<Task> tasks, String description) throws DukeException {
        int index = Integer.parseInt(description);
        if (index <= tasks.size()) {
            Task t = tasks.get(index - 1);
            tasks.remove(t);
            printMessage("Noted. I've removed this task: \n" + t + "\nNow you have " + tasks.size() + " tasks in the list.\n");
            saveFile.writeFile(tasks);
        }
        else
            throw new NoSuchTaskException();
    }

    public static void main(String[] args) {
        saveFile.readFile(tasks);
        String input;
        Scanner sc= new Scanner(System.in);
        printMessage("Hello! I'm " + NAME + "\nWhat can I do for you?\n");

        while (sc.hasNextLine()) {
            try {
                input = sc.nextLine();
                segments = input.split("/");
                String frontWords = segments[0].toLowerCase(); //capture first half of input before "/"
                String[] inputSplit = frontWords.split(SPACE);

                if (input.equalsIgnoreCase("bye")) {
                    endProgram();
                    break;
                }
                else if (input.equalsIgnoreCase("list"))
                    displayList(tasks);
                else if (frontWords.startsWith("mark") || frontWords.startsWith("unmark")) {
                    if (input.replace(inputSplit[0], "").equals(""))
                        throw new MissingInputException("mark / unmark.");
                    else
                        markOrUnmarkTask(tasks, inputSplit[0], inputSplit[1]);
                }
                else if (frontWords.startsWith("todo")){
                    if (input.replace(inputSplit[0], "").equals(""))
                        throw new MissingInputException("add into list");
                    else
                        createTodoTask(tasks, frontWords);

                } else if (frontWords.startsWith("deadline") || frontWords.startsWith("event")) {
                    if (input.replace(inputSplit[0], "").equals(""))
                        throw new MissingInputException("add into list");
                    else if (segments[1].equals(""))
                        throw new EmptyDescriptionException(inputSplit[0]);
                    else
                        createOtherTask(tasks, frontWords, frontWords.split(SPACE)[0], segments[1]);
                }
                else if (frontWords.startsWith("delete"))
                    if (input.replace(inputSplit[0], "").equals(""))
                        throw new MissingInputException("delete.");
                    else
                        deleteTask(tasks, inputSplit[1]);
                else
                    throw new UnknownInputException();
            }
            catch (Exception e) {
                printMessage(e.getMessage());
            }
        }
    }

}

