package vn.iotstar.service.impl;

import vn.iotstar.dao.UserDao;
import vn.iotstar.dao.impl.UserDaoImpl;
import vn.iotstar.entity.User;
import vn.iotstar.service.UserService;

import java.sql.Date;
import java.util.List;

public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    public UserServiceImpl() {
        this.userDao = new UserDaoImpl();
    }

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public User login(String username, String password) {
        if (username == null || password == null) {
            return null;
        }
        User user = this.get(username.trim());
        if (user != null && password.equals(user.getPassWord())) {
            return user;
        }
        return null;
    }

    @Override
    public User get(String username) {
        if (username == null || username.trim().isEmpty()) {
            return null;
        }
        return userDao.get(username.trim());
    }

    @Override
    public void insert(User user) {
        userDao.insert(user);
    }

    @Override
    public boolean register(String username, String password, String email, String fullname, String phone) {
        if (userDao.checkExistUsername(username)) {
            return false;
        }
        if (userDao.checkExistEmail(email)) {
            return false;
        }
        long millis = System.currentTimeMillis();
        Date date = new Date(millis);
        // roleid = 3 là người dùng thông thường (Regular User)
        User newUser = new User(email, username, fullname, password, null, 3, phone, date);
        userDao.insert(newUser);
        return true;
    }

    @Override
    public boolean checkExistEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return userDao.checkExistEmail(email.trim());
    }

    @Override
    public boolean checkExistUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            return false;
        }
        return userDao.checkExistUsername(username.trim());
    }

    @Override
    public boolean checkExistPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return false;
        }
        return userDao.checkExistPhone(phone.trim());
    }

    @Override
    public List<User> findAll() {
        return userDao.findAll();
    }
}

