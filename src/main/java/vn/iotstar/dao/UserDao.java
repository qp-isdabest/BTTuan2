package vn.iotstar.dao;

import vn.iotstar.entity.User;
import java.util.List;

public interface UserDao {

    User get(String username);

    void insert(User user);

    void update(User user);

    boolean checkExistEmail(String email);

    boolean checkExistUsername(String username);

    boolean checkExistPhone(String phone);

    List<User> findAll();
}

