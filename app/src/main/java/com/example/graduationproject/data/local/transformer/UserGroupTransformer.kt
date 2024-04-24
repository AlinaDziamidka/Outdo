package com.example.graduationproject.data.local.transformer

import com.example.graduationproject.data.local.database.model.GroupModel
import com.example.graduationproject.data.local.database.model.UserGroupModel
import com.example.graduationproject.domain.entity.Group
import com.example.graduationproject.domain.entity.UserGroup

public class UserGroupTransformer {
    fun fromModel(model: UserGroupModel): UserGroup {
        return UserGroup(
            groupId = model.groupId,
            userId = model.userId
        )
    }

    fun toModel(userGroup: UserGroup): UserGroupModel {
        return UserGroupModel(
            groupId = userGroup.groupId,
            userId = userGroup.userId
        )
    }
}