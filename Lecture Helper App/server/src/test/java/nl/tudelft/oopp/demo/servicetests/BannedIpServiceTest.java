package nl.tudelft.oopp.demo.servicetests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import nl.tudelft.oopp.demo.entities.BannedIp;
import nl.tudelft.oopp.demo.repositories.BannedIpRepository;
import nl.tudelft.oopp.demo.services.BannedIpService;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * The type Banned ip service test.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@org.springframework.test.context.TestPropertySource(
        locations = "classpath:application-test.properties")
class BannedIpServiceTest {

    @Autowired
    private BannedIpService bannedIpService;

    @MockBean
    private BannedIpRepository bannedIpRepository;

    private static BannedIp b1;
    private static List<BannedIp> bannedIpList;

    /**
     * Initializing the tests.
     */
    @org.junit.jupiter.api.BeforeEach
    void init() {
        bannedIpList = new ArrayList<BannedIp>();
        for (int i = 1; i < 5; i++) {
            BannedIp b = new BannedIp("127.000." + i, LocalTime.of(i,20),i);
            bannedIpList.add(b);
        }

        b1 = new BannedIp("ipAddress", LocalTime.of(20,20),10);
    }

    /**
     * Test rooms where banned.
     */
    @Test
    void testRoomsWhereBanned() {
        BannedIp b = new BannedIp("127.000.1", LocalTime.of(21,20),15);
        bannedIpList.add(b);
        List<Long> ret = bannedIpList.stream().filter(x -> x.getIpAddress().equals("127.000.1"))
                .map(x -> x.getRoomId())
                .collect(Collectors.toList());
        when(bannedIpRepository.findAll()).thenReturn(bannedIpList);
        assertEquals(ret, bannedIpService.roomsWhereBanned("127.000.1"));
    }

    /**
     * Test Save.
     */
    @Test
    void testSave() {
        when(bannedIpRepository.save(b1)).thenReturn(b1);
        assertEquals(b1, bannedIpService.save(b1));
    }

    /**
     * Test Find all.
     */
    @Test
    void testFindAll() {
        when(bannedIpRepository.findAll()).thenReturn(bannedIpList);
        assertEquals(bannedIpList, bannedIpService.findAll());
    }

    /**
     * Test find by id.
     */
    @Test
    void testFindById() {
        when(bannedIpRepository.findById(10L)).thenReturn(java.util.Optional.of(b1));
        assertEquals(Optional.of(b1), bannedIpService.findById(10));
    }

    /**
     * Deleteby id.
     */
    @Test
    void deletebyId() {
        when(bannedIpRepository.findAllById(List.of(10L))).thenReturn(List.of(b1));
        bannedIpService.deleteById(10L);
        verify(bannedIpRepository, times(1)).deleteAll(List.of(b1));
    }
}