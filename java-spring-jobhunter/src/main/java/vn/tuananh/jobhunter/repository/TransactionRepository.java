package vn.tuananh.jobhunter.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import vn.tuananh.jobhunter.domain.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long>,
                JpaSpecificationExecutor<Transaction> {
    Optional<Transaction> findByPaymentRef(String paymentRef);
}
