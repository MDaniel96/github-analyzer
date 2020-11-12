package bme.softwarearchitectures.githubanalyzerserver.controller

import bme.softwarearchitectures.githubanalyzerserver.model.SingleRepositoryRequest
import bme.softwarearchitectures.githubanalyzerserver.service.SingleRepositoryService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/repository/single")
class SingleRepositoryController(val singleRepositoryService: SingleRepositoryService) {

    @PostMapping("/analyze")
    fun analyze(@RequestBody request: SingleRepositoryRequest) {
        singleRepositoryService.analyze(request)
    }

    @PostMapping
    fun getRepositoryInfo(@RequestBody request: SingleRepositoryRequest) =
            singleRepositoryService.getRepositoryInfo(request)?.let {
                ResponseEntity.ok(it)
            } ?: ResponseEntity.notFound().build()
}