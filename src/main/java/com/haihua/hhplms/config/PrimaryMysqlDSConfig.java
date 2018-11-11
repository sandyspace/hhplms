package com.haihua.hhplms.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = "com.haihua.hhplms.*.mapper", sqlSessionTemplateRef = "primaryMysqlSqlSessionTemplate")
public class PrimaryMysqlDSConfig {
    @Value("${primary.mysql.jdbc.url}")
    private String jdbcUrl;
    @Value("${primary.mysql.jdbc.username}")
    private String username;
    @Value("${primary.mysql.jdbc.password}")
    private String password;
    @Value("${primary.mysql.jdbc.driverClassName}")
    private String driverClassName;

    @Bean(name = "primaryMysqlDataSource")
    @Primary
    public DataSource primaryMysqlDataSource() {
        DruidDataSource primaryMysqlDataSource = new DruidDataSource();
        primaryMysqlDataSource.setDriverClassName(driverClassName);
        primaryMysqlDataSource.setUrl(jdbcUrl);
        primaryMysqlDataSource.setUsername(username);
        primaryMysqlDataSource.setPassword(password);
        return primaryMysqlDataSource;
    }

    @Bean(name = "primaryMysqlSqlSessionFactory")
    @Primary
    public SqlSessionFactory primaryMysqlSqlSessionFactory(@Qualifier("primaryMysqlDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean primaryMysqlSqlSessionFactory = new SqlSessionFactoryBean();
        primaryMysqlSqlSessionFactory.setDataSource(dataSource);
        primaryMysqlSqlSessionFactory.setConfigLocation(new PathMatchingResourcePatternResolver().getResource("classpath:mybatis/config/configuration.xml"));
        primaryMysqlSqlSessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mybatis/mapper/**/*.xml"));
        return primaryMysqlSqlSessionFactory.getObject();
    }

    @Bean(name = "primaryMysqlTransactionManager")
    @Primary
    public DataSourceTransactionManager primaryMysqlTransactionManager(@Qualifier("primaryMysqlDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "primaryMysqlSqlSessionTemplate")
    @Primary
    public SqlSessionTemplate primaryMysqlSqlSessionTemplate(@Qualifier("primaryMysqlSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
