package aston.homework.ru.service;

import aston.homework.ru.dao.IUserDAO;
import aston.homework.ru.entity.User;
import aston.homework.ru.exceptions.UserNotFoundException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Optional;

public class UserService {
    private final IUserDAO userDAOImpl;
    private final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public UserService(IUserDAO userDAO) {
        this.userDAOImpl = userDAO;
    }

    public Optional findById(int id) {
        Optional<User> findUser = null;
        findUser = userDAOImpl.show(id);
        if (findUser.isPresent()) {
            System.out.println(findUser.get());
            return findUser;
        }
        return findUser;
}

public List<User> findAll() {
    List<User> users = userDAOImpl.showAll();
    if (users.isEmpty()) {
        System.out.println("There are no users...");
        throw new UserNotFoundException("There are no users...");
    } else {
        return users;
    }
}

public User createNewUser(String name, int age, String email) {
        User user = new User(name, age, email);
        userDAOImpl.save(user);
        return user;
}

public User update(int id, String name, int age, String email) {
            return userDAOImpl.update(id, name, age, email);
    }

    public boolean deleteById(int id) {
        return userDAOImpl.delete(id);
    }
}
