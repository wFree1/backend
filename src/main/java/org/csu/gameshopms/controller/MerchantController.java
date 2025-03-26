package org.csu.gameshopms.controller;

import org.csu.gameshopms.entity.Merchant;
import org.csu.gameshopms.service.MerchantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class MerchantController {
    @Autowired
    private MerchantService merchantService;

    @PostMapping("/user/login")
    public Map<String, Object> login(@RequestParam Map<String, String> loginData) {
        Map<String, Object> response = new HashMap<>();
        try {
            String merchantname = loginData.get("merchantname");
            String password = loginData.get("password");

            if (merchantname == null || password == null) {
                response.put("code", 400);
                response.put("message", "商家名称或密码不能为空");
                return response;
            }

            Map<String, Object> result = merchantService.login(merchantname, password);
            response.put("code", 200);
            response.put("message", "登录成功");
            response.put("data", result.get("merchant"));
            response.put("token", result.get("token"));
        } catch (IllegalArgumentException e) {
            response.put("code", 401);
            response.put("message", e.getMessage());
        } catch (Exception e) {
            response.put("code", 500);
            response.put("message", "服务器错误：" + e.getMessage());
            e.printStackTrace();
        }
        return response;
    }

    @PostMapping("/user/register")
    public Map<String, Object> registerUser(@RequestParam Map<String, String> registerData) {
        Map<String, Object> response = new HashMap<>();
        try {
            Merchant merchant = new Merchant();
            merchant.setMerchantname(registerData.get("merchantname"));
            merchant.setPassword(registerData.get("password"));
            merchant.setEmail(registerData.get("email"));

            merchantService.register(merchant);
            response.put("message", "注册成功");
            response.put("success", true);
        } catch (IllegalArgumentException e) {
            response.put("message", e.getMessage());
            response.put("success", false);
        } catch (Exception e) {
            response.put("message", "服务器错误：" + e.getMessage());
            response.put("success", false);
            e.printStackTrace();
        }
        return response;
    }

    @GetMapping("/user/getUserInfo")
    public Map<String, Object> getUserInfo(@RequestParam(value = "id", required = false) String id) {
        Map<String, Object> response = new HashMap<>();
        try {
            if (id == null || id.trim().isEmpty()) {
                response.put("message", "缺少 id 参数");
                response.put("success", false);
                return response;
            }

            int merchantId;
            try {
                merchantId = Integer.parseInt(id);
            } catch (NumberFormatException e) {
                response.put("message", "id 参数必须是有效的整数");
                response.put("success", false);
                return response;
            }

            Merchant merchant = merchantService.getMerchantInfo(merchantId);
            response.put("data", merchant);
            response.put("success", true);
        } catch (IllegalArgumentException e) {
            response.put("message", e.getMessage());
            response.put("success", false);
        } catch (Exception e) {
            response.put("message", "服务器错误：" + e.getMessage());
            response.put("success", false);
            e.printStackTrace();
        }
        return response;
    }
    @PostMapping("/users/updateInfo")
    public Map<String, Object> updateUserInfo(@RequestBody Merchant merchant) {
        Map<String, Object> response = new HashMap<>();
        try {
            int result = merchantService.updateMerchantInfo(merchant);
            if (result > 0) {
                response.put("message", "更新商家信息成功");
                response.put("success", true);
            } else {
                response.put("message", "更新失败，可能是数据未发生变化");
                response.put("success", false);
            }
        } catch (IllegalArgumentException e) {
            response.put("message", e.getMessage());
            response.put("success", false);
        } catch (Exception e) {
            response.put("message", "服务器错误：" + e.getMessage());
            response.put("success", false);
            e.printStackTrace();
        }
        return response;
    }

    @PostMapping("/user/updatePwd")
    public Map<String, Object> changePwd(@RequestBody Map<String, String> data) {
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
            System.out.println("开始修改密码: userId=" + userId + ", newPassword=" + newPassword);
            merchantService.updatePassword(userId, newPassword);
            metaMap.put("status", 200);
            metaMap.put("msg", "密码修改成功");
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

    @GetMapping("/user/name")
    public Map<String, Object> getUserName(@RequestParam(value = "id", required = false) String id) {
        Map<String, Object> response = new HashMap<>();
        response.put("name", merchantService.getName(id));
        return response;
    }
}