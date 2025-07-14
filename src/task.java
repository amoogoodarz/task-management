import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
                System.out.println("TASK ID = " + t.taskId + "\nTASK TITLE = " + t.title + "\nTASK DESCRIPTION = " + t.description +
                        "\nTASK STATUS = " + t.status + "\nTASK DUE DATE = " + t.dueDate.toString() + "\nTASK'S ASSIGNED USER ID =" + t.assignedUserId);
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
    public static void recommendedTasks(String userId) {

    }
}
