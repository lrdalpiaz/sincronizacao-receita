package br.com.sicredi.sync.tool;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.Banner.Mode;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import br.com.sicredi.sync.tool.balancer.SyncAccountBalancerConfiguration;

@Configuration
@SpringBootApplication
@Import(SyncAccountBalancerConfiguration.class)
public class Main implements ApplicationRunner {

    @Autowired
    private SyncAccountToolApplication starter;
    
    public static void main(String[] args) {
        new SpringApplicationBuilder().headless(false).web(false).sources(Main.class).bannerMode(Mode.OFF).run(args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        starter.start();
    }
}

