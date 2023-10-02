package users.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@FeignClient("authentication")
public interface AuthenticationClient {
    @RequestMapping(method = RequestMethod.GET, value = "/api/v1/authentication/permit")
    @ResponseBody ResponseEntity<String> permit(@RequestParam("jwt") String jwt);
}

