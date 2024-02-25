package ru.owen.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.owen.app.model.Mutual.Customer;
import ru.owen.app.model.Mutual.Invoice;

import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, String> {
    List<Invoice> findAllByCustomer(Customer customer);
}
