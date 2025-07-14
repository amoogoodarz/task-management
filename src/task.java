import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class task {
    private String taskId;
    private String title;
    private String description;
    private String status;
    private Timestamp dueDate;
    private String assignedUserId;

    public task(String taskId, String title, String description, Timestamp dueDate) {
        this.taskId = taskId;
        this.title = title;
        this.description = description;
        status = "TO-DO";
        this.dueDate = dueDate;
        assignedUserId = null;
    }
    public task(String taskId, String title, String description,String status, Timestamp dueDate, String assignedUserId) {
        this.taskId = taskId;
        this.title = title;
        this.description = description;
        this.status = status;
        this.dueDate = dueDate;
        this.assignedUserId = assignedUserId;
    }
    @Override
    public String toString(){
        return "TASK ID = " + taskId + "\nTASK TITLE = " + title + "\nTASK DESCRIPTION = " + description +
                "\nTASK STATUS = " + status + "\nTASK DUE DATE = " + dueDate.toString() + "\nTASK'S ASSIGNED USER ID =" + assignedUserId;
    }

    public static void addTask(String title, String description, String dueDate) {
        Timestamp due = Timestamp.valueOf(dueDate);
        HashMap <Object, Object> newTask = new HashMap<>();
        newTask.put("title", title);
        newTask.put("description", description);
        newTask.put("status", "TO-DO");
        newTask.put("dueDate", due);
        newTask.put("assignedUserId", null);
        database.insert("tasks", newTask);
        System.out.println("adding task was successful");
    }
    public static void getTask(String id) {
        HashMap<Object, Object> con = new HashMap<>();
        con.put("id", Integer.valueOf(id));
        List <Object> objects = database.read("tasks", con);
        List <task> theTask = (List<task>) (Object) objects;
        if (theTask.isEmpty()) {
            System.out.println("not found");
            return;
        }
        for (task t : theTask)
            if (t.taskId.equals(id)) {
                System.out.println(t);
            }
    }

    public static void assignTask(String taskId, String userId) {
        HashMap <Object, Object> userCon = new HashMap<>();
        userCon.put("id", Integer.valueOf(userId));
        List <Object> userObjects = database.read("users", userCon);
        List <user> users = (List<user>) (Object) userObjects;
        if(users.isEmpty()){
            System.out.println("user not found");
            return;
        }
        HashMap <Object, Object> taskCon = new HashMap<>();
        taskCon.put("id", Integer.valueOf(taskId));
        List <Object> taskObjects = database.read("tasks", taskCon);
        List <task> tasks = (List<task>) (Object) taskObjects;
        if(tasks.isEmpty()){
            System.out.println("task not found");
            return;
        }
        for(task t: tasks){
            if(t.status.equals("DONE")){
                System.out.println("task already Done");
                return;
            }
        }
        HashMap<Object, Object> newFields = new HashMap<>();
        newFields.put("assignedUserId", Integer.valueOf(userId));
        HashMap<Object, Object> conditions = new HashMap<>();
        conditions.put("id", Integer.valueOf(taskId));
        int changes = database.update("tasks", newFields, conditions);
        if(changes > 0)
            System.out.println("Data updated successfully");
        else
            System.out.println("fail to update data");
    }
    public static void startTask(String taskId, String userId){
        HashMap <Object, Object> userCon = new HashMap<>();
        userCon.put("id", Integer.valueOf(userId));
        List <Object> userObjects = database.read("users", userCon);
        List <user> users = (List<user>) (Object) userObjects;
        if(users.isEmpty()){
            System.out.println("user not found");
            return;
        }
        HashMap <Object, Object> taskCon = new HashMap<>();
        taskCon.put("id", Integer.valueOf(taskId));
        List <Object> taskObjects = database.read("tasks", taskCon);
        List <task> tasks = (List<task>) (Object) taskObjects;
        if(tasks.isEmpty()){
            System.out.println("task not found");
            return;
        }
        task theTask = tasks.get(0);
        if(theTask.status.equals("DONE")){
            System.out.println("task already Done");
            return;
        }
        if(theTask.status.equals("IN-PROGRESS")){
            System.out.println("task already started");
            return;
        }
        HashMap<Object, Object>taskNewFields = new HashMap<>();
        HashMap<Object, Object> taskConditions = new HashMap<>();
        if(theTask.assignedUserId != null && !theTask.assignedUserId.equals(userId)){
            System.out.println("the user who is starting the task is not the one who supposed to" +
                    "\nDo you want to change the assigned user to the new one? (yes or no)");
            Scanner scanner = new Scanner(System.in);
            String response = scanner.nextLine();
            if(response.toLowerCase().equals("no"))
                return;
            taskNewFields.put("assignedUserId", Integer.valueOf(userId));
        }
        taskNewFields.put("status", "IN-PROGRESS");
        taskConditions.put("id", Integer.valueOf(userId));
        int taskChanges = database.update("tasks", taskNewFields, taskCon);
        if(taskChanges > 0)
            System.out.println("task updated successfully");
        else
            System.out.println("could not update task");
        int userScore = Integer.parseInt(users.get(0).getScore());
        userScore += 2;
        HashMap<Object, Object> userNewFields = new HashMap<>();
        userNewFields.put("score", Integer.valueOf(userScore));
        HashMap<Object, Object> userConditions = new HashMap<>();
        userConditions.put("id", Integer.valueOf(userId));
        int userChanges = database.update("users", userNewFields, userConditions);
        if(userChanges > 0)
            System.out.println("user's score increased successfully");
        else
            System.out.println("could not update user scores");
    }
    public static void completeTask(String taskId, String userId){
        HashMap <Object, Object> userCon = new HashMap<>();
        userCon.put("id", Integer.valueOf(userId));
        List <Object> userObjects = database.read("users", userCon);
        List <user> users = (List<user>) (Object) userObjects;
        if(users.isEmpty()){
            System.out.println("user not found");
            return;
        }
        HashMap <Object, Object> taskCon = new HashMap<>();
        taskCon.put("id", Integer.valueOf(taskId));
        List <Object> taskObjects = database.read("tasks", taskCon);
        List <task> tasks = (List<task>) (Object) taskObjects;
        if(tasks.isEmpty()){
            System.out.println("task not found");
            return;
        }
        task theTask = tasks.get(0);
        if(theTask.status.equals("DONE")){
            System.out.println("task already Done");
            return;
        }
        HashMap<Object, Object>taskNewFields = new HashMap<>();
        HashMap<Object, Object> taskConditions = new HashMap<>();
        HashMap<Object, Object> userNewFields = new HashMap<>();
        HashMap<Object, Object> userConditions = new HashMap<>();
        int userScore = Integer.parseInt(users.get(0).getScore());
        if(theTask.status.equals("TO-DO")){
            System.out.println("The task has not started yet" +
                    "\nDo you want to complete it at once?(yes or no)");
            Scanner scanner = new Scanner(System.in);
            String response = scanner.nextLine();
            if(response.toLowerCase().equals("no"))
                return;
            userScore += 4;
        }else{
            userScore += 2;
        }
        if(!theTask.assignedUserId.equals(userId)){
            taskNewFields.put("assignedUserId", Integer.valueOf(userId));
        }
        userNewFields.put("score", userScore);
        userConditions.put("id", Integer.valueOf(userId));
        int userChanges = database.update("users", userNewFields, userConditions);
        if(userChanges > 0)
            System.out.println("user's score increased successfully");
        else
            System.out.println("could not update user scores");

        taskNewFields.put("status", "DONE");
        taskConditions.put("id", Integer.valueOf(taskId));
        int taskChanges = database.update("tasks", taskNewFields, taskConditions);
        if(taskChanges > 0)
            System.out.println("task updated successfully");
        else
            System.out.println("could not update task");
    }
    public static void recommendedTasks(String userId) {
        List <Object> userObjects = database.read("users", null);
        List <user> users = (List<user>) (Object) userObjects;
        if(users.isEmpty()){
            System.out.println("user not found");
            return;
        }
        HashMap<Object, Object> conditions = new HashMap<>();
        List <Object> taskObjects = database.read("tasks", null);
        List <task> tasks = (List<task>) (Object) taskObjects;
        List <task> targetTasks = new ArrayList<>();
        for(task t: tasks)
            if (t.assignedUserId == "null")
                targetTasks.add(t);

        users.sort((u1, u2) -> Integer.compare(Integer.parseInt(u2.getScore()), Integer.parseInt(u1.getScore())));

        targetTasks.sort((t1, t2) -> t2.dueDate.compareTo(t1.dueDate));

        int i = 0;
        while(i != users.size())
            if(users.get(i++).getId().equals(userId))
                break;

        float scale = (float) i/users.size();
        int m = (int) scale * targetTasks.size();
        task theTask = targetTasks.get(targetTasks.size() - m);
        System.out.println(theTask);

    }
}
