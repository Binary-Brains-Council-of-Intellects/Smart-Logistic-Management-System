package com.binarybrains.slms.payroll.repository;

import com.binarybrains.slms.payroll.model.Payroll;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PayrollRepository extends MongoRepository<Payroll, String> {

    List<Payroll> findByEmployeeId(String employeeId);

    List<Payroll> findByMonthAndYear(int month, int year);

    boolean existsByEmployeeIdAndMonthAndYear(String employeeId, int month, int year);

    List<Payroll> findByYear(int year);
}
