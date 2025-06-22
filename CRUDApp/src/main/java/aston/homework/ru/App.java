package aston.homework.ru;

import aston.homework.ru.dao.IUserDAO;
import aston.homework.ru.dao.UserDAOImpl;
import aston.homework.ru.entity.User;
import aston.homework.ru.service.UserService;
import aston.homework.ru.util.HibernateUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class App {
    private static final IUserDAO USER_DAO = new UserDAOImpl();
    public static void main(String[] args) {
        final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        final UserService userService = new UserService(USER_DAO);
            try {
                boolean run = true;
                while(run) {
                    System.out.println("Select an operation:" + "\n"
                            + "1 - Registering a new user." + "\n"
                            + "2 - Update user data" + "\n"
                            + "3 - Find a user" + "\n"
                            + "4 - Show all users" + "\n"
                            + "5 - Delete a user" + "\n"
                            + "esc - to exit the program");
                    String choose = reader.readLine();
                    switch (choose) {
                        case ("1"):
                            System.out.println("Enter the user's details");
                            System.out.print("Enter the user's Name: ");
                            String newName = reader.readLine();
                            System.out.print("Enter the user's Age: ");
                            int newAge = Integer.parseInt(reader.readLine());
                            System.out.print("Enter the user's Email: ");
                            String newEmail = reader.readLine();
                            userService.createNewUser(newName, newAge, newEmail);
                            break;
                        case ("2"):
                            System.out.println("Enter the data you want to update");
                            System.out.print("Enter the user's ID: ");
                            int updateId = Integer.parseInt(reader.readLine());
                            System.out.print("Enter the user's Name: ");
                            String updateName = reader.readLine();
                            System.out.print("Enter the user's Age: ");
                            int updateAge = Integer.parseInt(reader.readLine());
                            System.out.print("Enter the user's Email: ");
                            String updateEmail = reader.readLine();
                            userService.update(updateId, updateName, updateAge, updateEmail);
                            break;
                        case ("3"):
                            System.out.print("Enter the User's ID: ");
                            int findId = Integer.parseInt(reader.readLine());
                            userService.findById(findId);
                            break;
                        case ("4"):
                            List<User> users = userService.findAll();
                            for(User user : users) {
                                System.out.println(user);
                            }
                            break;
                        case ("5"):
                            System.out.print("Enter the user's ID: ");
                            int deleteId = Integer.parseInt(reader.readLine());
                            userService.deleteById(deleteId);
                            break;
                        case ("esc"):
                            run = false;
                            break;
                        default:
                            System.out.println("An incorrect command number has been entered! Try again." + "\n");
                    }
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            } finally {
                HibernateUtil.close();
                try {
                    reader.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }