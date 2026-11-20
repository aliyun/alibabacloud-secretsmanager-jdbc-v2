package com.aliyun.kms.secretsmanager.v2;

import org.junit.Ignore;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import static org.junit.Assert.*;

public class MysqlSecretsManagerDriverTest {

    @Test
    public void testContactUrl() {
        MysqlSecretsManagerDriver driver = new MysqlSecretsManagerDriver();
        assertEquals("jdbc:mysql://host:3306/db", driver.contactUrl("host", "3306", "db"));
        assertEquals("jdbc:mysql://host/db", driver.contactUrl("host", null, "db"));
        assertEquals("jdbc:mysql://host:3306", driver.contactUrl("host", "3306", null));
        assertEquals("jdbc:mysql://host", driver.contactUrl("host", null, null));
    }

    @Test
    public void testGetRealDriverClass() {
        MysqlSecretsManagerDriver driver = new MysqlSecretsManagerDriver();
        assertEquals("com.mysql.cj.jdbc.Driver", driver.getRealDriverClass());
    }

    @Test
    public void testIsAuthError() {
        MysqlSecretsManagerDriver driver = new MysqlSecretsManagerDriver();
        SQLException authError = new SQLException("access denied", "28000", 1045);
        assertTrue(driver.isAuthError(authError));

        SQLException nestedAuthError = new SQLException("nested", new SQLException("cause", "28000", 1045));
        assertTrue(driver.isAuthError(nestedAuthError));

        SQLException otherError = new SQLException("syntax error", "42000", 1064);
        assertFalse(driver.isAuthError(otherError));

        assertFalse(driver.isAuthError(new RuntimeException("other")));
    }

    @Ignore("Requires real KMS and MySQL database")
    @Test
    public void testConnectWithUrl() throws SQLException {
        SecretsManagerDriver driver = new MysqlSecretsManagerDriver();
        Properties properties = new Properties();
        properties.put("user", "#secretName#");
        Connection connect = driver.connect("secrets-manager:mysql://#endpoint#:#port#/#database#?zeroDateTimeBehavior=convertToNull&useUnicode=true&characterEncoding=UTF8", properties);
        assertNotNull(connect);
    }
}