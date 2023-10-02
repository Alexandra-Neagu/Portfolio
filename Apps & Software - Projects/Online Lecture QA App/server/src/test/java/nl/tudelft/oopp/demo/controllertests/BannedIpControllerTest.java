package nl.tudelft.oopp.demo.controllertests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import nl.tudelft.oopp.demo.entities.BannedIp;
import nl.tudelft.oopp.demo.serializers.LocalDateAdapter;
import nl.tudelft.oopp.demo.serializers.LocalDateDeserializer;
import nl.tudelft.oopp.demo.serializers.LocalDateTimeDeserializer;
import nl.tudelft.oopp.demo.serializers.LocalTimeAdapter;
import nl.tudelft.oopp.demo.serializers.LocalTimeDeserializer;
import nl.tudelft.oopp.demo.services.BannedIpService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;


/**
 * The BannedIp controller test.
 * All that a controller does is manage the request we get,
 * and send back whatever we get from the service.
 * Therefore that is what we test.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@org.springframework.test.context.TestPropertySource(
        locations = "classpath:application-test.properties")
public class BannedIpControllerTest {

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private BannedIpService bannedIpService;

    @Autowired
    private MockMvc mvc;

    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .registerTypeAdapter(LocalTime.class, new LocalTimeAdapter())
            .registerTypeAdapter(LocalTime.class, new LocalTimeDeserializer())
            .registerTypeAdapter(LocalDate.class, new LocalDateDeserializer())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer())
            .create();


    private static BannedIp b1;
    private static List<BannedIp> bannedIpList;

    /**
     * Init.
     */
    @org.junit.jupiter.api.BeforeAll
    public static void init() {
        bannedIpList = new ArrayList<BannedIp>();
        for (int i = 1; i < 5; i++) {
            BannedIp b = new BannedIp("127.000." + i, LocalTime.of(i,20),i);
            bannedIpList.add(b);
        }

        b1 = new BannedIp("ipAddress", LocalTime.of(20,20),10);
    }

    /**
     * Test rooms where banned.
     *
     * @throws Exception the exception
     */
    @Test
    void testRoomsWhereBanned() throws Exception {
        BannedIp b = new BannedIp("127.000.1", LocalTime.of(21,20),15);
        bannedIpList.add(b);
        List<Long> ret = bannedIpList.stream().filter(x -> x.getIpAddress().equals("127.000.1"))
                .map(x -> x.getRoomId())
                .collect(Collectors.toList());

        assertEquals(ret, mvcPerformBannedIpList("/bannedIps/roomsBanned/127.000.1",
                "roomsWhereBanned", ret, "127.000.1"));
    }


    private List<Long> mvcPerformBannedIpList(String urlTemplate,
                                              String methodName,
                                              List<Long> response,
                                              String param) throws Exception {
        Method method;
        Object[] obj;
        if (param == null) {
            obj = new Object[0];
            method = bannedIpService.getClass().getDeclaredMethod(methodName);
        } else {
            obj = new Object[1];
            obj[0] = param;
            Class[] carg = new Class[1];
            carg[0] = String.class;
            method = bannedIpService.getClass().getDeclaredMethod(methodName, carg);
        }
        when(method.invoke(bannedIpService, obj)).thenReturn(response);

        MvcResult result = mvc.perform(MockMvcRequestBuilders.get(urlTemplate)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        return gson.fromJson(result.getResponse().getContentAsString(),
                new TypeToken<List<Long>>() {
                }.getType());
    }

    /**
     * Test save.
     *
     * @throws Exception the exception
     */
    @Test
    void testSave() throws Exception {

        when(bannedIpService.save(b1)).thenReturn(b1);
        //String requestBody = new ObjectMapper().valueToTree(bannedIp).toString();
        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/bannedIps/insert")
                .content(gson.toJson(b1))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        BannedIp bannedIp = gson.fromJson(result.getResponse()
                .getContentAsString(), BannedIp.class);
        verify(bannedIpService).save(any(BannedIp.class));
        assertEquals(b1, bannedIp);
    }
}
