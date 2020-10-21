package bme.softwarearchitectures.githubanalyzerserver.controller

import bme.softwarearchitectures.githubanalyzerserver.model.TestMessage
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/test")
class TestController {

    @GetMapping
    fun test() = TestMessage(text = "Hello world!")
}