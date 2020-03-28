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

package org.wso2.carbon.extension.custom.mutual.tls.handlers.internal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.wso2.carbon.extension.custom.mutual.tls.handlers.introspection.ISIntrospectionDataProvider;
import org.wso2.carbon.extension.custom.mutual.tls.handlers.introspection.IntrospectionResponseInterceptor;
import org.wso2.carbon.identity.oauth.event.OAuthEventInterceptor;
import org.wso2.carbon.identity.oauth2.IntrospectionDataProvider;

/**
 * TLS Mutual Auth osgi Component.
 */
@Component(
        name = "org.wso2.carbon.extension.custom.mutual.tls.handlers",
        immediate = true
)
public class MutualTLSHandlerServiceComponent {

    private static Log log = LogFactory.getLog(MutualTLSHandlerServiceComponent.class);
    @Activate
    protected void activate(ComponentContext context) {

        BundleContext bundleContext = context.getBundleContext();
        try {
            bundleContext.registerService(OAuthEventInterceptor.class, new IntrospectionResponseInterceptor(), null);
            bundleContext.registerService(IntrospectionDataProvider.class, new ISIntrospectionDataProvider(), null);
            if (log.isDebugEnabled()) {
                log.debug("Handler component is activated successfully.");
            }
        } catch (Throwable e) {
            log.error("Error occurred while activating Handler Extensions component", e);
        }
    }
}
