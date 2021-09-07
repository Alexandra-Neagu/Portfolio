package nl.tudelft.oopp.demo.controllers;

import java.util.List;
import java.util.Optional;

import nl.tudelft.oopp.demo.entities.BannedIp;
import nl.tudelft.oopp.demo.services.BannedIpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * The type Banned ip controller.
 */
@Controller
@RestController
@RequestMapping("bannedIps")
public class BannedIpController {

    private final BannedIpService bannedIpService;

    /**
     * Instantiates a new Banned ip controller.
     *
     * @param bannedIpService the banned ip service
     */
    public BannedIpController(BannedIpService bannedIpService) {
        this.bannedIpService = bannedIpService;
    }

    /**
     * Save banned ip.
     *
     * @param bannedIp the banned ip
     * @return the banned ip
     */
    @PostMapping("/insert")
    BannedIp save(@RequestBody BannedIp bannedIp) {
        return bannedIpService.save(bannedIp);
    }

    /**
     * Find by id optional.
     *
     * @param id the id
     * @return the optional
     */
    @GetMapping("/get/{id}")
    public Optional<BannedIp> findById(@PathVariable long id) {
        return bannedIpService.findById(id);
    }

    /**
     * Find a ll list.
     *
     * @return the list
     */
    @GetMapping
    List<BannedIp> findALl() {
        return bannedIpService.findAll();
    }

    /**
     * Delete byid.
     *
     * @param id the id
     */
    @DeleteMapping("/delete/{id}")
    public void deleteByid(@PathVariable long id) {
        bannedIpService.deleteById(id);
    }

    /**
     * Rooms where banned list.
     *
     * @param ip the ip
     * @return the list
     */
    @GetMapping("/roomsBanned/{ip}")
    public List<Long> roomsWhereBanned(@PathVariable String ip) {
        return bannedIpService.roomsWhereBanned(ip);
    }
}
