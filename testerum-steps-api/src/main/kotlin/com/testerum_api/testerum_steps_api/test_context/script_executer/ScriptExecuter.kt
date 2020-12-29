package com.testerum_api.testerum_steps_api.test_context.script_executer

import com.testerum_api.testerum_steps_api.services.TesterumService

interface ScriptExecuter : TesterumService {

    fun executeScript(script: String): Any?

    fun executeScript(script: String, context: Map<String, Any?>): Any?

}
