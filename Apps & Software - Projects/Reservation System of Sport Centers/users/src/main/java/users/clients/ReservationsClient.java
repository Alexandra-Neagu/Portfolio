package users.clients;

import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@FeignClient("reservations")
public interface ReservationsClient {
    @RequestMapping(method = RequestMethod.GET, value = "api/v1/reservation/exists")
    @ResponseBody ResponseEntity<Boolean> doesReservationExist(
            @RequestParam("reservationUuid") UUID reservationUuid,
            @RequestParam("teamUuid") UUID teamUuid
    );
}
