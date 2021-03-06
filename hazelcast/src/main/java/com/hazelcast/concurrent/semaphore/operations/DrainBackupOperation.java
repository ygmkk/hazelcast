/*
 * Copyright (c) 2008-2017, Hazelcast, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hazelcast.concurrent.semaphore.operations;

import com.hazelcast.concurrent.semaphore.SemaphoreContainer;
import com.hazelcast.concurrent.semaphore.SemaphoreDataSerializerHook;

public class DrainBackupOperation extends SemaphoreBackupOperation {

    public DrainBackupOperation() {
    }

    public DrainBackupOperation(String name, int permitCount, String firstCaller) {
        super(name, permitCount, firstCaller);
    }

    @Override
    public void run() throws Exception {
        SemaphoreContainer semaphoreContainer = getSemaphoreContainer();
        semaphoreContainer.drain(firstCaller);
        response = true;
    }

    @Override
    public int getId() {
        return SemaphoreDataSerializerHook.DRAIN_BACKUP_OPERATION;
    }
}
