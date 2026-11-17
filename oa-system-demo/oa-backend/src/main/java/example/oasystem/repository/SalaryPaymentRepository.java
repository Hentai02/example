package example.oasystem.repository;

import example.oasystem.entries.SalaryPayment;
import org.springframework.data.repository.CrudRepository;

public interface SalaryPaymentRepository extends CrudRepository<SalaryPayment, Integer> {
    
}
