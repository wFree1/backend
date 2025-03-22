package org.csu.gameshop.persistence;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.csu.gameshop.entity.Category;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryMapper extends BaseMapper<Category> {
}
