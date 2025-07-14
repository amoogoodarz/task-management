import java.sql.*;
import java.util.HashMap;
import java.util.Objects;
import java.util.ArrayList;
import java.util.List;
public class database {
//    public static List<user> users = new ArrayList<>();
//    public static List<task> tasks = new ArrayList<>();
    public static Connection getConnection(){
        String url = "jdbc:postgresql://localhost:5432/postgres";
        String user = "task";
        String password = "manager";
        try{
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection(url, user, password);
            return connection;
        } catch (Exception e){
            System.err.println(e);
        }
        return null;
    }
    public static void initDatabase(){
        try(Connection connection = getConnection()){
            if(connection == null)
                return;
            DatabaseMetaData dbm = connection.getMetaData();
            ResultSet userTable = dbm.getTables(null, null, "users", null);
            if(!userTable.next()){
                System.out.println("Creating table: users");
                Statement statement = connection.createStatement();
                statement.executeUpdate("""
                        CREATE TABLE users(
                            id SERIAL PRIMARY KEY,
                            name VARCHAR(64),
                            email VARCHAR(64) UNIQUE,
                            score INTEGER DEFAULT 0);
                        """);
            }
            ResultSet taskTable = dbm.getTables(null, null, "tasks", null);
            if(!taskTable.next()){
                System.out.println("Creating table: tasks");
                Statement statement = connection.createStatement();
                statement.executeUpdate("""
                        CREATE TABLE tasks(
                            id SERIAL PRIMARY KEY,
                            title VARCHAR(128),
                            description TEXT,
                            status VARCHAR(32),
                            dueDate TIMESTAMP,
                            assignedUserId INTEGER REFERENCES users(id));
                        """);
            }
        }catch (Exception e){
            System.out.println(e);
        }
    }
    public static boolean insert(String tableName, HashMap<Object, Object> fields){
        Connection connection = null;
        PreparedStatement finalQuery = null;
        try{
            connection = getConnection();
            StringBuilder query = new StringBuilder();
            query.append("INSERT INTO ").append(tableName).append(" (");
            for(Object key: fields.keySet())
                query.append(key.toString() + ", ");
            query.delete(query.length() - 2, query.length());
            query.append(") VALUES ( ");
            for (int i = 0; i < fields.size(); i++)
                query.append("?, ");
            query.delete(query.length() - 2, query.length() - 1);
            query.append(")");
            assert connection != null;
            finalQuery = connection.prepareStatement(query.toString());
            int i = 1;
            for(Object value: fields.values())
                finalQuery.setObject(i++, value);
//            System.out.println(query);
//            System.out.println(finalQuery);
            int changes = finalQuery.executeUpdate();
            return changes > 0;
        }catch (Exception e){
            System.out.println(e);
        }finally {
            if(finalQuery != null){
                try {
                    finalQuery.close();
                }catch (Exception e){
                    System.out.println(e);
                }
            }
            if(connection != null){
                try {
                    connection.close();
                }catch(Exception e){
                    System.out.println(e);
                }
            }
        }
        return false;
    }

    public static List<Object> read(String tableName, HashMap<Object, Object> conditions){
        List <Object> resultList = new ArrayList<>();
        Connection connection = null;
        PreparedStatement finalQuery = null;
        ResultSet resultSet = null;
        try{
            connection = getConnection();
            StringBuilder query = new StringBuilder();
            query.append("SELECT * FROM ").append(tableName);
            if(conditions != null && !conditions.isEmpty()) {
                query.append(" WHERE ");
                for (Object key : conditions.keySet())
                    query.append(key + " = ? AND ");
                query.delete(query.length() - 5, query.length());
            }
            assert connection != null;
            finalQuery = connection.prepareStatement(query.toString());
            if(conditions != null && !conditions.isEmpty()) {
                int i = 1;
                for (Object value : conditions.values())
                    finalQuery.setObject(i++, value);
            }
//            System.out.println(query);
//            System.out.println(finalQuery);
            resultSet = finalQuery.executeQuery();

            if(tableName.equals("users")){
                while(resultSet.next()){
                    String id = String.valueOf(resultSet.getInt("id"));
                    String name = resultSet.getString("name");
                    String email = resultSet.getString("email");
                    String score = String.valueOf(resultSet.getInt("score"));
                    user theUser = new user(id, name, email, score);
                    resultList.add(theUser);
                }
            }else if(tableName.equals("tasks")){
                while(resultSet.next()){
                    String id = String.valueOf(resultSet.getInt("id"));
                    String title = resultSet.getString("title");
                    String desc = resultSet.getString("description");
                    String status = resultSet.getString("status");
                    Timestamp due = resultSet.getTimestamp("dueDate");
                    String assignmentId = String.valueOf(resultSet.getString("assignedUserId"));
                    task theTask = new task(id, title, desc, status, due, assignmentId);
                    resultList.add(theTask);
                }
            }
        }catch (Exception e){
            System.out.println(e);
        }finally {
            if(finalQuery != null){
                try {
                    finalQuery.close();
                }catch(Exception e){
                    System.out.println(e);
                }
            }
            if(connection != null){
                try {
                    connection.close();
                }catch(Exception e){
                    System.out.println(e);
                }
            }
            if(resultSet != null){
                try {
                    resultSet.close();
                }catch(Exception e){
                    System.out.println(e);
                }
            }
        }
        return resultList;
    }
    public static int update(String tableName, HashMap<Object, Object> newFields, HashMap<Object, Object> conditions){
        Connection connection = null;
        PreparedStatement finalQuery = null;
        try{
            connection = getConnection();
            StringBuilder query = new StringBuilder();
            query.append("UPDATE ").append(tableName).append(" SET ");
            for(Object key: newFields.keySet())
                query.append(key).append(" = ?, ");
            query.delete(query.length()-2 ,query.length());
            query.append(" WHERE ");
            for(Object key: conditions.keySet())
                query.append(key).append(" = ? AND");
            query.delete(query.length()-3 ,query.length());
            assert connection != null;
            finalQuery = connection.prepareStatement(query.toString());
            int i = 1;
            for(Object value: newFields.values())
                finalQuery.setObject(i++, value);
            for(Object value: conditions.values())
                finalQuery.setObject(i++, value);
//            System.out.println(query);
//            System.out.println(finalQuery);
            return finalQuery.executeUpdate();
        }catch(Exception e){
            System.out.println(e);
        }finally{
            if (finalQuery != null) {
                try{
                    finalQuery.close();
                }catch (SQLException e) {
                    System.out.println(e);
                }
            }
            if (connection != null) {
                try{
                    connection.close();
                }catch (SQLException e) {
                    System.out.println(e);
                }
            }
        }
        return 0;
    }
}
