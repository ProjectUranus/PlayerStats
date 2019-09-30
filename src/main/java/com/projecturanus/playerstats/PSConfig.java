package com.projecturanus.playerstats;

import net.minecraftforge.common.config.Config;

@Config(modid = PlayerStatsKt.MODID)
public class PSConfig {
    @Config.Comment("MongoDB Connection String")
    @Config.RequiresMcRestart
    public static String connectionString = "mongodb://127.0.0.1:27017/stat";

    @Config.Comment("Database name")
    public static String databaseName = "stat";

    @Config.Comment("Player collection name")
    public static String collectionName = "pstats";

    @Config.Comment("Inventory collection name")
    public static String inventoryCollectionName = "istats";
}
