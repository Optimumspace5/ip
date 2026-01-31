package ace.ui;

import ace.AceException;
import ace.task.Task;
import ace.task.TaskList;

import java.util.Scanner;

/**
 * Handles all user interaction for the Ace application.
 * Responsible for reading user input and displaying messages to the user.
 */
public class Ui {
    private final Scanner scanner;

    /**
     * Constructs a Ui object that reads input from standard input.
     */
    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Displays the welcome message when the application starts.
     */
    public void showWelcome() {
        System.out.println("Hello, I'm ace.Ace.");
        System.out.println("What can i do for you?");
    }

    /**
     * Reads a full line of user input from standard input.
     *
     * @return The command entered by the user.
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /**
     * Displays the farewell message before the application exits.
     */
    public void showBye() {
        System.out.println("Bye, Hope to see you again soon!");
    }

    /**
     * Displays an error message to the user.
     *
     * @param message Error message to be shown.
     */
    public void showError(String message) {
        System.out.println("Oops! " + message);
    }

    /**
     * Displays all tasks currently in the task list.
     *
     * @param tasks The task list to be displayed.
     * @throws AceException If an error occurs while accessing the task list.
     */
    public void showList(TaskList tasks) throws AceException {
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + ". " + tasks.get(i));
        }
    }

    /**
     * Displays confirmation after a task is added.
     *
     * @param task The task that was added.
     * @param size The updated size of the task list.
     */
    public void showAdded(Task task, int size) {
        System.out.println("Got it. I've added this task:");
        System.out.println(task);
        System.out.println("Now you have " + size + " tasks in the list.");
    }

    /**
     * Displays confirmation after a task is deleted.
     *
     * @param task The task that was removed.
     * @param size The updated size of the task list.
     */
    public void showDeleted(Task task, int size) {
        System.out.println("Noted. I've removed this task:");
        System.out.println(task);
        System.out.println("Now you have " + size + " tasks in the list.");
    }

    /**
     * Displays confirmation after a task is marked as completed.
     *
     * @param task The task that was marked.
     */
    public void showMarked(Task task) {
        System.out.println("Nice! I've marked this task as done:");
        System.out.println(task);
    }

    /**
     * Displays confirmation after a task is marked as not completed.
     *
     * @param task The task that was unmarked.
     */
    public void showUnmarked(Task task) {
        System.out.println("OK, I've marked this task as not done yet:");
        System.out.println(task);
    }
}
