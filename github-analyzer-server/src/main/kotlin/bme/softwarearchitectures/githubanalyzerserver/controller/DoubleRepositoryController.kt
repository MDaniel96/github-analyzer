package bme.softwarearchitectures.githubanalyzerserver.controller

import bme.softwarearchitectures.githubanalyzerserver.model.compare.DoubleRepositoryRequest
import bme.softwarearchitectures.githubanalyzerserver.service.DoubleRepositoryService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/repository/double")
class DoubleRepositoryController(val doubleRepositoryService: DoubleRepositoryService) {

    @PostMapping
    fun analyze(@RequestBody request: DoubleRepositoryRequest) {
        doubleRepositoryService.analyze(request)
    }

    @GetMapping("/{repository1Url}/{repository2Url}")
    fun getRepositoryInfo(@PathVariable repository1Url: String, @PathVariable repository2Url: String) =
            doubleRepositoryService.getRepositoryInfo(repository1Url, repository2Url)?.let {
                ResponseEntity.ok(it)
            } ?: ResponseEntity.noContent().build()
}