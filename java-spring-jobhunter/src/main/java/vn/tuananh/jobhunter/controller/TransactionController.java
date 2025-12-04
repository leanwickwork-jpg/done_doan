package vn.tuananh.jobhunter.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.tuananh.jobhunter.domain.response.ResultPaginationDTO;
import vn.tuananh.jobhunter.service.TransactionService;
import vn.tuananh.jobhunter.util.annotation.ApiMessage;
import vn.tuananh.jobhunter.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/transactions/by-user")
    @ApiMessage("Get list transactions by user")
    public ResponseEntity<ResultPaginationDTO> fetchTransactionsByUser(Pageable pageable) throws IdInvalidException {
        return ResponseEntity.ok().body(transactionService.fetchTransactionsByUser(pageable));
    }
} 