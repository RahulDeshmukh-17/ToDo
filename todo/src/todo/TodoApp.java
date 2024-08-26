package todo;

import java.sql.*;
import java.util.Scanner;

public class TodoApp {

    private static final String URL = "jdbc:mysql://localhost:3307/todo_db";
    private static final String USER = "root";
    private static final String PASSWORD = "R@huld17";

    private static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void createTask(String description) {
        String sql = "INSERT INTO tasks (description) VALUES (?)";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, description);
            pstmt.executeUpdate();
            System.out.println("Task added.");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void readTasks() {
        String sql = "SELECT * FROM tasks";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String description = rs.getString("description");
                boolean isDone = rs.getBoolean("is_done");
                System.out.println(id + ". " + (isDone ? "[D] " : "[ ] ") + description);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void updateTask(int id) {
        String sql = "UPDATE tasks SET is_done = TRUE WHERE id = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Task marked as done.");
            } else {
                System.out.println("Task not found.");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void deleteTask(int id) {
        String sql = "DELETE FROM tasks WHERE id = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Task deleted.");
            } else {
                System.out.println("Task not found.");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nTo-Do List:");
            readTasks();

            System.out.println("\nOptions:");
            System.out.println("1. Add a new task");
            System.out.println("2. Mark a task as done");
            System.out.println("3. Delete a task");
            System.out.println("4. Exit123");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();  

            switch (choice) {
                case 1:
                    System.out.print("Enter the description of the new task: ");
                    String description = scanner.nextLine();
                    createTask(description);
                    break;
                case 2:
                    System.out.print("Enter the task number to mark as done: ");
                    int doneIndex = scanner.nextInt();
                    updateTask(doneIndex);
                    break;
                case 3:
                    System.out.print("Enter the task number to delete: ");
                    int deleteIndex = scanner.nextInt();
                    deleteTask(deleteIndex);
                    break;
                case 4:
                    System.out.println("Exiting...");
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
}

