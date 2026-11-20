package com.aliyun.kms.secretsmanager.v2;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.junit.Ignore;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

public class C3p0DataSourceTest {
    @Ignore("Requires real database configuration")
    @Test
    public void testC3p0DataSource() throws SQLException {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        Connection connection = dataSource.getConnection();
        System.out.println(connection);
    }
}
