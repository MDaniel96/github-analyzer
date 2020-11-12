package bme.softwarearchitectures.githubanalyzerserver.controller

import bme.softwarearchitectures.githubanalyzerserver.model.compare.DoubleRepositoryRequest
import bme.softwarearchitectures.githubanalyzerserver.service.DoubleRepositoryService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/repository/double")
class DoubleRepositoryController(val doubleRepositoryService: DoubleRepositoryService) {

    @PostMapping("/analyze")
    fun analyze(@RequestBody request: DoubleRepositoryRequest) {
        doubleRepositoryService.analyze(request)
    }

    @PostMapping
    fun getRepositoryInfo(@RequestBody request: DoubleRepositoryRequest) =
            doubleRepositoryService.getRepositoryInfo(request)?.let {
                ResponseEntity.ok(it)
            } ?: ResponseEntity.noContent().build()
}