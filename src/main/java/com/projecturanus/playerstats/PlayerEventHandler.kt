package com.projecturanus.playerstats

import com.mongodb.client.MongoCollection
import com.mongodb.client.model.ReplaceOptions
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.stats.StatList
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.PlayerEvent
import org.bson.Document

val groupedStats = arrayOf("mineBlock", "useItem", "craftItem", "breakItem", "drop", "pickup", "killEntity", "entityKilledBy")

val collection: MongoCollection<Document>
    get() = database.getCollection(PSConfig.collectionName)

val syncBehaviors = mutableListOf<Document.(EntityPlayerMP) -> Unit>()

fun sync(player: EntityPlayerMP) {
    GlobalScope.launch {
        collection.replaceOne(Document().apply {
            append("_id", player.uniqueID)
        }, Document().apply {
            append("_id", player.uniqueID)
            append("name", player.name)
            append("posX", player.posX)
            append("posY", player.posY)
            append("posZ", player.posZ)
            val groupedMap = groupedStats.map { it to Document() }.toMap()
            StatList.ALL_STATS.forEach {
                if (player.statFile.readStat(it) != 0) {
                    if (!groupedStats.any { prefix ->
                            if (it.statId.startsWith("stat.$prefix.")) {
                                groupedMap[prefix]?.append(it.statId.removePrefix("stat.$prefix.").replace('.', ':'), player.statFile.readStat(it))
                                true
                            } else false
                        })
                        append(it.statId.removePrefix("stat.").replace('.', ':'), player.statFile.readStat(it))
                }
            }
            groupedMap.filter { it.value.size > 0 }.forEach { (key, value) -> append(key, value) }
            syncBehaviors.forEach { it(player) }
        }, ReplaceOptions().upsert(true))
        logger.info("Synchronized ${player.name}'s stats to the database")
    }
}

object PlayerEventHandler {
    @SubscribeEvent
    fun playerJoinEvent(event: PlayerEvent.PlayerLoggedInEvent) {
        sync(event.player as EntityPlayerMP)
    }

    @SubscribeEvent
    fun playerChangeWorldEvent(event: PlayerEvent.PlayerChangedDimensionEvent) {
        sync(event.player as EntityPlayerMP)
    }

    @SubscribeEvent
    fun playerLeaveEvent(event: PlayerEvent.PlayerLoggedOutEvent) {
        sync(event.player as EntityPlayerMP)
    }
}
