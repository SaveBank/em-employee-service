package SaveBankEmployeeService.Service.Impl;

import SaveBankEmployeeService.Dto.EmployeeDto;
import SaveBankEmployeeService.Entity.Employee;
import SaveBankEmployeeService.Exception.EmailAlreadyExistsException;
import SaveBankEmployeeService.Mapper.EmployeeMapper;
import SaveBankEmployeeService.Repository.EmployeeRepository;
import SaveBankEmployeeService.Service.EmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl  implements EmployeeService {

    private EmployeeRepository employeeRepository;

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
}
