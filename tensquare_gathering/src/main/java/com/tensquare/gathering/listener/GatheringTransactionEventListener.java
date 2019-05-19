package com.tensquare.gathering.listener;

import com.tensquare.gathering.pojo.Gathering;
import org.springframework.context.PayloadApplicationEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class GatheringTransactionEventListener {
    // 指定目标方法在事务commit之前执行
    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void beforeCommit(PayloadApplicationEvent<Gathering> event) {
        System.out.println("事务提交之前执行: " + event.getPayload().getId());
    }

    // 指定目标方法在事务commit之后执行
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void afterCommit(PayloadApplicationEvent<Gathering> event) {
        System.out.println("事务提交之后执行: " + event.getPayload().getId());
    }

    // 指定目标方法在事务完成时执行，这里的完成是指无论事务是成功提交还是事务回滚了
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMPLETION)
    public void afterCompletion(PayloadApplicationEvent<Gathering> event) {
        System.out.println("事务完成之后执行: " + event.getPayload().getId());
    }

    // 指定目标方法在事务rollback之后执行
    @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
    public void afterRollback(PayloadApplicationEvent<Gathering> event) {
        System.out.println("事务回滚之后执行: " + event.getPayload().getId());
    }

}
