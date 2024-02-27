package com.example.graduationproject.data.transormer
import com.example.graduationproject.data.remote.api.response.GroupResponse
import com.example.graduationproject.domain.entity.Group
import com.example.graduationproject.domain.entity.UserGroup

public class GroupTransformer {
    fun fromResponse(response: GroupResponse): Group {
        return Group(
            groupName = response.groupName,
            creatorId = response.creatorId,
            groupAvatarPath = response.groupAvatarPath
        )
    }
}