package vn.tuananh.jobhunter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.tuananh.jobhunter.domain.Transaction;
import vn.tuananh.jobhunter.domain.User;
import vn.tuananh.jobhunter.repository.TransactionRepository;
import vn.tuananh.jobhunter.repository.UserRepository;
import vn.tuananh.jobhunter.util.constant.PaymentStatusEnum;
import vn.tuananh.jobhunter.util.constant.TransactionTypeEnum;
import vn.tuananh.jobhunter.util.error.BalanceException;
import vn.tuananh.jobhunter.util.error.IdInvalidException;

import java.util.UUID;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.turkraft.springfilter.builder.FilterBuilder;
import com.turkraft.springfilter.converter.FilterSpecification;
import com.turkraft.springfilter.converter.FilterSpecificationConverter;
import com.turkraft.springfilter.parser.FilterParser;
import com.turkraft.springfilter.parser.node.FilterNode;
import vn.tuananh.jobhunter.domain.response.ResultPaginationDTO;
import vn.tuananh.jobhunter.domain.response.transaction.ResFetchTransactionDTO;
import vn.tuananh.jobhunter.util.SecurityUtil;

@Service
public class TransactionService {

    @Value("${tuananh.fee.apply}")
    private double applyFee;

    @Value("${tuananh.fee.create-post}")
    private double createPostFee;

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    @Autowired
    private FilterParser filterParser;

    @Autowired
    private FilterSpecificationConverter filterSpecificationConverter;

    public TransactionService(UserRepository userRepository, TransactionRepository transactionRepository) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
    }

    public void handleApplyFee(User user) {
        if (user.getBalance() < applyFee) {
            throw new BalanceException("Số dư không đủ để ứng tuyển. Vui lòng nạp thêm tiền.");
        }

        // Tạo transaction ghi nhận phí
        Transaction transaction = new Transaction();
        transaction.setPaymentRef(UUID.randomUUID().toString().replace("-", ""));
        transaction.setAmount(applyFee);
        transaction.setPaymentStatus(PaymentStatusEnum.SUCCESS);
        transaction.setTransactionType(TransactionTypeEnum.APPLY_FEE);
        transaction.setUser(user);

        // Trừ tiền từ tài khoản
        user.setBalance(user.getBalance() - applyFee);
        
        // Lưu transaction và cập nhật user
        transactionRepository.save(transaction);
        userRepository.save(user);
    }

    public void handleCreatePostFee(User user) {
        if (user.getBalance() < createPostFee) {
            throw new BalanceException("Số dư không đủ để đăng bài. Vui lòng nạp thêm tiền.");
        }

        // Tạo transaction ghi nhận phí
        Transaction transaction = new Transaction();
        transaction.setPaymentRef(UUID.randomUUID().toString().replace("-", ""));
        transaction.setAmount(createPostFee);
        transaction.setPaymentStatus(PaymentStatusEnum.SUCCESS);
        transaction.setTransactionType(TransactionTypeEnum.POST_FEE);
        transaction.setUser(user);

        // Trừ tiền từ tài khoản
        user.setBalance(user.getBalance() - createPostFee);
        
        // Lưu transaction và cập nhật user
        transactionRepository.save(transaction);
        userRepository.save(user);
    }

    public ResFetchTransactionDTO getTransaction(Transaction transaction) {
        ResFetchTransactionDTO res = new ResFetchTransactionDTO();
        res.setId(transaction.getId());
        res.setEmail(transaction.getUser().getEmail());
        res.setPaymentRef(transaction.getPaymentRef());
        res.setAmount(transaction.getAmount());
        res.setPaymentStatus(transaction.getPaymentStatus());
        res.setTransactionType(transaction.getTransactionType());
        res.setCreatedAt(transaction.getCreatedAt());
        res.setCreatedBy(transaction.getCreatedBy());
        res.setUpdatedAt(transaction.getUpdatedAt());
        res.setUpdatedBy(transaction.getUpdatedBy());


        res.setUser(new ResFetchTransactionDTO.UserResume(transaction.getUser().getId(), transaction.getUser().getName()));

        return res;
    }

    public ResultPaginationDTO fetchTransactionsByUser(Pageable pageable) throws IdInvalidException {
        String email = SecurityUtil.getCurrentUserLogin().isPresent()
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";
        
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new IdInvalidException("Không tìm thấy người dùng");
        }

        // Tạo filter để lọc theo user.id
        FilterNode node = filterParser.parse("user.id=" + user.getId());
        FilterSpecification<Transaction> spec = filterSpecificationConverter.convert(node);
        Page<Transaction> pageTransaction = transactionRepository.findAll(spec, pageable);

        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();
        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());
        mt.setPages(pageTransaction.getTotalPages());
        mt.setTotal(pageTransaction.getTotalElements());
        rs.setMeta(mt);

        // remove sensitive data
        List<ResFetchTransactionDTO> listTransaction = pageTransaction.getContent()
                .stream().map(item -> this.getTransaction(item))
                .collect(Collectors.toList());

        rs.setResult(listTransaction);
        return rs;
    }
} 