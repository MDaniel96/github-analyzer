package bme.softwarearchitectures.githubanalyzerserver.controller

import bme.softwarearchitectures.githubanalyzerserver.model.SingleRepositoryRequest
import bme.softwarearchitectures.githubanalyzerserver.service.SingleRepositoryService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/repository/single")
class SingleRepositoryController(val singleRepositoryService: SingleRepositoryService) {

    @PostMapping
    fun analyze(@RequestBody request: SingleRepositoryRequest) {
         singleRepositoryService.analyze(request)
    }

    @GetMapping("/{repositoryUrl}")
    fun getRepositoryInfo(@PathVariable repositoryUrl: String) =
            singleRepositoryService.getRepositoryInfo(repositoryUrl)?.let {
                ResponseEntity.ok(it)
            } ?: ResponseEntity.noContent().build()

}