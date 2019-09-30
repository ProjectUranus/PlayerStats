package com.projecturanus.playerstats

import net.minecraft.command.CommandBase
import net.minecraft.command.ICommandSender
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.server.MinecraftServer
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.BlockPos
import net.minecraft.util.text.TextComponentString
import net.minecraftforge.items.CapabilityItemHandler

object SyncStatCommand: CommandBase() {
    override fun getName() = "syncstats"

    override fun execute(server: MinecraftServer, sender: ICommandSender, args: Array<String>) {
        if (sender is EntityPlayerMP) {
            sync(sender)
            sender.sendMessage(TextComponentString("Synchronized stat data"))
        } else {
            sender.sendMessage(TextComponentString("Only online player can execute this command!"))
        }
    }

    override fun getUsage(sender: ICommandSender) = "commands.playerstats.syncstats.usage"
}

object ReadContainerCommand: CommandBase() {
    override fun getName() = "readcontainer"

    override fun execute(server: MinecraftServer, sender: ICommandSender, args: Array<String>) {
        if (sender is EntityPlayerMP) {
            println(sender.world.getTileEntity(BlockPos(10, 64, 10))?.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP))
        } else {
            sender.sendMessage(TextComponentString("Only online player can execute this command!"))
        }
    }

    override fun getUsage(sender: ICommandSender) = "commands.playerstats.readcontainer.usage"
}
