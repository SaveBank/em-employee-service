package SaveBankEmployeeService.Service.Impl;

import SaveBankEmployeeService.Dto.APIResponseDto;
import SaveBankEmployeeService.Dto.DepartmentDto;
import SaveBankEmployeeService.Dto.EmployeeDto;
import SaveBankEmployeeService.Entity.Employee;
import SaveBankEmployeeService.Exception.EmailAlreadyExistsException;
import SaveBankEmployeeService.Exception.ResourceNotFoundException;
import SaveBankEmployeeService.Mapper.EmployeeMapper;
import SaveBankEmployeeService.Repository.EmployeeRepository;
import SaveBankEmployeeService.Service.EmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Optional;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl  implements EmployeeService {

    private EmployeeRepository employeeRepository;

    private WebClient webClient;

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

    @Override
    public APIResponseDto getEmployeeById(Long id){

        Employee employee = employeeRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("employee","id", id)
        );
        EmployeeDto savedEmployeeDto = EmployeeMapper.mapper.mapToEmployeeDto(employee);

        DepartmentDto departmentDto = webClient.get()
                .uri("http://localhost:8080/api/departments/" + employee.getDepartmentCode())
                .retrieve()
                .bodyToMono(DepartmentDto.class)
                .block();

        APIResponseDto apiResponseDto = new APIResponseDto();
        apiResponseDto.setDepartmentDto(departmentDto);
        apiResponseDto.setEmployeeDto(savedEmployeeDto);

        return apiResponseDto;

    }
}
