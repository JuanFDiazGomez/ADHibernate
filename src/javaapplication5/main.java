/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication5;

import java.sql.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

/**
 *
 * @author alumno
 */
public class main {

    public static void main(String[] args) {
	int opcion = 0;
	Scanner sc = new Scanner(System.in);

	do {
	    System.out.println("--------------------------------------");
	    System.out.println("            Menú Principal");
	    System.out.println("--------------------------------------");
	    System.out.println("1. Creacion de departamentos");
	    System.out.println("2. Creacion de empleados");
	    System.out.println("3. Mostrar informacion de un departamento");
	    System.out.println("0. Salir");
	    System.out.print("Introduce la opción deseada: ");
	    opcion = sc.nextInt();
	    switch (opcion) {
		case 1: {
		    crearDepartamentosHibernate();
		    break;
		}
		case 2: {
		    crearEmpleadosHibernate();
		    break;
		}
		case 3: {
		    leerDepartamentos();
		    break;
		}
		default: {
		    break;
		}
	    }
	} while (opcion != 0);
    }

    private static void crearDepartamentosHibernate() {
	Scanner sc = new Scanner(System.in);

	SessionFactory sesion = NewHibernateUtil.getSessionFactory();
	Session sesionCreada = sesion.openSession();

	Transaction tx = sesionCreada.beginTransaction();

	System.out.println("Inserte los datos del departamento:");
	System.out.print("Numero: ");
	byte id = Byte.parseByte(sc.nextLine());
	System.out.print("Nombre: ");
	String nombre = sc.nextLine();
	System.out.print("Localidad: ");
	String localidad = sc.nextLine();

	Departamentos dep = new Departamentos(id, nombre, localidad, new HashSet());
	sesionCreada.save(dep);
	try {
	    tx.commit();
	} catch (ConstraintViolationException cve) {
	    System.out.println("El departamento ya existe");
	}

	sesionCreada.close();
    }

    private static void crearEmpleadosHibernate() {
	Scanner sc = new Scanner(System.in);

	SessionFactory sesion = NewHibernateUtil.getSessionFactory();
	Session sesionCreada = sesion.openSession();

	Transaction tx = sesionCreada.beginTransaction();

	System.out.println("Inserte los datos del empleado:");
	System.out.print("Numero: ");
	short id = Short.parseShort(sc.nextLine());
	System.out.print("Apellido: ");
	String apellido = sc.nextLine();
	System.out.print("Oficio: ");
	String oficio = sc.nextLine();
	System.out.print("Director: ");
	short director = Short.parseShort(sc.nextLine());
	System.out.print("Fecha de alta: ");
	Date falt = Date.valueOf(sc.nextLine());
	System.out.print("Salario: ");
	float salario = Float.parseFloat(sc.nextLine());
	System.out.print("Comision: ");
	float comision = Float.parseFloat(sc.nextLine());
	System.out.print("Numero de departamento: ");
	byte departamento = Byte.parseByte(sc.nextLine());

	Departamentos dept = (Departamentos) sesionCreada.load(Departamentos.class, departamento);
	Empleados emp = new Empleados(id, apellido, oficio, director, falt, salario, comision, dept);
	sesionCreada.save(emp);

	try {
	    tx.commit();
	} catch (ConstraintViolationException cve) {
	    System.out.println("El empleado ya existe");
	}

	sesionCreada.close();
    }

    private static void leerDepartamentos() {
	try {
	    Scanner sc = new Scanner(System.in);

	    SessionFactory sesion = NewHibernateUtil.getSessionFactory();
	    Session sesionCreada = sesion.openSession();

	    System.out.println("Introduzca los datos del departamento a buscar:");
	    System.out.print("Numero: ");
	    byte id = Byte.parseByte(sc.nextLine());

	    Departamentos dept = (Departamentos) sesionCreada.load(Departamentos.class, id);

	    System.out.println("Datos del epartamento:" + dept.getDeptNo()
		    + "\n\tNombre de departamento: " + dept.getDnombre()
		    + "\n\tLocalidad de departamento: " + dept.getLoc()
	    );

	    Set empleados = dept.getEmpleadoses();
	    for (Iterator it = empleados.iterator(); it.hasNext();) {
		Empleados emple = (Empleados) it.next();
		System.out.println("\tNumero empleado: " + emple.getEmpNo()
			+ "\n\t\tApellido: " + emple.getApellido()
			+ "\n\t\tOficio: " + emple.getOficio()
			+ "\n\t\tDirector: " + emple.getDir()
			+ "\n\t\tFecha de alta: " + emple.getFechaAlt().toString()
			+ "\n\t\tSalario: " + emple.getSalario()
			+ "\n\t\tComision: " + emple.getComision()
		);
	    }

	    sesionCreada.close();
	}catch(ObjectNotFoundException onfe){
	    System.out.println("El departamento solicitado no existe");
	}
    }
}
