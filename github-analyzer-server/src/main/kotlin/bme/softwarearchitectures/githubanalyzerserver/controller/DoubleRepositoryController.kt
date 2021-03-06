package bme.softwarearchitectures.githubanalyzerserver.controller

import bme.softwarearchitectures.githubanalyzerserver.dto.compare.DoubleRepositoryRequest
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

    @PostMapping("/development")
    fun getDevelopmentCompareResult(@RequestBody request: DoubleRepositoryRequest) =
            doubleRepositoryService.getDevelopmentCompareResult(request)?.let {
                ResponseEntity.ok(it)
            } ?: ResponseEntity.ok().build()

    @PostMapping("/developer")
    fun getDeveloperCompareResult(@RequestBody request: DoubleRepositoryRequest) =
            doubleRepositoryService.getDeveloperCompareResult(request)?.let {
                ResponseEntity.ok(it)
            } ?: ResponseEntity.ok().build()
}