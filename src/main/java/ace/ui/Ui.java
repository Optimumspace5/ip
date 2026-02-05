package ace.ui;

import ace.AceException;
import ace.task.Task;
import ace.task.TaskList;

import java.util.ArrayList;
import java.util.Scanner;public class Ui {
    private final Scanner scanner;
    private final boolean isGui;
    private final StringBuilder output;

    /**
     * Constructs a Ui object in CLI mode (reads from stdin, prints to stdout).
     */
    public Ui() {
        this(false);
    }

    /**
     * Constructs a Ui object.
     *
     * @param isGui true => GUI mode (buffer output, no stdin)
     */
    public Ui(boolean isGui) {
        this.isGui = isGui;
        this.output = new StringBuilder();
        this.scanner = isGui ? null : new Scanner(System.in);
    }

    private void println(String line) {
        if (isGui) {
            output.append(line).append(System.lineSeparator());
        } else {
            System.out.println(line);
        }
    }

    /**
     * Returns buffered output (GUI mode). Does not clear it.
     */
    public String getOutput() {
        return output.toString();
    }

    /**
     * Returns buffered output and clears it (GUI mode).
     */
    public String consumeOutput() {
        String s = output.toString();
        output.setLength(0);
        return s;
    }

    /**
     * Displays the welcome message when the application starts.
     */
    public void showWelcome() {
        println("Hello, I'm ace.Ace.");
        println("What can i do for you?");
    }

    /**
     * Reads a full line of user input from standard input.
     *
     * @return The command entered by the user.
     */
    public String readCommand() {
        if (isGui) {
            throw new IllegalStateException("readCommand() should not be used in GUI mode.");
        }
        return scanner.nextLine();
    }

    /**
     * Displays the farewell message before the application exits.
     */
    public void showBye() {
        println("Bye, Hope to see you again soon!");
    }

    /**
     * Displays an error message to the user.
     *
     * @param message Error message to be shown.
     */
    public void showError(String message) {
        println("Oops! " + message);
    }

    /**
     * Displays all tasks currently in the task list.
     *
     * @param tasks The task list to be displayed.
     * @throws AceException If an error occurs while accessing the task list.
     */
    public void showList(TaskList tasks) throws AceException {
        println("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            println((i + 1) + ". " + tasks.get(i));
        }
    }

    /**
     * Displays confirmation after a task is added.
     *
     * @param task The task that was added.
     * @param size The updated size of the task list.
     */
    public void showAdded(Task task, int size) {
        println("Got it. I've added this task:");
        println(task.toString());
        println("Now you have " + size + " tasks in the list.");
    }

    /**
     * Displays confirmation after a task is deleted.
     *
     * @param task The task that was removed.
     * @param size The updated size of the task list.
     */
    public void showDeleted(Task task, int size) {
        println("Noted. I've removed this task:");
        println(task.toString());
        println("Now you have " + size + " tasks in the list.");
    }

    /**
     * Displays confirmation after a task is marked as completed.
     *
     * @param task The task that was marked.
     */
    public void showMarked(Task task) {
        println("Nice! I've marked this task as done:");
        println(task.toString());
    }

    /**
     * Displays confirmation after a task is marked as not completed.
     *
     * @param task The task that was unmarked.
     */
    public void showUnmarked(Task task) {
        println("OK, I've marked this task as not done yet:");
        println(task.toString());
    }

    public void showFindResults(ArrayList<Task> matches) {
        println("Here are the matching tasks in your list:");
        for (int i = 0; i < matches.size(); i++) {
            println((i + 1) + ". " + matches.get(i));
        }
    }
}