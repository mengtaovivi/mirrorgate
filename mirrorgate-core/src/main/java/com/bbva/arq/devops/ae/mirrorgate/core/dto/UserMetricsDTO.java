/*
 * Copyright 2017 Banco Bilbao Vizcaya Argentaria, S.A.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bbva.arq.devops.ae.mirrorgate.core.dto;

import javax.validation.constraints.NotNull;

/**
 * Created by alfonso on 27/07/17.
 */
public class UserMetricsDTO {

    @NotNull
    private String viewId;

    private Long rtActiveUsers;

    private Long ga7dayUsers;

    public String getViewId() {
        return viewId;
    }

    public UserMetricsDTO setViewId(String viewId) {
        this.viewId = viewId;
        return this;
    }

    public Long getRtActiveUsers() {
        return rtActiveUsers;
    }

    public UserMetricsDTO setRtActiveUsers(Long rtActiveUsers) {
        this.rtActiveUsers = rtActiveUsers;
        return this;
    }

    public Long getGa7dayUsers() {
        return ga7dayUsers;
    }

    public UserMetricsDTO setGa7dayUsers(Long ga7dayUsers) {
        this.ga7dayUsers = ga7dayUsers;
        return this;
    }
}
