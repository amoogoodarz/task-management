import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static int userNum;
    public static int taskNum;
    public static boolean isDone;
    public static void main(String[] args) {
        preRun();
        String command;
        Scanner scanner = new Scanner(System.in);
        System.out.println("""
                welcome
                commands format:
                add user: ADD-USER|NAME|EMAIL
                show users: SHOW-USERS
                add task: ADD-TASK|TITLE|DESCRIPTION|DUE DATE|USER ID
                get task: GET-TASK|TASK ID
                assign task: ASSIGN-TASK|TASK ID|USER ID
                get recommended tasks: REC_TASKS|USER ID
                see commands: COMS
                end: END
                """);
        while (!isDone){
            System.out.println("enter your command");
            command = scanner.nextLine();
            String[] splitCommand = command.split("[|]");
            parse_line(splitCommand);
        }
    }
    public static void preRun(){
        isDone = false;
        userNum = 0;
        taskNum = 0;
    }
    public static void parse_line(String[] splitCommand){
        switch (splitCommand[0]) {
            case "ADD-USER" -> user.addUser(splitCommand[1], splitCommand[2]);
            case "SHOW-USERS" -> user.showUsers();
            case "COMS" -> commands();
            case "ADD-TASK" -> task.addTask(splitCommand[1], splitCommand[2], splitCommand[3]);
            case "GET-TASK" -> task.getTask(splitCommand[1]);
            case "ASSIGN-TASK" -> task.assignTask(splitCommand[1], splitCommand[2]);
            case "REC-TASKS" -> task.recommendedTasks(splitCommand[1]);
            case "END" -> Main.isDone = true;
            default -> System.out.println("format not supported try again!");
        }
    }
    public static void commands(){
        System.out.println("""
                add user: ADD-USER|NAME|EMAIL
                show users: SHOW-USERS
                add task: ADD-TASK|TITLE|DESCRIPTION|DUE DATE|USER ID
                get task: GET-TASK|TASK ID
                assign task: ASSIGN-TASK|TASK ID|USER ID
                get recommended tasks: REC_TASKS|USER ID
                see commands: CONS
                end: END
                """);
    }
}

class user {
    private String id;
    private String name;
    private String email;
    public user(String id, String name, String email){
        this.id = id;
        this.name = name;
        this.email= email;
    }
    public static void addUser(String name, String email){
        for(user u: database.users){
            if(u.email.equals(email)){
                System.out.println("User with this email already exists");
                return;
            }
        }
        user newUser = new user(String.valueOf(Main.userNum++), name, email);
        database.users.add(newUser);
        System.out.println("adding user was successful");
    }
    public static void showUsers(){
        if(Main.userNum == 0){
            System.out.println("no user added so far");
            return;
        }
        System.out.println("USER ID\tUSER NAME\tUSER EMAIL");
        for(user u: database.users)
            System.out.println(u.id +"\t"+ u.name +"\t"+ u.email);
    }
}
class task {
    private String taskId;
    private String title;
    private String description;
    private String status;
    private LocalDateTime dueDate;
    private String assignedUserId;
    public task(String taskId, String title, String description, LocalDateTime dueDate){
        this.taskId = taskId;
        this.title = title;
        this.description=description;
        status = "TO-DO";
        this.dueDate = dueDate;
        assignedUserId = null;
    }
    public static void addTask(String title, String description, String dueDate){
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(dueDate, formatter);
        //System.out.println(dateTime.format(formatter));
        task newtask = new task(String.valueOf(Main.taskNum++), title, description, dateTime);
        database.tasks.add(newtask);
        System.out.println("adding task was successful");
    }
    public static void getTask(String id){
        if(Integer.parseInt(id) >= Main.taskNum){
            System.out.println("not found");
            return;
        }
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        for(task t: database.tasks)
            if(t.taskId.equals(id)){
                System.out.println("TASK ID = " + t.taskId + "\nTASK TITLE = " + t.title + "\nTASK DESCRIPTION = " + t.description +
                        "\nTASK STATUS = " + t.status + "\nTASK DUE DATE = " + t.dueDate.format(formatter) + "\nTASK'S ASSIGNED USER ID =" + t.assignedUserId);
            }
    }
    public static void assignTask(String taskId, String userId){
        if(Integer.parseInt(taskId) >= Main.taskNum){
            System.out.println("task not found");
            return;
        }
        if(Integer.parseInt(userId) >= Main.userNum){
            System.out.println("user not found");
            return;
        }
        for(task t: database.tasks)
            if(t.taskId.equals(taskId))
                t.assignedUserId = userId;
        System.out.println("task assigned successfully");
    }
    public static void recommendedTasks(String userId){

    }
}
class database {
    public static List<user> users = new ArrayList<>();
    public static List<task> tasks = new ArrayList<>();



}