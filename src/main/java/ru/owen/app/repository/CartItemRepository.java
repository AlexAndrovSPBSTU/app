package ru.owen.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.owen.app.model.Customer;
import ru.owen.app.model.Cart.CartItem;
import ru.owen.app.model.Cart.CartItemId;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, CartItemId> {
    List<CartItem> findAllByCustomer(Customer customer);

}
