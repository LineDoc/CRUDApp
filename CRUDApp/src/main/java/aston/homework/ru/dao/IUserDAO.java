package aston.homework.ru.dao;

import aston.homework.ru.entity.User;

import java.util.List;
import java.util.Optional;

/**
 * Интерфейс IUserDAO предназначен для разделения слоя логики работы приложения
 * от логики работы с данными (управление подключением к источнику данных для получения, хранения данных).
 */
public interface IUserDAO {
    /**
     * Метод show(int id) позволяет получить данные о пользователе по ID. Принимает в качестве
     *      * аргумента ID пользователя
     */
    Optional<User> show(int id);
    /**
     * Метод showAll() позволяет получить данные всех пользователей, записанных в БД.
     */
    List<User> showAll();
    /**
     * Метод save(User user) позволяет сохранить нового пользователя в БД. Принимает в качестве аргумента
     * сущность класса User
     */
    User save(User user);
    /**
     * Метод update(int id, String name, int age, String email) позволяет обновить данные
     * существующего пользователя. Принимает в качестве аргумента ID обновляемого пользователя и
     * его новые данные
     */
    User update(int id, String name, int age, String email);

    /**
     * Метод delete(int id) позволяет удалить пользователя User из БД по ID.
     * В качестве аргумента принимает идентификатор пользователя ID
     */
    boolean delete(int id);
}