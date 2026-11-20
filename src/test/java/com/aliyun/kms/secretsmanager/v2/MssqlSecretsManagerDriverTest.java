package com.aliyun.kms.secretsmanager.v2;

import com.aliyun.kms.secretsmanager.v2.service.SecretsManagerDriverRefreshStrategy;
import com.aliyuncs.kms.secretsmanager.client.v2.SecretCacheClient;
import com.aliyuncs.kms.secretsmanager.client.v2.SecretCacheClientBuilder;
import com.aliyuncs.kms.secretsmanager.client.v2.cache.FileCacheSecretStoreStrategy;
import com.aliyuncs.kms.secretsmanager.client.v2.exception.CacheSecretException;
import com.aliyuncs.kms.secretsmanager.client.v2.service.DefaultSecretManagerClientBuilder;
import com.aliyuncs.kms.secretsmanager.client.v2.service.FullJitterBackoffStrategy;
import com.aliyuncs.kms.secretsmanager.client.v2.utils.CacheClientConstant;
import com.aliyuncs.kms.secretsmanager.client.v2.utils.CredentialsProviderUtils;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import static org.junit.Assert.*;

/**
 * Unit test for simple App.
 */
public class MssqlSecretsManagerDriverTest {

    @Test
    public void testContactUrl() {
        MssqlSecretsManagerDriver driver = new MssqlSecretsManagerDriver();
        assertEquals("jdbc:sqlserver://host:1433;DatabaseName=db", driver.contactUrl("host", "1433", "db"));
        assertEquals("jdbc:sqlserver://host;DatabaseName=db", driver.contactUrl("host", null, "db"));
        assertEquals("jdbc:sqlserver://host:1433", driver.contactUrl("host", "1433", null));
        assertEquals("jdbc:sqlserver://host", driver.contactUrl("host", null, null));
    }

    @Test
    public void testGetRealDriverClass() {
        MssqlSecretsManagerDriver driver = new MssqlSecretsManagerDriver();
        assertEquals("com.microsoft.sqlserver.jdbc.SQLServerDriver", driver.getRealDriverClass());
    }

    @Test
    public void testIsAuthError() {
        MssqlSecretsManagerDriver driver = new MssqlSecretsManagerDriver();
        SQLException authError = new SQLException("login failed", "28000", 18456);
        assertTrue(driver.isAuthError(authError));

        SQLException otherError = new SQLException("syntax error", "42000", 102);
        assertFalse(driver.isAuthError(otherError));

        assertFalse(driver.isAuthError(new RuntimeException("other")));
    }

    @Ignore("Requires real KMS and MSSQL database")
    @Test
    public void testConnectWithUrlCustomCacheClient() throws SQLException, CacheSecretException, IOException {
        String secretName = "MssqlRdsTest";
        SecretCacheClient client = SecretCacheClientBuilder.newCacheClientBuilder(DefaultSecretManagerClientBuilder.standard().withCredentialsProvider(CredentialsProviderUtils.withAccessKey("<your-access-key>", "<your-secret>"))
                .withRegion("cn-hangzhou").withBackoffStrategy(new FullJitterBackoffStrategy(3, 2000, 10000)).build())
                .withCacheSecretStrategy(new FileCacheSecretStoreStrategy("secrets", true, "1234abcd")).withRefreshSecretStrategy(new SecretsManagerDriverRefreshStrategy()).withSecretTTL(secretName, 60 * 1000l).withCacheStage(CacheClientConstant.STAGE_ACS_CURRENT).build();
        SecretsManagerDriver driver = new MssqlSecretsManagerDriver(client);
        Properties properties = new Properties();
        properties.put("user", secretName);
        Connection connect = driver.connect("secrets-manager:sqlserver://<your-db-url>:3433;DatabaseName=test_data_base", properties);
        assertNotNull(connect);
    }
}
