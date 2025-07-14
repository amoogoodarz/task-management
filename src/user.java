import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
public class user {
    private String id;
    private String name;
    private String email;

    public user(String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public static void addUser(String name, String email) {
        List <Object> objects = database.read("users", null);
        List <user> users = (List<user>) (Object) objects;
        for(user u : users) {
            if (u.email.equals(email)) {
                System.out.println("User with this email already exists");
                return;
            }
        }
        HashMap <Object, Object> newUser = new HashMap<>();
        newUser.put("name", name);
        newUser.put("email", email);
        database.insert("users", newUser);
        System.out.println("adding user was successful");
    }

    public static void showUsers() {
        List <Object> objects = database.read("users", null);
        List <user> users = (List<user>) (Object) objects;
        if(users.isEmpty()){
            System.out.println("no user added so far");
            return;
        }
        System.out.println("   USER ID\tUSER NAME\tUSER EMAIL");
        for(user u: users)
            System.out.println(u.id + "\t" + u.name + "\t" + u.email);
        }
}
