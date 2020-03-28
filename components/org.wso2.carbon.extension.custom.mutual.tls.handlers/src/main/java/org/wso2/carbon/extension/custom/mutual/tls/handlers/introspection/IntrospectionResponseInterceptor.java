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

package org.wso2.carbon.extension.custom.mutual.tls.handlers.introspection;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import org.wso2.carbon.identity.oauth.event.AbstractOAuthEventInterceptor;
import org.wso2.carbon.identity.oauth2.IdentityOAuth2Exception;
import org.wso2.carbon.identity.oauth2.dto.OAuth2IntrospectionResponseDTO;
import org.wso2.carbon.identity.oauth2.dto.OAuth2TokenValidationRequestDTO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is used to modify the token introspection response.
 */
public class IntrospectionResponseInterceptor extends AbstractOAuthEventInterceptor {

    private static final String CERT_THUMBPRINT = "x5t";
    private static final String CERT_THUMBPRINT_SEPARATOR = ":";
    private static final String CONFIRMATION_CLAIM_ATTRIBUTE = "cnf";

    private static Log log = LogFactory.getLog(IntrospectionResponseInterceptor.class);

    @Override
    public void onPostTokenValidation(OAuth2TokenValidationRequestDTO oAuth2TokenValidationRequestDTO,
                                      OAuth2IntrospectionResponseDTO oAuth2IntrospectionResponseDTO, Map<String,
            Object> params) throws IdentityOAuth2Exception {

        /*
         Omit the cert thumbprint scope (added by MTLSTokenBindingAuthorizationCodeGrantHandler) from the scopes list
         and add as a separate parameter in the introspection response as specified under
         https://tools.ietf.org/html/draft-ietf-oauth-mtls-17.
        */
        JSONObject cnf = null;
        String scopeString = oAuth2IntrospectionResponseDTO.getScope();
        if (StringUtils.isNotEmpty(scopeString)) {
            String[] scopeArray = scopeString.trim().split("\\s+");
            List<String> scopeList = new ArrayList<>(Arrays.asList(scopeArray));
            List<String> removableScopes = new ArrayList<>(); // to avoid concurrent modification exceptions.

            // Iterate the scope list and remove any internal scopes.
            for (String scope : scopeList) {
                if (scope.startsWith(CERT_THUMBPRINT)) {
                    String[] certHashScope = scope.split(CERT_THUMBPRINT_SEPARATOR, 2);
                    cnf = new JSONObject();
                    cnf.put(certHashScope[0].trim(), certHashScope[1].trim());

                    removableScopes.add(scope);
                    if (log.isDebugEnabled()) {
                        log.debug("Removing the internal scope " + scope + " from introspection response");
                    }
                }
            }
            scopeList.removeAll(removableScopes);
            oAuth2IntrospectionResponseDTO.setScope(String.join(" ", scopeList));
        }

        Map<String, Object> introspectionResponseProperties = oAuth2IntrospectionResponseDTO.getProperties();
        if (introspectionResponseProperties == null) {
            introspectionResponseProperties = new HashMap<>();
        }

        // If the MTLS cert hash is present as a scope, add the cert hash under cnf parameter.
        if (cnf != null) {
            introspectionResponseProperties.put(CONFIRMATION_CLAIM_ATTRIBUTE, cnf);
        }
        oAuth2IntrospectionResponseDTO.setProperties(introspectionResponseProperties);
    }
}
