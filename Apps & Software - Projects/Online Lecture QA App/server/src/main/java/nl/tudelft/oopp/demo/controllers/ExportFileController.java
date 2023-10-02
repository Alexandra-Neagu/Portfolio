package nl.tudelft.oopp.demo.controllers;

import nl.tudelft.oopp.demo.entities.StringResponse;
import nl.tudelft.oopp.demo.services.ExportFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Export file controller.
 */
@RestController
@RequestMapping("exportFile")
public class ExportFileController {

    private final ExportFileService exportFileService;

    /**
     * Instantiates a new Export file controller.
     *
     * @param exportFileService the export file service
     */
    @Autowired
    public ExportFileController(ExportFileService exportFileService) {
        this.exportFileService = exportFileService;
    }

    /**
     * Create file.
     *
     * @param lecturerRoomCode the lecturer room code
     * @return the string response
     */
    @GetMapping("/create/{lecturerRoomCode}")
    public StringResponse createFile(@PathVariable String lecturerRoomCode) {
        return new StringResponse(exportFileService.createFile(lecturerRoomCode));
    }
}
