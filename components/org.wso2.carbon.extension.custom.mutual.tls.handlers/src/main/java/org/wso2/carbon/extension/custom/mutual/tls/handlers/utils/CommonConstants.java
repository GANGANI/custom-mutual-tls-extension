/*
 * Copyright (c) 2020, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.extension.custom.mutual.tls.handlers.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Define common constants.
 */
public class CommonConstants {

    public static final String CERT_THUMBPRINT = "x5t";
    public static final String IS_SCOPE_PREFIX = "IS_";
    public static final String TIMESTAMP_SCOPE_PREFIX = "TIME_";


    public static final String CERT_THUMBPRINT_SEPERATOR = ":";
    public static final String SHA256_DIGEST_ALGORITHM = "SHA256";
    public static final String AUTHENTICATOR_TYPE_PARAM = "authenticatorType";
    public static final String AUTHENTICATOR_TYPE_MTLS = "mtls";
    public static final String BEGIN_CERT = "-----BEGIN CERTIFICATE-----";
    public static final String END_CERT = "-----END CERTIFICATE-----";
    public static final String MTLS_AUTH_HEADER = "MTLS.ClientAuthenticationHeader";
    public static Map<String, Object> configuration = new HashMap<>();

}
