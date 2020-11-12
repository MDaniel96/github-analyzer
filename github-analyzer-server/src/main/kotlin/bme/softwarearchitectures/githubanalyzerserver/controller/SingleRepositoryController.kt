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

    @PostMapping("/contribution")
    fun getContributionResult(@RequestBody request: SingleRepositoryRequest) =
            singleRepositoryService.getContributionResultMap(request)?.let {
                ResponseEntity.ok(it)
            } ?: ResponseEntity.ok().build()

    @PostMapping("/modification")
    fun getModificationResult(@RequestBody request: SingleRepositoryRequest) =
            singleRepositoryService.getModificationResultMap(request)?.let {
                ResponseEntity.ok(it)
            } ?: ResponseEntity.ok().build()

    @PostMapping("/distribution")
    fun getDistributionResult(@RequestBody request: SingleRepositoryRequest) =
            singleRepositoryService.getDistributionResultMap(request)?.let {
                ResponseEntity.ok(it)
            } ?: ResponseEntity.ok().build()
}