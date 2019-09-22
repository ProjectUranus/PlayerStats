package com.projecturanus.playerstats.support

val supports = arrayOf(FTBLibSupport, FTBUtilitiesSupport)

abstract class ModSupport {
    abstract fun getModId(): String
    abstract fun init()
}
