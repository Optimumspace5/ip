import java.util.*;
public class Ace {
    public static void main(String[] args) {

        System.out.println("Hello, I'm Ace.");
        System.out.println("What can i do for you?");

        Scanner scanner = new Scanner(System.in);
        ArrayList<Task> list = new ArrayList<>();
        while (true) {
            try {
                String userInput = scanner.nextLine();

                if (userInput.equals("bye")) {
                    System.out.println("Bye, Hope to see you again soon!");
                    break;
                }

                if (userInput.equals("todo")) {
                    throw new AceException("The description of a todo cannot be empty.");
                }

                if (userInput.equals("deadline")) {
                    throw new AceException("Invalid deadline format. Use: deadline <desc> /by <time>");
                }

                if (userInput.equals("event")) {
                    throw new AceException("Invalid event format. Use: event <desc> /from <start> /to <end>");
                }

                if (userInput.equals("list")) {
                    System.out.println("Here are the tasks in your list:");
                    for (int i = 0; i < list.size(); i++) {
                        System.out.println(i + 1 + ". " + list.get(i).toString());
                    }
                    continue;
                }
                if (userInput.startsWith("delete ")) {
                    try {
                        int taskNumber = Integer.parseInt(userInput.substring(7));
                        int taskIndex = taskNumber - 1;

                        if (taskIndex < 0 || taskIndex >= list.size()) {
                            throw new AceException("Invalid task number.");
                        }

                        Task removedTask = list.remove(taskIndex);

                        System.out.println("Noted. I've removed this task:");
                        System.out.println(removedTask);
                        System.out.println("Now you have " + list.size() + " tasks in the list.");

                    } catch (NumberFormatException e) {
                        throw new AceException("Invalid task number.");
                    }
                    continue;
                }

                if (userInput.startsWith("todo ")) {
                    String description = userInput.substring(5);
                    list.add(new Todo(description));
                    System.out.println("Got it. I've added this task:");
                    System.out.println(list.get(list.size() - 1));
                    System.out.println("Now you have " + list.size() + " tasks in the list.");
                    continue;
                }

                if (userInput.startsWith("deadline ")) {
                    String[] parts = userInput.substring(9).split(" /by ", 2);
                    if (parts.length < 2) {
                        throw new AceException("Invalid deadline format. Use: deadline <desc> /by <time>");
                    }
                    String description = parts[0];
                    String by = parts[1];

                    list.add(new Deadline(description, by));
                    System.out.println("Got it. I've added this task:");
                    System.out.println(list.get(list.size() - 1));
                    System.out.println("Now you have " + list.size() + " tasks in the list.");
                    continue;
                }

                if (userInput.startsWith("event ")) {
                    String[] parts = userInput.substring(6).split(" /from | /to ");
                    if (parts.length < 3) {
                        throw new AceException("Invalid event format. Use: event <desc> /from <start> /to <end>");
                    }
                    String description = parts[0];
                    String from = parts[1];
                    String to = parts[2];

                    list.add(new Event(description, from, to));
                    System.out.println("Got it. I've added this task:");
                    System.out.println(list.get(list.size() - 1));
                    System.out.println("Now you have " + list.size() + " tasks in the list.");
                    continue;
                }

                if (userInput.startsWith("mark ")) {
                    try {
                        int taskNumber = Integer.parseInt(userInput.substring(5));
                        int taskIndex = taskNumber - 1;

                        if (taskIndex < 0 || taskIndex >= list.size()) {
                            throw new AceException("Invalid task number.");
                        }

                        list.get(taskIndex).markDone();
                        System.out.println("Nice! I've marked this task as done:");
                        System.out.println(list.get(taskIndex));

                    } catch (NumberFormatException e) {
                        throw new AceException("Invalid task number.");
                    }
                    continue;
                }

                if (userInput.startsWith("unmark ")) {
                    try {
                        int taskNumber = Integer.parseInt(userInput.substring(7));
                        int taskIndex = taskNumber - 1;

                        if (taskIndex < 0 || taskIndex >= list.size()) {
                            throw new AceException("Invalid task number.");
                        }

                        list.get(taskIndex).markNotDone();
                        System.out.println("OK, I've marked this task as not done yet:");
                        System.out.println(list.get(taskIndex));
                    } catch (NumberFormatException e) {
                        throw new AceException("Invalid task number.");
                    }
                    continue;
                }

                throw new AceException("I don't know what that means.");

            } catch (AceException e) {
                System.out.println("Oops! " + e.getMessage());
            }
        }
    }
}
