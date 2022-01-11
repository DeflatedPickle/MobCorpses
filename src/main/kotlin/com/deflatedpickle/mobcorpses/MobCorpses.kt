/* Copyright (c) 2021-2022 DeflatedPickle under the MIT license */

package com.deflatedpickle.mobcorpses

import net.fabricmc.api.ModInitializer

@Suppress("UNUSED")
object MobCorpses : ModInitializer {
    private const val MOD_ID = "$[id]"
    private const val NAME = "$[name]"
    private const val GROUP = "$[group]"
    private const val AUTHOR = "$[author]"
    private const val VERSION = "$[version]"

    override fun onInitialize() {
        println(listOf(MOD_ID, NAME, GROUP, AUTHOR, VERSION))
    }
}
