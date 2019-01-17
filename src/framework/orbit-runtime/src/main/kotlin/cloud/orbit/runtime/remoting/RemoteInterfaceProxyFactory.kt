/*
 Copyright (C) 2015 - 2019 Electronic Arts Inc.  All rights reserved.
 This file is part of the Orbit Project <http://www.orbit.cloud>.
 See license in LICENSE.
 */

package cloud.orbit.runtime.remoting

import cloud.orbit.core.key.Key
import cloud.orbit.core.remoting.Addressable
import cloud.orbit.runtime.pipeline.PipelineManager
import java.lang.reflect.Proxy

class RemoteInterfaceProxyFactory(
    private val pipelineManager: PipelineManager,
    private val interfaceDefinitionDictionary: RemoteInterfaceDefinitionDictionary
) {
    fun <T : Addressable> getReference(interfaceClass: Class<T>, key: Key): T {
        val interfaceDefinition = interfaceDefinitionDictionary.getOrCreate(interfaceClass)

        val invocationHandler = RemoteInterfaceProxy(
            pipelineManager = pipelineManager,
            interfaceDefinition = interfaceDefinition,
            key = key
        )
        val javaProxy = Proxy.newProxyInstance(
            javaClass.classLoader,
            arrayOf(interfaceClass),
            invocationHandler
        )
        @Suppress("UNCHECKED_CAST")
        return javaProxy as T
    }
}