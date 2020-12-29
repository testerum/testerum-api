package com.testerum_api.testerum_steps_api.test_context.test_vars

import com.testerum_api.testerum_steps_api.services.TesterumService

interface TestVariables : TesterumService {

    fun contains(name: String): Boolean

    /**
     * @throws VariableNotFoundException is [name] is not defined
     */
    operator fun get(name: String): Any?

    fun getOrDefault(name: String, defaultValue: Any?): Any?
    fun getOrDefault(name: String, defaultValueSupplier: () -> Any?): Any?

    /**
     * @return the previous value, or null if [name] was previously not defined
     */
    operator fun set(name: String, value: Any?): Any?

    fun resolveIn(text: String): String

    fun getVariablesDetailsForDebugging(): String

}
