package com.aliyun.kms.secretsmanager.v2;

import com.aliyun.kms.secretsmanager.v2.utils.Constants;
import com.aliyun.tea.utils.StringUtils;
import com.aliyuncs.kms.secretsmanager.client.v2.SecretCacheClient;
import com.aliyuncs.kms.secretsmanager.client.v2.service.UserAgentManager;

import java.sql.SQLException;


public class MssqlSecretsManagerDriver extends SecretsManagerDriver {

    public static final int LOGIN_FAILED_CODE = 18456;

    static {
        UserAgentManager.registerUserAgent(Constants.MSSQL_DRIVER_JAVA_OF_USER_AGENT, Constants.MSSQL_DRIVER_JAVA_OF_USER_AGENT_PRIORITY, Constants.PROJECT_VERSION);
    }

    protected MssqlSecretsManagerDriver() {
        super();
    }

    public MssqlSecretsManagerDriver(SecretCacheClient secretCacheClient) {
        super(secretCacheClient);
    }

    public MssqlSecretsManagerDriver(SecretCacheClient secretCacheClient, String urlUnique) {
        super(secretCacheClient, urlUnique);
    }

    @Override
    protected String getRealDriverClass() {
        return "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    }


    @Override
    public String contactUrl(String endpoint, String port, String dbName) {
        String url = "jdbc:sqlserver://" + endpoint;
        if (!StringUtils.isEmpty(port)) {
            url += ":" + port;
        }
        if (!StringUtils.isEmpty(dbName)) {
            url += ";DatabaseName=" + dbName;
        }
        return url;
    }

    @Override
    protected boolean isAuthError(Exception e) {
        if (e instanceof SQLException) {
            SQLException sqle = (SQLException) e;
            int errorCode = sqle.getErrorCode();
            return errorCode == LOGIN_FAILED_CODE;
        }
        return false;
    }

}

