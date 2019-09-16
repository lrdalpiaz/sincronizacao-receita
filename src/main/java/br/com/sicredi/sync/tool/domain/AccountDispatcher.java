package br.com.sicredi.sync.tool.domain;

import java.time.Instant;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.sicredi.sync.tool.balancer.balancer.ProcessDispatcher;
import br.com.sicredi.sync.tool.balancer.balancer.ProcessRequest;

@Component
public class AccountDispatcher {
    private final AtomicInteger accountCounter = new AtomicInteger();
    private final ProcessDispatcher<Account> dispatcher;
    
    @Autowired
    public AccountDispatcher(ProcessDispatcher<Account> dispatcher) {
        this.dispatcher = dispatcher;
    }
    
    public void dispatch(Account account) {
        dispatcher.dispatch(new ProcessRequest<Account>(account, Date.from(Instant.now())));
        accountCounter.incrementAndGet();        
    }

    public int accountsDispatched() {
        return accountCounter.get();
    }

}
