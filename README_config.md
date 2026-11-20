# Alibaba Cloud secrets manager client v2 profile settings 

Build the client credentials with the configuration file (secretsmanager.properties) in the directory where the program runs:

1. Use ECS RAM role to access Aliyun KMS, you must set the following configuration variables

```properties
# the type of access credentials
credentials_type=ecs_ram_role
# ECS RAM Role name
credentials_role_name=<your role name>
# the region information
cache_client_region_id=[{"regionId":"<regionId>"}]
# When accessing KMS instance gateway, use the following configuration
# cache_client_region_id=[{"regionId":"<regionId>","endpoint":"<you kms instanceId>.cryptoservice.kms.aliyuncs.com"}]
```

2. Use OIDC Role ARN to access Aliyun KMS, you must set the following configuration variables

```properties
# the type of access credentials
credentials_type=oidc_role_arn
# role arn (optional; defaults to env ALIBABA_CLOUD_ROLE_ARN)
credentials_role_arn=<role arn>
# OIDC provider arn (optional; defaults to env ALIBABA_CLOUD_OIDC_PROVIDER_ARN)
credentials_oidc_provider_arn=<oidc provider arn>
# OIDC token file path (optional; defaults to env ALIBABA_CLOUD_OIDC_TOKEN_FILE)
credentials_oidc_token_file_path=<oidc token file path>
# the region information
cache_client_region_id=[{"regionId":"<regionId>"}]
# When accessing KMS instance gateway, use the following configuration
# cache_client_region_id=[{"regionId":"<regionId>","endpoint":"<you kms instanceId>.cryptoservice.kms.aliyuncs.com"}]
```

The three fields are optional only when the corresponding environment variable is set:
`credentials_role_arn` ↔ `ALIBABA_CLOUD_ROLE_ARN`, `credentials_oidc_provider_arn` ↔
`ALIBABA_CLOUD_OIDC_PROVIDER_ARN`, `credentials_oidc_token_file_path` ↔
`ALIBABA_CLOUD_OIDC_TOKEN_FILE`. If a value is absent from both the configuration file and the
environment, client construction fails with `IllegalArgumentException` (for example,
`RoleArn or environment variable ALIBABA_CLOUD_ROLE_ARN cannot be empty.`).

On ACK, these three environment variables are not provided automatically: the standard RRSA
setup adds them to the Pod spec together with a projected ServiceAccount token. If you do not
inject them there, fill in all three fields above in `secretsmanager.properties`.

3. Use Aliyun AK SK to access Aliyun KMS, you must set the following configuration variables

```properties
# the type of access credentials
credentials_type=ak
# AK
credentials_access_key_id=<access key id>
# SK
credentials_access_secret=<access key secret>
# the region information
cache_client_region_id=[{"regionId":"<regionId>"}]
# When accessing KMS instance gateway, use the following configuration
# cache_client_region_id=[{"regionId":"<regionId>","endpoint":"<you kms instanceId>.cryptoservice.kms.aliyuncs.com"}]
```

4. Use the default Alibaba Cloud credential chain

If `credentials_type` is not set in `secretsmanager.properties` (or the file does not exist),
the client resolves credentials through the default Alibaba Cloud credential chain (environment
variables such as `ALIBABA_CLOUD_ACCESS_KEY_ID` / `ALIBABA_CLOUD_ACCESS_KEY_SECRET`, instance RAM
role, etc.). Region information is still required:

```properties
# no credentials_type (default Alibaba Cloud credential chain)
cache_client_region_id=[{"regionId":"<regionId>"}]
```

5. Customize the secret refresh interval (optional)

```properties
# Refresh interval of the cached secret value, in milliseconds.
# Default: 6 hours (21600000). Minimum: 5 minutes (300000); values <= 0 use the default.
refresh_secret_ttl=21600000
```

When the secret has a rotation interval configured in Secrets Manager, the server-side
rotation interval takes precedence over `refresh_secret_ttl`.