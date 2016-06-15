SELECT Employees.EmpTable.LastName, Employees.EmpTable.FirstName, Employees.EmpTable.MiddleName AS MiddleInitial, 
Employees.EmpTable.Street, Employees.EmpTable.City, Employees.EmpTable.State, convert(Employees.EmpTable.EmpId, biginteger) AS EmpId, 
Employees.EmpTable.HomePhone AS Phone, convert(Employees.EmpTable.Manager, biginteger) AS mgrID 
FROM Employees.EmpTable 
WHERE INPUTS.mgrID = Employees.EmpTable.EmpId