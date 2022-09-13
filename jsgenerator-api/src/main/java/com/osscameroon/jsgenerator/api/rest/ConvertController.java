package com.osscameroon.jsgenerator.api.rest;

import com.osscameroon.jsgenerator.api.domain.Command;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

import static java.util.List.of;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * ConvertController
 *
 * @author Salathiel @t salathiel@genese.name
 * @since Sep 13, 2022 @t 05:14:18
 */
@RestController
@RequestMapping("convert")
public class ConvertController {
    private static final Logger LOGGER = getLogger(ConvertController.class);

    @PostMapping
    public List<Void> inlineAction(@RequestBody @Valid final Command command) {
        LOGGER.info("{}", command);
        return of();
    }
}
