import java.util.*;
public class Ace {
    public static void main(String[] args) {
        String logo = " ____        _        \n"
                + "|  _ \\ _   _| | _____ \n"
                + "| | | | | | | |/ / _ \\\n"
                + "| |_| | |_| |   <  __/\n"
                + "|____/ \\__,_|_|\\_\\___|\n";
        System.out.println("Hello, I'm Ace.");
        System.out.println("What can i do for you?");

        Scanner scanner = new Scanner(System.in);
        Task[] list = new Task[100];
        int index = 0;
        while (true) {
            String userInput = scanner.nextLine();

            if (userInput.equals("bye")) {
                System.out.println("Bye, Hope to see you again soon!");
                break;
            }

            if (userInput.equals("list")) {
                System.out.println("Here are the tasks in your list:");
                for (int i = 0; i < index; i++) {
                    System.out.println(i+1 + ". " + list[i].toString());
                }
                continue;
            }

            if (userInput.startsWith("todo ")) {
                String description = userInput.substring(5);
                list[index++] = new Todo(description);
                System.out.println("Got it. I've added this task:");
                System.out.println(list[index - 1]);
                System.out.println("Now you have " + index + " tasks in the list.");
                continue;
            }

            if (userInput.startsWith("deadline ")) {
                String[] parts = userInput.substring(9).split(" /by ", 2);
                if (parts.length < 2) {
                    System.out.println("Invalid deadline format. Use: deadline <desc> /by <time>");
                    continue;
                }
                String description = parts[0];
                String by = parts[1];

                list[index++] = new Deadline(description, by);
                System.out.println("Got it. I've added this task:");
                System.out.println(list[index - 1]);
                System.out.println("Now you have " + index + " tasks in the list.");
                continue;
            }

            if (userInput.startsWith("event ")) {
                String[] parts = userInput.substring(6).split(" /from | /to ");
                if (parts.length < 3) {
                    System.out.println("Invalid event format. Use: event <desc> /from <start> /to <end>");
                    continue;
                }
                String description = parts[0];
                String from = parts[1];
                String to = parts[2];

                list[index++] = new Event(description, from, to);
                System.out.println("Got it. I've added this task:");
                System.out.println(list[index - 1]);
                System.out.println("Now you have " + index + " tasks in the list.");
                continue;
            }

            if (userInput.startsWith("mark ")) {
                try {
                int taskNumber = Integer.parseInt(userInput.substring(5));
                int taskIndex = taskNumber - 1;

                if (taskIndex < 0 || taskIndex >=index) {
                    System.out.println("Invalid task number.");
                    continue;
                }

                list[taskIndex].markDone();
                System.out.println("Nice! I've marked this task as done:");
                System.out.println(list[taskIndex]);

                } catch (NumberFormatException e) {
                    System.out.println("Invalid task number.");
                }
                continue;
            }

            if (userInput.startsWith("unmark ")) {
                try {
                    int taskNumber = Integer.parseInt(userInput.substring(7));
                    int taskIndex = taskNumber - 1;

                    if (taskIndex < 0 || taskIndex >= index) {
                        System.out.println("Invalid task number.");
                        continue;
                    }

                    list[taskIndex].markNotDone();
                    System.out.println("OK, I've marked this task as not done yet:");
                    System.out.println(list[taskIndex]);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid task number.");
                }
                continue;
            }

        }
    }
}
