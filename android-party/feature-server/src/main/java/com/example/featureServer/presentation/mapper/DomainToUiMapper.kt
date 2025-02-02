package com.example.featureServer.presentation.mapper

import com.example.core.mapper.Mapper
import com.example.domainServer.domain.model.Server
import com.example.featureServer.presentation.model.ServerUiModel

class DomainToUiMapper : Mapper<Server, ServerUiModel> {
    companion object {
        private const val KILOMETER = "km"
    }

    override fun map(from: Server): ServerUiModel {
        return ServerUiModel(
            serverName = from.name,
            distance = "${from.distance}  $KILOMETER"
        )
    }
}
