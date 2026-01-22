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
        String[] list = new String[100];
        int index = 0;
        while (true) {
            String userInput = scanner.nextLine();

            if (userInput.equals("bye")) {
                System.out.println("Bye, Hope to see you again soon!");
                break;
            }

            if (userInput.equals("list")) {
                for (int i = 0; i < index; i++) {
                    System.out.println(i+1 + ". " + list[i]);
                }
                continue;
            }
            list[index] = userInput;
            index++;
            System.out.println("added: " + userInput);
        }
    }
}
