package com.example.graduationproject.domain.repository.remote

import com.example.graduationproject.data.remote.api.request.GroupsRequest
import com.example.graduationproject.data.remote.api.response.GroupResponse
import com.example.graduationproject.domain.entity.Group
import com.example.graduationproject.domain.util.Event
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface GroupRemoteRepository {
    suspend fun fetchGroupsByGroupId(groupIdQuery: String): Event<Group>

    suspend fun insertGroup(groupName: String, creatorId: String, groupAvatarPath: String?): Event<Group>
}