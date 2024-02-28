package com.example.graduationproject.data.transormer

import com.example.graduationproject.data.remote.api.response.UserGroupsResponse
import com.example.graduationproject.domain.entity.UserGroup

public class UserGroupTransformer {

    fun fromResponse(response: UserGroupsResponse): UserGroup {
        return UserGroup(
            groupId = response.groupId,
            userId = response.userId
        )
    }
}
