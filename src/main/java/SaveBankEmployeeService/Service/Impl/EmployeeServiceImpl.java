package SaveBankEmployeeService.Service.Impl;

import SaveBankEmployeeService.Dto.APIResponseDto;
import SaveBankEmployeeService.Dto.DepartmentDto;
import SaveBankEmployeeService.Dto.EmployeeDto;
import SaveBankEmployeeService.Entity.Employee;
import SaveBankEmployeeService.Exception.EmailAlreadyExistsException;
import SaveBankEmployeeService.Exception.ResourceNotFoundException;
import SaveBankEmployeeService.Mapper.EmployeeMapper;
import SaveBankEmployeeService.Repository.EmployeeRepository;
import SaveBankEmployeeService.Service.APIClient;
import SaveBankEmployeeService.Service.EmployeeService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Optional;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl  implements EmployeeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    private EmployeeRepository employeeRepository;

    private APIClient apiClient;

    @Override
    public EmployeeDto saveEmployee(EmployeeDto employeeDto) {

        Optional<Employee> optionalUser = employeeRepository.findByEmail(employeeDto.getEmail());

        if(optionalUser.isPresent()){
            throw new EmailAlreadyExistsException("Email Already Exists for User");
        }

        Employee employee = EmployeeMapper.mapper.mapToEmployee(employeeDto);

        Employee saveDEmployee = employeeRepository.save(employee);

        EmployeeDto savedEmployeeDto = EmployeeMapper.mapper.mapToEmployeeDto(saveDEmployee);

        return savedEmployeeDto;
    }

//    @CircuitBreaker(name = "${spring.application.name}", fallbackMethod = "getDefaultDepartment")
    @Retry(name = "${spring.application.name}", fallbackMethod = "getDefaultDepartment")
    @Override
    public APIResponseDto getEmployeeById(Long id){

        LOGGER.info("inside getEmployeeById() method");

        Employee employee = employeeRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("employee","id", id)
        );
        EmployeeDto savedEmployeeDto = EmployeeMapper.mapper.mapToEmployeeDto(employee);

       DepartmentDto departmentDto = apiClient.getDepartment(employee.getDepartmentCode());

        APIResponseDto apiResponseDto = new APIResponseDto();
        apiResponseDto.setDepartmentDto(departmentDto);
        apiResponseDto.setEmployeeDto(savedEmployeeDto);

        return apiResponseDto;
    }

    public APIResponseDto getDefaultDepartment(Long id, Exception exception){

        LOGGER.info("inside getDefaultDepartment() method");

        Employee employee = employeeRepository.findById(id).get();
        EmployeeDto savedEmployeeDto = EmployeeMapper.mapper.mapToEmployeeDto(employee);

        DepartmentDto departmentDto = new DepartmentDto();
        departmentDto.setDepartmentName("IT");
        departmentDto.setDepartmentCode("DCIT");
        departmentDto.setDepartmentDescription("IT department");

        APIResponseDto apiResponseDto = new APIResponseDto();
        apiResponseDto.setDepartmentDto(departmentDto);
        apiResponseDto.setEmployeeDto(savedEmployeeDto);

        return apiResponseDto;

    }
}
