package org.csu.gameshopms.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.csu.gameshopms.domian.Merchant;
import org.csu.gameshopms.persistence.MerchantMapper;
import org.csu.gameshopms.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class MerchantService {
    @Autowired
    private MerchantMapper merchantMapper;

    public Map<String, Object> login(String merchantname, String password) {
        QueryWrapper<Merchant> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("merchantname", merchantname);
        Merchant merchant = merchantMapper.selectOne(queryWrapper);

        if (merchant == null) {
            throw new IllegalArgumentException("商家名称不存在");
        }

        if (!password.equals(merchant.getPassword())) { // 直接比较明文密码
            throw new IllegalArgumentException("密码错误");
        }

        String token = JwtUtil.generateToken(merchant.getId());
        Map<String, Object> result = new HashMap<>();
        result.put("merchant", merchant);
        result.put("token", token);
        return result;
    }

    public Merchant register(Merchant merchant) {
        QueryWrapper<Merchant> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("merchantname", merchant.getMerchantname());
        if (merchantMapper.selectOne(queryWrapper) != null) {
            throw new IllegalArgumentException("商家名称已存在");
        }

        merchantMapper.insert(merchant); // 直接存储明文密码
        return merchant;
    }

    public Merchant getMerchantInfo(int id) {
        Merchant merchant = merchantMapper.selectById(id);
        if (merchant == null) {
            throw new IllegalArgumentException("商家不存在");
        }
        return merchant;
    }

    public int updateMerchantInfo(Merchant merchant) {
        Merchant existingMerchant = merchantMapper.selectById(merchant.getId());
        if (existingMerchant == null) {
            throw new IllegalArgumentException("商家不存在");
        }
        return merchantMapper.updateById(merchant);
    }
}