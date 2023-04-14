package SaveBankEmployeeService.Mapper;

import SaveBankEmployeeService.Dto.APIResponseDto;
import SaveBankEmployeeService.Dto.DepartmentDto;
import SaveBankEmployeeService.Dto.EmployeeDto;
import SaveBankEmployeeService.Entity.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface EmployeeMapper  {

    EmployeeMapper mapper = Mappers.getMapper(EmployeeMapper.class);

    Employee mapToEmployee(EmployeeDto employeeDto);
    EmployeeDto mapToEmployeeDto(Employee employee);

}
