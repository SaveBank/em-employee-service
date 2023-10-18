package SaveBankEmployeeService.Controller;

import SaveBankEmployeeService.configs.AccountServiceConfig;
import SaveBankEmployeeService.configs.ConfigProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/employees")
public class ConfigPropertyController {

    @Autowired
    private AccountServiceConfig accountServiceConfig;

    @GetMapping("/properties")
    public ResponseEntity<String> getAccountProperties() throws JsonProcessingException {

        ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
        ConfigProperties configProperties = new ConfigProperties(accountServiceConfig.getMsg(),accountServiceConfig.getBuildVersion(),accountServiceConfig.getMailDetails(),accountServiceConfig.getActiveBranches());

        String jsonString = objectWriter.writeValueAsString(configProperties);
        return new ResponseEntity<>(jsonString, HttpStatus.OK);
    }
}
