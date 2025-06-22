package aston.homework.ru.integration;

import aston.homework.ru.dao.IUserDAO;
import aston.homework.ru.dao.UserDAOImpl;
import aston.homework.ru.entity.User;
import aston.homework.ru.service.UserService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Интеграционные тесты для DAO и Service слоёв с применением Testcontainers
 */
@Testcontainers
public class UserDAOImplTest {
    /**
     * Контейнер PostgreSQL
     */
    @Container
    private static final PostgreSQLContainer<?> CONTAINER = new PostgreSQLContainer<>
            ("postgres:16-alpine");

    private static IUserDAO userDAO;
    private static UserService userService;

    @BeforeAll
    static void startContainer() {
        CONTAINER.start();
    }

    @AfterAll
    static void stopContainer() {
        CONTAINER.stop();
    }


    @BeforeEach
    void setUp() {
        System.setProperty("hibernate.connection.url", CONTAINER.getJdbcUrl());
        System.setProperty("hibernate.connection.username", CONTAINER.getUsername());
        System.setProperty("hibernate.connection.password", CONTAINER.getPassword());
        userDAO = new UserDAOImpl();
        userService = new UserService(userDAO);
    }

    /**
     * Тестирование сохранения нового пользователя
     */
    @Test
    void saveUserTest() {
        User savedUser = userService.createNewUser("TestUser", 22, "test@mail.com");
        assertNotNull(savedUser.getId());
        assertNotNull(savedUser.getEmail());
        assertNotNull(savedUser.getCreatedAt());
    }

    /**
     * Тестирование поиска пользователя по ID.
     * Сначала производится запись нового пользователя, а затем его поиск
     */
    @Test
    void findUserByIdTest() {
        User savedUser = userService.createNewUser("TestUser", 22, "test@mail.com");
        Optional<User> findUser = userService.findById(savedUser.getId());
        assertTrue(findUser.isPresent());
    }

    /**
     * Тестирование поиска всех доступных пользователей
     */
    @Test
    void findAllUsers() {
        userService.createNewUser("TestUser1", 22, "test1@mail.com");
        userService.createNewUser("TestUser2", 21, "test2@mail.com");
        List<User> users = userService.findAll();
        assertNotNull(users);
    }

    /**
     * Тестирование удаления пользователя из БД по ID
     */
    @Test
    void deleteUserByIdTest() {
        User savedUser = userService.createNewUser("TestUser", 22, "test@mail.com");
        boolean deleteUser = userService.deleteById(savedUser.getId());
        assertTrue(deleteUser);
    }

    /**
     * Тестирование обновление данных пользователя
     */
    @Test
    void updateUserDataTest() {
        String newName = "UpdateTestUser";
        int newAge = 40;
        String newEmail = "update@mail.com";
        User oldUser = userService.createNewUser("TestUser", 33, "test@mail.ru");
        User updatedUser = userService.update(oldUser.getId(), newName, newAge, newEmail);

        assertEquals(newName, updatedUser.getName());
        assertEquals(newAge, updatedUser.getAge());
        assertEquals(newEmail, updatedUser.getEmail());

    }
}
