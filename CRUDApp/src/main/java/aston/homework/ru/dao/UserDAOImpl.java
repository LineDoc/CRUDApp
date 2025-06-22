package aston.homework.ru.dao;

import aston.homework.ru.entity.User;
import aston.homework.ru.exceptions.UserNotFoundException;
import aston.homework.ru.util.HibernateUtil;
import org.hibernate.Session;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserDAOImpl implements IUserDAO {
    private static final Logger logger = Logger.getLogger(UserDAOImpl.class.getName());

    /**
     * Поиск сущности User в БД по ID, оборачиваем в Optional для удобства.
     */
    @Override
    public Optional<User> show(int id) {
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            User user = session.find(User.class, id);
            session.getTransaction().commit();
            return Optional.ofNullable(user);
        } catch (UserNotFoundException e) {
            session.getTransaction().rollback();
            logger.log(Level.SEVERE, "The user with this id was not found.: " + id, e);
            return Optional.empty();
        } catch (Exception e) {
            session.getTransaction().rollback();
            logger.log(Level.SEVERE, "Error finding user by id: " + id, e);
            return Optional.empty();
        }
    }

    /**
     * Поиск всех имеющихся сущностей User в БД
     */
    @Override
    public List<User> showAll() {
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            List<User> users = session.createQuery("from User", User.class).getResultList();
            session.getTransaction().commit();
            return users;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error finding all users", e);
            throw new RuntimeException("Error identifying all users", e);
        }
    }

    /**
     * Сохранение новой сущности User в БД.
     */
    @Override
    public User save(User user) {
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            user.setCreatedAt(LocalDateTime.now());
            session.persist(user);
            session.getTransaction().commit();
            System.out.println("The user has been added");
            System.out.println(user);
            return user;
        } catch (Exception e) {
            session.getTransaction().rollback();
            logger.log(Level.SEVERE, "Error saving user: " + user, e);
            throw new RuntimeException("Error to save user", e);
        }
    }

    /**
     * Обновление имеющейся сущности в БД. В качестве аргументов поступают id обновляемого пользователя и
     * его новые данные
     */
    @Override
    public User update(int id, String name, int age, String email) {
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            User updatedUser = session.find(User.class, id);
            updatedUser.setName(name);
            updatedUser.setEmail(email);
            updatedUser.setAge(age);
            session.getTransaction().commit();
            System.out.println("The user's data has been updated!");
            return updatedUser;
        } catch (UserNotFoundException e) {
            session.getTransaction().rollback();
            logger.log(Level.SEVERE, "The user with this id was not found: " + id, e);
            throw new UserNotFoundException("The user not found");
        } catch (Exception e) {
            session.getTransaction().rollback();
            logger.log(Level.SEVERE, "Error updating user with ID: " + id, e);
            throw new RuntimeException("Error to update user", e);
        }
    }

    /**
     * Поиск и удаление сущности в БД по ID
     */
    @Override
    public boolean delete(int id) {
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            User deleteUser = session.find(User.class, id);
            session.remove(deleteUser);
            session.getTransaction().commit();
            System.out.println("The user has been successfully deleted!");
            return true;
        } catch (Exception e) {
            session.getTransaction().rollback();
            logger.log(Level.SEVERE, "Error deleting user by id: " + id, e);
            throw new RuntimeException("Error to delete user by id", e);
        }
    }
}