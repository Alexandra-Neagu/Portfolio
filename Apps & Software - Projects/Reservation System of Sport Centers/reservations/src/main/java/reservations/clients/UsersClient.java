package reservations.clients;

import java.net.http.HttpResponse;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import reservations.entities.Reservation;

@FeignClient("users")
public interface UsersClient {
    String teamUuidString = "teamUuid";

    @RequestMapping(method = RequestMethod.GET, value = "api/v1/team/exists")
    @ResponseBody ResponseEntity<Boolean> doesTeamExist(
            @RequestParam(teamUuidString) UUID teamUuid
    );

    @RequestMapping(method = RequestMethod.GET, value = "api/v1/team/canTeamBook")
    @ResponseBody ResponseEntity<Boolean> canTeamBook(
            @RequestParam(teamUuidString) UUID teamUuid,
            @RequestParam("reservationTime") String reservationTime
    );

    @RequestMapping(method = RequestMethod.GET, value = "api/v1/team/getTeamSize")
    @ResponseBody ResponseEntity<Integer> getTeamSize(@RequestParam(teamUuidString) UUID teamUuid);

    @RequestMapping(method = RequestMethod.PUT, value = "api/v1/team/addReservation")
    @ResponseBody ResponseEntity<Void> addReservationForTeam(
            @RequestParam(teamUuidString) UUID teamUuid,
            @RequestParam("reservationUuid") UUID reservationUuid,
            @RequestParam("timestamp") String timestamp
    );


    @RequestMapping(method = RequestMethod.DELETE, value = "api/v1/team/deleteReservation")
    @ResponseBody ResponseEntity<Void> deleteReservationForTeam(
            @RequestParam("reservationUuid") UUID reservationUuid,
            @RequestParam("teamUuid") UUID teamUuid
    );
}
