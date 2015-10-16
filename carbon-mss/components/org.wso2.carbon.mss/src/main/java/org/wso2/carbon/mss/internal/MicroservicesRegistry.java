/*
 *  Copyright (c) WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */
package org.wso2.carbon.mss.internal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wso2.carbon.mss.internal.router.HttpResourceHandler;
import org.wso2.carbon.mss.internal.router.Interceptor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * MicroservicesRegistry for the MSS component
 */
public class MicroservicesRegistry {

    private static final Logger LOG = LoggerFactory.getLogger(MicroservicesRegistry.class);
    private static MicroservicesRegistry instance = new MicroservicesRegistry();
    private volatile Set<Object> httpServices = new HashSet<>();
    private List<Interceptor> interceptors = new ArrayList<>();
    private HttpResourceHandler httpResourceHandler =
            new HttpResourceHandler(Collections.emptyList(), interceptors, null, null);

    private MicroservicesRegistry() {
    }

    public static MicroservicesRegistry getInstance() {
        return instance;
    }

    public void addHttpService(Object httpHandler) {
        httpServices.add(httpHandler);
        updateHttpResourceHandler();
        LOG.info("Added HTTP Service: " + httpHandler);
    }

    public void removeHttpService(Object httpService) {
        httpServices.remove(httpService);
        updateHttpResourceHandler();
    }

    public HttpResourceHandler getHttpResourceHandler() {
        return httpResourceHandler;
    }

    public void addInterceptor(Interceptor interceptor) {
        interceptors.add(interceptor);
    }

    private void updateHttpResourceHandler() {
        httpResourceHandler =
                new HttpResourceHandler(Collections.unmodifiableSet(httpServices), interceptors, null, null);
    }

    Set<Object> getHttpServices() {
        return Collections.unmodifiableSet(httpServices);
    }
}
