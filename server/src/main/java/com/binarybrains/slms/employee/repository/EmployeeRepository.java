package com.binarybrains.slms.employee.repository;

import com.binarybrains.slms.employee.model.Employee;
import com.binarybrains.slms.employee.model.EmployeeType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends MongoRepository<Employee, String> {

    Optional<Employee> findByEmail(String email);

    List<Employee> findByEmployeeType(EmployeeType employeeType);

    List<Employee> findByActiveTrue();

    List<Employee> findByEmployeeTypeAndActiveTrue(EmployeeType employeeType);

    @Query("{ 'name': { $regex: ?0, $options: 'i' } }")
    List<Employee> searchByName(String keyword);

    boolean existsByEmail(String email);

    long countByActiveTrue();

    long countByEmployeeType(EmployeeType employeeType);
}
