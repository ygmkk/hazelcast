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

package com.hazelcast.internal.cluster.impl.operations;

import com.hazelcast.internal.cluster.impl.ClusterDataSerializerHook;
import com.hazelcast.internal.cluster.impl.ClusterServiceImpl;
import com.hazelcast.internal.cluster.impl.MembersView;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;

import java.io.IOException;

/**
 * An operation sent by the member that starts mastership claim process to fetch/gather member views of other members.
 * Collected member views will be used to decide final, most recent member list.
 *
 * @since 3.9
 */
public class FetchMembersViewOp extends AbstractClusterOperation implements JoinOperation {

    private String targetUuid;
    private MembersView membersView;

    public FetchMembersViewOp() {
    }

    public FetchMembersViewOp(String targetUuid) {
        this.targetUuid = targetUuid;
    }

    @Override
    public void run() throws Exception {
        ClusterServiceImpl service = getService();
        String thisUuid = service.getLocalMember().getUuid();
        if (!targetUuid.equals(thisUuid)) {
            throw new IllegalStateException("Rejecting mastership claim, since target uuid[" + targetUuid
                    + "] is not matching local member uuid[" + thisUuid + "].");
        }
        membersView = service.handleMastershipClaim(getCallerAddress(), getCallerUuid());
    }

    @Override
    public boolean returnsResponse() {
        return true;
    }

    @Override
    public Object getResponse() {
        return membersView;
    }

    @Override
    public int getId() {
        return ClusterDataSerializerHook.FETCH_MEMBER_LIST_STATE;
    }

    @Override
    protected void writeInternal(ObjectDataOutput out) throws IOException {
        out.writeUTF(targetUuid);
    }

    @Override
    protected void readInternal(ObjectDataInput in) throws IOException {
        targetUuid = in.readUTF();
    }
}
