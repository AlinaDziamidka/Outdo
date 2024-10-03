package com.example.graduationproject.data.local.transformer

import com.example.graduationproject.data.local.database.model.GroupModel
import com.example.graduationproject.domain.entity.Group

public class GroupTransformer {
    fun fromModel(model: GroupModel): Group {
        return Group(
            groupId = model.groupId,
            groupName = model.groupName,
            creatorId = model.creatorId,
            groupAvatarPath = model.groupAvatarPath
        )
    }

    fun toModel(group: Group): GroupModel {
        return GroupModel(
            groupId = group.groupId,
            groupName = group.groupName,
            creatorId = group.creatorId,
            groupAvatarPath = group.groupAvatarPath
        )
    }
}