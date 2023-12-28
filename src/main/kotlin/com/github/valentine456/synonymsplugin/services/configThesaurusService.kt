package com.github.valentine456.synonymsplugin.services

import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.project.Project

fun setUpThesaurusService(project: Project): ThesaurusService {
    val myService: ThesaurusService = ServiceManager.getService(project, FileThesaurusService::class.java)
    val myService2: ThesaurusService = ServiceManager.getService(project, BigHugeThesaurusService::class.java)
    val myService3: ThesaurusService = ServiceManager.getService(project, ApiNinjaThesaurusService::class.java)
    val myService4: ThesaurusService = ServiceManager.getService(project, MockThesaurusService::class.java)

    myService.nextService = myService2
    myService2.nextService = myService3
    myService3.nextService = myService4

    return myService
}