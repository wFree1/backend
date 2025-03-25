package org.csu.gameshopms.persistence;

import org.csu.gameshopms.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order,Long> {

}
