import java.util.HashMap;
import java.util.Scanner;

public class Main {
    public static boolean isDone;
    public static void main(String[] args) {
        database.getConnection();
        preRun();
        String command;
        Scanner scanner = new Scanner(System.in);
        System.out.println("""
                welcome
                commands format:
                add user: ADD-USER|NAME|EMAIL
                show users: SHOW-USERS
                add task: ADD-TASK|TITLE|DESCRIPTION|DUE DATE
                get task: GET-TASK|TASK ID
                assign task: ASSIGN-TASK|TASK ID|USER ID
                start task: START-TASK|TASK ID|USER ID
                get recommended tasks: REC_TASKS|USER ID
                see commands: COMS
                end: END
                """);
        while (!isDone){
            System.out.println("\nenter your command");
            command = scanner.nextLine();
            String[] splitCommand = command.split("[|]");
            parse_line(splitCommand);
        }
    }
    public static void preRun(){
        database.initDatabase();
        isDone = false;
    }
    public static void parse_line(String[] splitCommand){
        switch (splitCommand[0].toLowerCase()) {
            case "add-user" -> user.addUser(splitCommand[1], splitCommand[2]);
            case "show-user" -> user.showUsers();
            case "coms" -> commands();
            case "add-task" -> task.addTask(splitCommand[1], splitCommand[2], splitCommand[3]);
            case "get-task" -> task.getTask(splitCommand[1]);
            case "assign-task" -> task.assignTask(splitCommand[1], splitCommand[2]);
            case "start-task" -> task.startTask(splitCommand[1], splitCommand[2]);
            case "complete-task" -> task.completeTask(splitCommand[1], splitCommand[2]);
            case "rec-task" -> task.recommendedTasks(splitCommand[1]);
            case "end" -> end();
            default -> System.out.println("format not supported try again!");
        }
    }
    public static void commands(){
        System.out.println("""
                add user: ADD-USER|NAME|EMAIL
                show users: SHOW-USERS
                add task: ADD-TASK|TITLE|DESCRIPTION|DUE DATE
                get task: GET-TASK|TASK ID
                assign task: ASSIGN-TASK|TASK ID|USER ID
                start task: START-TASK|TASK ID|USER ID
                get recommended tasks: REC_TASKS|USER ID
                see commands: COMS
                end: END
                """);
    }
    public static void end(){
        Main.isDone = true;
        // todo (data base)
    }
}

