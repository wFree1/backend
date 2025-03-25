package org.csu.gameshopms.service;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.csu.gameshopms.entity.User;
import org.csu.gameshopms.mapper.CartMapper;
import org.csu.gameshopms.mapper.ExistingGamesMapper;
import org.csu.gameshopms.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Component
public class UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CartMapper cartMapper; // 假设有一个 CartMapper
    @Autowired
    private ExistingGamesMapper existingGamesMapper; // 假设有一个 ExistingGamesMapper

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Transactional
    public void register(User user) {
        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
        // 设置默认密码
        if (user.getPassword() == null) {
            user.setPassword("123456"); // 明文默认密码
            // 或使用加密密码
            // user.setPassword(new BCryptPasswordEncoder().encode("123456"));
        }
        userMapper.insert(user);
    }

    public List<User> listUsers(Map<String, Object> params) {
        String query = (String) params.get("query");
        int offset = (int) params.get("offset");
        int limit = (int) params.get("limit");
        return userMapper.selectUsersWithPagination(query, offset, limit);
    }

    public User getUserByName(String userName) {
        Map<String, Object> params = new HashMap<>();
        params.put("userName", userName);
        if (userMapper.selectByMap(params).isEmpty())
            return null;
        else
            return userMapper.selectByMap(params).getFirst();
    }

    public int change(Map<String, Object> params) {
        // 提取 id
        if (!params.containsKey("id")) {
            throw new IllegalArgumentException("缺少 id 参数");
        }
        int id = Integer.parseInt(params.get("id").toString());
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new IllegalArgumentException("用户不存在");
        }

        // 创建 UpdateWrapper
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", id); // 确保只更新指定用户

        // 动态设置要更新的字段
        params.forEach((key, value) -> {
            if (!key.equals("id")) { // 排除 id 字段
                updateWrapper.set(key, value); // 动态设置字段值
            }
        });

        // 执行更新
        return userMapper.update(user, updateWrapper);
    }

    public void updateUser(User userInfoData) {
        Collection<User> C = new HashSet<>();
        C.add(userInfoData);
        userMapper.updateById(C, userInfoData.getId());
    }

//    public int countUsers() {
//        return userMapper.size();
//    }

    public User getUserById(int id) {
        User user = userMapper.selectById(id);
        if (user == null) {
            return null;
        }
        // 打印日志，检查数据
        System.out.println("User: " + user);
        return user;
    }

    @Transactional // 确保事务性
    public void deleteUser(int id) {
        // 先删除 cart 表中与该用户相关的记录
        cartMapper.deleteByUserId(id);
        // 再删除 existing_games 表中与该用户相关的记录
        existingGamesMapper.deleteByUserId(id);
        // 最后删除 user 表中的记录
        if (userMapper.selectById(id) == null) {
            throw new RuntimeException("用户ID " + id + " 不存在");
        }
        userMapper.deleteById(id);
    }

//    public List<Role> listRoles() {
//        return new ArrayList<>(roleMapper.values())
//    }
//
//    public void assignRole(int userId, Integer rid) {
//        User user = userMap.get(userId);
//        if (user == null) {
//            throw new RuntimeException("用户ID " + userId + " 不存在");
//        }
//        Role role = roleMap.get(rid);
//        if (role == null) {
//            throw new RuntimeException("角色ID " + rid + " 不存在");
//        }
//        user.setRole(role); // 假设 User 类中有 setRole(Role role) 方法
//    }


    @Transactional
    public void resetPassword(int userId, String newPassword) {
        if (userId <= 0) {
            throw new IllegalArgumentException("用户ID 无效");
        }
        // 使用悲观锁
        User user = userMapper.selectByIdForUpdate(userId);
        if (user == null) {
            throw new RuntimeException("用户ID " + userId + " 不存在");
        }
        System.out.println("从数据库查询到的用户: " + user);

        user.setPassword(newPassword);

        System.out.println("准备更新用户: " + user);
        if (user.getId() == 0) {
            throw new IllegalStateException("用户ID 为空，无法更新");
        }
        if (user.getPassword() == null) {
            throw new IllegalStateException("新密码为空，无法更新");
        }

        int rows = userMapper.updateById(user);
        if (rows == 0) {
            throw new RuntimeException("更新用户密码失败，可能是用户已被删除");
        }
    }

    public int updateUserInfo(User user) {
        return userMapper.updateById(user);
    }
}

