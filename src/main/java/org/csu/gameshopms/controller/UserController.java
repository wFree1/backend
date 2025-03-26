package org.csu.gameshopms.controller;

import org.csu.gameshopms.entity.User;
import org.csu.gameshopms.mapper.UserMapper;
import org.csu.gameshopms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;
    @Qualifier("userMapper")
    @Autowired
    private UserMapper userMapper;


    /**
     * 2. 获取用户详细信息接口
     * 请求路径：GET /user/getUserInfo
     * 可通过查询参数（例如 username）获取用户信息
     */
    @GetMapping("/user/getUserInfo")
    public User getUserInfo(@RequestParam String username) {
        return userService.getUserByName(username);
    }

    /**
     * 3. 修改用户信息接口
     * 请求路径：POST /user/updateUserInfo
     * 前端传入 JSON 格式的用户信息
     */
    @PatchMapping("/user/update")
    public Map<String, Object> updateUser(@RequestBody Map<String, Object> params) {
        Map<String, Object> metaMap = new HashMap<>();
        try {
            int result = userService.change(params);
            if (result > 0) {
                metaMap.put("status", 200);
                metaMap.put("msg", "更新用户成功");
            } else {
                metaMap.put("status", 500);
                metaMap.put("msg", "更新用户失败，可能是数据库问题");
            }
        } catch (IllegalArgumentException e) {
            metaMap.put("status", 400);
            metaMap.put("msg", e.getMessage());
        } catch (RuntimeException e) {
            metaMap.put("status", 500);
            metaMap.put("msg", "更新用户失败：" + e.getMessage());
        } catch (Exception e) {
            metaMap.put("status", 500);
            metaMap.put("msg", "服务器内部错误，请联系管理员");
            e.printStackTrace();
        }
        Map<String, Object> response = new HashMap<>();
        response.put("meta", metaMap);
        response.put("data", null);
        return Collections.unmodifiableMap(response);
    }


    /**
     * 4. 获取用户列表接口
     * 请求路径：GET /users
     * 支持分页：page（页码），pageSize（每页数量）
     */
    @GetMapping("/users")
    @CrossOrigin(origins = "http://localhost:5173")
    public Map<String, Object> getUserList(
            @RequestParam(defaultValue = "") String query,
            @RequestParam(defaultValue = "1") int pagenum,
            @RequestParam(defaultValue = "10") int pagesize) {
        int offset = (pagenum - 1) * pagesize;
        Map<String, Object> params = new HashMap<>();
        params.put("query", query);
        params.put("offset", offset);
        params.put("limit", pagesize);
        List<User> users = userService.listUsers(params);

        int total = userMapper.getTotalUserCount(query);

        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("users", users.stream().map(user -> Map.of(
                "id", user.getId(),
                "username", user.getUsername(),
                "email", user.getEmail()
        )).collect(Collectors.toList()));
        dataMap.put("total", total);

        Map<String, Object> metaMap = new HashMap<>();
        metaMap.put("status", 200);
        metaMap.put("msg", "获取用户列表成功");

        return Map.of("meta", metaMap, "data", dataMap);
    }




    /**
     * 5. 添加新用户接口（管理员操作）
     * 请求路径：POST /users
     * 前端传入 JSON 格式的新用户数据
     */
    @PostMapping("/users")
    public ResponseEntity<Map<String, Object>> addUser(@RequestBody User user) {
        try {
            userService.register(user);
            Map<String, Object> response = new HashMap<>();
            Map<String, Integer> meta = new HashMap<>();
            meta.put("status", 201);
            response.put("meta", meta);
            response.put("data", "User registered successfully");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            Map<String, Integer> meta = new HashMap<>();
            meta.put("status", 500);
            response.put("meta", meta);
            response.put("data", "Failed to register user: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    /**
     * 6. 获取单个用户信息接口
     * 请求路径：GET /users/{id}
     */
    @GetMapping("/users/{id}")
    public Map<String, Object> getUserById(@PathVariable("id") int id) {
        User user = userService.getUserById(id);
        if (user == null) {
            return Map.of(
                    "meta", Map.of("status", 404, "msg", "用户不存在"),
                    "data", null
            );
        }
        Map<String, Object> userData = new HashMap<>();
        userData.put("id", user.getId());
        userData.put("username", user.getUsername());
        userData.put("email", user.getEmail());

        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("user", userData);

        return Map.of(
                "meta", Map.of("status", 200, "msg", "获取用户成功"),
                "data", dataMap
        );
    }
    /**
     * 7. 修改用户信息接口（管理员操作）
     * 请求路径：PUT /users/{id}
     * 前端传入 JSON 格式的用户数据（需包含修改字段）
     */
    @PutMapping("/users/{id}")
    public String updateUserById(@PathVariable int id, @RequestBody User userData) {
        userData.setId(id);
        userService.updateUser(userData);
        return "用户更新成功";
    }

    /**
     * 8. 删除用户接口
     * 请求路径：DELETE /users/{id}
     */
    @DeleteMapping("/users/{id}")
    public Map<String, Object> removeUserById(@PathVariable int id) {
        Map<String, Object> metaMap = new HashMap<>();
        try {
            userService.deleteUser(id);
            metaMap.put("status", 200);
            metaMap.put("msg", "删除用户成功");
        } catch (RuntimeException e) {
            metaMap.put("status", 404);
            metaMap.put("msg", e.getMessage());
        } catch (Exception e) {
            metaMap.put("status", 500);
            metaMap.put("msg", "服务器内部错误");
        }
        // 使用 HashMap 替代 Map.of
        Map<String, Object> response = new HashMap<>();
        response.put("meta", metaMap);
        response.put("data", null); // 允许 null 值
        return response;
    }


//    /**
//     * 9. 获取角色列表接口
//     * 请求路径：GET /roles
//     */
//    @GetMapping("/roles")
//    public List<Role> getRolesList() {
//        return userService.listRoles();
//    }
//
//    /**
//     * 10. 分配角色接口
//     * 请求路径：PUT /users/{userId}/role
//     * 前端传入 JSON 格式的 { rid: 角色ID }
//     */
//    @PutMapping("/users/{userId}/role")
//    public String setRole(@PathVariable int userId, @RequestBody Map<String, Integer> body) {
//        Integer rid = body.get("rid");
//        userService.assignRole(userId, rid);
//        return "角色分配成功";
//    }

    /**
     * 11. 重置密码接口
     * 请求路径：PATCH /user/updatePwd
     * 前端传入 JSON 格式数据，包含 userId 和 newPassword 字段
     */
    @PatchMapping("/user/updatePwd")
    public Map<String, Object> userResetPasswordService(@RequestBody Map<String, String> data) {
        Map<String, Object> metaMap = new HashMap<>();
        try {
            if (data == null || !data.containsKey("userId") || !data.containsKey("newPassword")) {
                metaMap.put("status", 400);
                metaMap.put("msg", "缺少必要的参数：userId 或 newPassword");
                Map<String, Object> response = new HashMap<>();
                response.put("meta", metaMap);
                response.put("data", null);
                return Collections.unmodifiableMap(response);
            }
            String userIdStr = data.get("userId");
            if (userIdStr == null || userIdStr.trim().isEmpty()) {
                metaMap.put("status", 400);
                metaMap.put("msg", "用户ID 不能为空");
                Map<String, Object> response = new HashMap<>();
                response.put("meta", metaMap);
                response.put("data", null);
                return Collections.unmodifiableMap(response);
            }
            int userId;
            try {
                userId = Integer.parseInt(userIdStr);
            } catch (NumberFormatException e) {
                metaMap.put("status", 400);
                metaMap.put("msg", "用户ID 格式错误");
                Map<String, Object> response = new HashMap<>();
                response.put("meta", metaMap);
                response.put("data", null);
                return Collections.unmodifiableMap(response);
            }
            if (userId <= 0) {
                metaMap.put("status", 400);
                metaMap.put("msg", "用户ID 无效");
                Map<String, Object> response = new HashMap<>();
                response.put("meta", metaMap);
                response.put("data", null);
                return Collections.unmodifiableMap(response);
            }
            String newPassword = data.get("newPassword");
            if (newPassword == null || newPassword.trim().isEmpty()) {
                metaMap.put("status", 400);
                metaMap.put("msg", "新密码不能为空");
                Map<String, Object> response = new HashMap<>();
                response.put("meta", metaMap);
                response.put("data", null);
                return Collections.unmodifiableMap(response);
            }
            System.out.println("开始重置密码: userId=" + userId + ", newPassword=" + newPassword);
            userService.resetPassword(userId, newPassword);
            metaMap.put("status", 200);
            metaMap.put("msg", "密码重置成功");
        } catch (IllegalArgumentException e) {
            metaMap.put("status", 400);
            metaMap.put("msg", e.getMessage());
        } catch (RuntimeException e) {
            metaMap.put("status", 404);
            metaMap.put("msg", "用户不存在，请确认用户ID是否正确");
        } catch (Exception e) {
            e.printStackTrace();
            metaMap.put("status", 500);
            metaMap.put("msg", "服务器内部错误");
        }
        Map<String, Object> response = new HashMap<>();
        response.put("meta", metaMap);
        response.put("data", null);
        return Collections.unmodifiableMap(response);
    }

    @GetMapping("/users/total")
    public Map<String, Object> total() {
        HashMap<String,Object> response = new HashMap<>();
        response.put("user_toatl",userService.total());
        return response;
    }


}

