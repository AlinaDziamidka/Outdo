package com.example.graduationproject.data.transormer

import com.example.graduationproject.data.remote.api.response.GroupResponse
import com.example.graduationproject.domain.entity.Group

public class GroupTransformer {
    fun fromResponse(response: GroupResponse): Group {
        return Group(
            groupId = response.groupId,
            groupName = response.groupName,
            creatorId = response.creatorId,
            groupAvatarPath = response.groupAvatarPath
        )
    }
}