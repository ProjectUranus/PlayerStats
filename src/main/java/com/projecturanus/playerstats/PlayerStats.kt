package com.projecturanus.playerstats

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoDatabase
import com.projecturanus.playerstats.support.ModSupport
import com.projecturanus.playerstats.support.supports
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.Loader
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLConstructionEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.event.FMLServerStartingEvent
import org.apache.logging.log4j.LogManager

const val MODID = "playerstats"

val logger = LogManager.getLogger("PlayerStats")
lateinit var mongoClient: MongoClient
val database: MongoDatabase
    get() = mongoClient.getDatabase(PSConfig.databaseName)

@Mod(modid = MODID, serverSideOnly = true, acceptableRemoteVersions = "*", modLanguageAdapter = "net.shadowfacts.forgelin.KotlinAdapter",
    dependencies = "after: ftbutilities, ftblib")
object PlayerStats {
    @Mod.EventHandler
    fun construct(event: FMLConstructionEvent) {
        supports.filter { Loader.isModLoaded(it.getModId()) }.onEach {
            logger.info("Loaded support for ${it.getModId()}")
        }.onEach(MinecraftForge.EVENT_BUS::register).forEach(ModSupport::init)
    }

    @Mod.EventHandler
    fun preInit(event: FMLPreInitializationEvent) {
        mongoClient = MongoClients.create(PSConfig.connectionString)
        MinecraftForge.EVENT_BUS.register(PlayerEventHandler)
    }

    @Mod.EventHandler
    fun serverStarting(event: FMLServerStartingEvent) {
        event.registerServerCommand(SyncStatCommand)
    }
}
