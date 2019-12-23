package cn.saatana.config;

import cn.saatana.module.system.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class InitRunner implements ApplicationRunner {
    private final UserService userService;
    private final DataSource dataSource;
    @Value("classpath:init.sql")
    private Resource initSql;

    @Autowired
    public InitRunner(UserService userService, DataSource dataSource) {
        this.userService = userService;
        this.dataSource = dataSource;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (userService.findAll().size() == 0) {
            ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
            populator.addScript(initSql);
            populator.setSeparator("$$");
            DatabasePopulatorUtils.execute(populator, dataSource);
            System.out.println("init runner running");
        }
    }
}
