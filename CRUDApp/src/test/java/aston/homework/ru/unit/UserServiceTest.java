package aston.homework.ru.unit;

import aston.homework.ru.dao.IUserDAO;
import aston.homework.ru.dao.UserDAOImpl;
import aston.homework.ru.entity.User;
import aston.homework.ru.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;

/**
 * {@link UserServiceTest} содержит Unit-тесты Service-слоя при помощи JUnit5 и Mockito.
 */
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    /**
     * Помечаем поле {@link IUserDAO} как заглушку.
     */
    @Mock
    private IUserDAO userDAO;
    /**
     * Помечаем поле {@link UserService} для внедрения mock-ов
     */
    @InjectMocks
    private UserService userService;

    private User testUser;
    private int id;
    LocalDateTime localDateTime;

    /**
     * Метод setup() перед запуском каждого теста создаёт нового пользователя
     */
    @BeforeEach
    void setup() {
        testUser = new User("test111", 12, "test111@inbox.ru");
        testUser.setId(id = 23);
        testUser.setCreatedAt(localDateTime = LocalDateTime.now());
    }

    /**
     * Тестирование создания нового пользователя.
     * Задаём поведение заглушки {@link UserDAOImpl} таким образом, чтобы
     * при добавлении любого пользователя возвращался пользователь testUser
     */
    @Test
    void saveNewUserTest_WhenUserNotExist_ShouldReturnNewUser() {
        //Arrange
        Mockito.when(userDAO.save(any(User.class))).thenReturn(testUser);
        //Act
        User result = userService.createNewUser(testUser.getName(), testUser.getAge(), testUser.getEmail());
        result.setId(id);
        result.setCreatedAt(localDateTime);
        //Assert
        assertEquals(result, testUser);
    }
    /**
     * Тестирование поиска пользователя по ID, если пользователь существует в БД
     */
    @Test
    void findUserByIdTest_WhenUserExist_MustReturnUser() {
        //Arrange
        Mockito.when(userDAO.show(id)).thenReturn(Optional.of(testUser));
        //Act
        Optional<User> result = userService.findById(id);
        //Assert
        assertTrue(result.isPresent());
    }
    /**
     * Тестирование поиска пользователя по ID, если пользователь с таким ID отсутствует в БД
     */
    @Test
    void findUserByIdTest_WhenUserNotExist() {
        //Arrange
        Mockito.when(userDAO.show(anyInt())).thenReturn(Optional.empty());
        //Act
        Optional<User> result = userService.findById(id);
        //Assert
        assertFalse(result.isPresent());
    }
    /**
     * Тестирование поиска всех доступных пользователей в БД
     */
    @Test
    void showAllUsersTest_ShouldReturnAllUsers() {
        //Arrange
        List<User> users = Arrays.asList(testUser,
                new User("testAll1", 22, "testall@yandex.ru"),
                new User("testAll2", 54, "testall2@mail.com"));
        Mockito.when(userDAO.showAll()).thenReturn(users);
        //Act
        List<User> resultList = userService.findAll();
        //Assert
        assertEquals(users, resultList);
    }
    /**
     * Тестирование обновления данных пользователя.
     * Поведение mock-а задаётся таким образом, чтобы при передаче любых параметров
     * в метод update() возвращался заранее созданный обновлённый User
     */
    @Test
    void updateUserTest_ShouldReturnUpdateUser() {
        //Arrange
        String newName = "updateTest";
        int newAge = 22;
        String newEmail = "updatetest@inbox.ru";
        User updatedUser = new User(newName, newAge, newEmail);
        Mockito.when(userDAO.update(anyInt(), anyString(), anyInt(), anyString())).thenReturn(updatedUser);
        //Act
        User result = userService.update(id, newName, newAge, newEmail);
        //Assert
        assertEquals(newName, result.getName());
        assertEquals(newAge, result.getAge());
        assertEquals(newEmail, result.getEmail());
    }

    /**
     * Тестирование удаления существующего пользователя.
     */
    @Test
    void deleteUserByIdTest_WhenUserExist_ShouldReturnTrue() {
        //Arrange
        Mockito.when(userDAO.delete(id)).thenReturn(true);
        //Act
        boolean result = userService.deleteById(id);
        //Assert
        assertTrue(result);

    }

    /**
     * Тестирование удаления пользователя, если пользователя с таким ID не существует.
     */
    @Test
    void deleteUserByIdTest_WhenUserNotExist_ShouldReturnFalse() {
        //Arrange
        Mockito.when(userDAO.delete(id)).thenReturn(false);
        //Act
        boolean result = userService.deleteById(id);
        //Assert
        assertFalse(result);
    }
}
