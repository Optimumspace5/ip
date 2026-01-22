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
                for (int i = 0; i < index; i++) {
                    System.out.println(i+1 + ". " + list[i].toString());
                }
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
            list[index] = new Task(userInput);
            index++;
            System.out.println("added: " + userInput);
        }
    }
}
