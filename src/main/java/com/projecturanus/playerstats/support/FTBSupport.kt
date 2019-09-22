package com.projecturanus.playerstats.support

import com.feed_the_beast.ftblib.lib.data.FTBLibAPI
import com.feed_the_beast.ftblib.lib.data.Universe
import com.feed_the_beast.ftbutilities.data.FTBUtilitiesPlayerData
import com.projecturanus.playerstats.syncBehaviors
import org.bson.Document

/**
 * FTB Teams support
 */
object FTBLibSupport: ModSupport() {
    override fun getModId() = "ftblib"

    override fun init() {
        syncBehaviors += { player ->
            append("team", FTBLibAPI.getTeam(player.uniqueID))
        }
    }
}

object FTBUtilitiesSupport: ModSupport() {
    override fun getModId() = "ftbutilities"

    override fun init() {
        syncBehaviors += { player ->
            val data = FTBUtilitiesPlayerData.get(Universe.get().getPlayer(player))
            if (data.nickname.isNotBlank())
                append("nick", data.nickname)
            if (data.homes.size() > 0) {
                append("homes", Document().apply {
                    data.homes.list().forEach { home ->
                        append(home, Document().apply {
                            append("posX", data.homes.get(home)?.posX)
                            append("posY", data.homes.get(home)?.posY)
                            append("posZ", data.homes.get(home)?.posZ)
                            append("dim", data.homes.get(home)?.dim)
                        })
                    }
                })
            }
        }
    }
}
