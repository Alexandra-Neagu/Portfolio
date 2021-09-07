package nl.tudelft.oopp.demo.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import nl.tudelft.oopp.demo.entities.BannedIp;
import nl.tudelft.oopp.demo.repositories.BannedIpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * The type Banned ip service.
 */
@Service
public class BannedIpService {

    /**
     * The Banned ip repository.
     */
    private final BannedIpRepository bannedIpRepository;

    /**
     * Instantiates a new Banned ip service.
     *
     * @param bannedIpRepository the banned ip repository
     */
    @Autowired
    public BannedIpService(BannedIpRepository bannedIpRepository) {
        this.bannedIpRepository = bannedIpRepository;
    }

    /**
     * Save banned ip.
     *
     * @param bannedIp the banned ip
     * @return the banned ip
     */
    public BannedIp save(BannedIp bannedIp) {
        return bannedIpRepository.save(bannedIp);
    }

    /**
     * Find all list.
     *
     * @return the list
     */
    public List<BannedIp> findAll() {
        return bannedIpRepository.findAll();
    }

    /**
     * Find by id optional.
     *
     * @param id the id
     * @return the optional
     */
    public Optional<BannedIp> findById(long id) {
        return bannedIpRepository.findById(id);
    }

    /**
     * Delete by id.
     *
     * @param id the id
     */
    public void deleteById(long id) {
        bannedIpRepository.deleteAll(bannedIpRepository.findAllById(List.of(id)));
    }

    /**
     * Check if banned boolean.
     *
     * @param ip the ip
     * @return the boolean
     */
    public List<Long> roomsWhereBanned(String ip) {
        List<BannedIp> bannedIps = bannedIpRepository.findAll();
        List<Long> roomsBanned = new ArrayList();
        for (BannedIp bannedIp : bannedIps) {
            if (ip.equals(bannedIp.getIpAddress())) {
                roomsBanned.add(bannedIp.getRoomId());
            }
        }
        return roomsBanned;
    }
}