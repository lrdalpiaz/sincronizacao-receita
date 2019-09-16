package br.com.sicredi.sync.tool.balancer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.sicredi.sync.tool.balancer.balancer.BalancerManageable;
import br.com.sicredi.sync.tool.balancer.balancer.BalancerManager;
import br.com.sicredi.sync.tool.balancer.balancer.DefaultDataStructuresProvider;
import br.com.sicredi.sync.tool.balancer.balancer.ProcessDispatcher;
import br.com.sicredi.sync.tool.balancer.balancer.ProcessExecutor;
import br.com.sicredi.sync.tool.balancer.balancer.ProcessRequest;
import br.com.sicredi.sync.tool.balancer.balancer.TaskExecutor;
import br.com.sicredi.sync.tool.client.ReceitaServiceClient;
import br.com.sicredi.sync.tool.domain.Account;

@Configuration
public class SyncAccountBalancerConfiguration {

    @Value("${account.sync.maxConcurrent:20}")
    private int poolSize;
    @Value("${dry:false}")
    private boolean dryRun;

    @Bean
    public DefaultDataStructuresProvider standaloneDataStructuresProvider() {
        return new DefaultDataStructuresProvider();
    }

    @Bean
    public BalancerManageable<Account> balancerManageable(DefaultDataStructuresProvider dataStructuresProvider) {
        return new BalancerManager<>("sync-account-tool", dataStructuresProvider);
    }

    @Bean
    public ProcessDispatcher<Account> processDispatcheable(BalancerManageable<Account> balancerManager) {
        return new ProcessDispatcher<>(balancerManager);
    }

    @Bean
    public ProcessExecutor<Account> processExecutor(BalancerManageable<Account> balancerManager,
            TaskExecutor<Account> taskExecutor) {
        return new ProcessExecutor<>(balancerManager, taskExecutor, "sync-account-tool", poolSize);
    }

    @Bean
    public TaskExecutor<Account> taskExecutor(ReceitaServiceClient receitaServiceClient) {
        return new TaskExecutor<Account>() {

            @Override
            public void execute(ProcessRequest<Account> request) {
                receitaServiceClient.atualizarConta(request.getProcessId(), dryRun);
            }
        };
    }
}
