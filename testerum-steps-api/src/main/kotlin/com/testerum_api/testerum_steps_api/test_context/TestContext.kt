package com.testerum_api.testerum_steps_api.test_context

import com.testerum_api.testerum_steps_api.services.TesterumService

interface TestContext : TesterumService {

    val stepsClassLoader: ClassLoader

    val testName: String?
    val testPath: String?

    val testStatus: ExecutionStatus?

}
