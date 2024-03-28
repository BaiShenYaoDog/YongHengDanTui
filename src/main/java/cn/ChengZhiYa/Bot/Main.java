package cn.ChengZhiYa.Bot;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.TimeZone;

@SpringBootApplication
public class Main {
    public static Statement statement;
    public static HikariDataSource dataSource;
    public static void main(String[] args) {
        try {
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/plugin_verify?autoReconnect=true&serverTimezone=" + TimeZone.getDefault().getID());
            config.setUsername("root");
            config.setPassword("root");
            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("prepStmtCacheSize", "250");
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
            dataSource = new HikariDataSource(config);
            statement = dataSource.getConnection().createStatement();
        } catch (SQLException ignored) {
            System.out.println("无法连接数据库!");
        }

        SpringApplication.run(Main.class, args);
    }
}
