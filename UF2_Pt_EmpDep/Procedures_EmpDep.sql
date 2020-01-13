DELIMITER //
	CREATE PROCEDURE igualarComision(IN `var_comision` float(6,2))
	BEGIN
		UPDATE empleados SET comision = var_comision WHERE oficio = "VENDEDOR";
	END;
//

DELIMITER //
	CREATE PROCEDURE listarEmpleados(IN `var_dnombre` varchar(15))
	BEGIN
		SELECT * FROM empleados WHERE dept_no = (SELECT dept_no FROM departamentos WHERE dnombre = var_dnombre);
	END;
//