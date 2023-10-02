package authentication.clients;

import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * The UsersClient interface enables the `Authentication` microservice to reach endpoints
 * from the `Users` microservice.
 */
@FeignClient("users")
public interface UsersClient {

    @RequestMapping(value = "api/v1/user/add", method = RequestMethod.PUT)
    @ResponseBody void addUser(
            @RequestParam("userUuid") UUID userUuid,
            @RequestParam("name") String name
    );

    @RequestMapping(value = "api/v1/user/delete", method = RequestMethod.DELETE)
    @ResponseBody void deleteUser(
            @RequestParam("userUuid") UUID userUuid
    );
}
