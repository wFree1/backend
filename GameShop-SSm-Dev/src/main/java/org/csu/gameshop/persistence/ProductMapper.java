package org.csu.gameshop.persistence;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.csu.gameshop.entity.Product;
import org.springframework.stereotype.Repository;

@Repository()
public interface ProductMapper extends BaseMapper<Product> {
}
