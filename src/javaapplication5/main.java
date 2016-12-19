/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication5;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.PropertyValueException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

/**
 *
 * @author alumno
 */
public class main {

    static Scanner sc = new Scanner(System.in);

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
	    System.out.println("4. Modificar un departamento");
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
		    leerUnDepartamentos();
		    break;
		}
		case 4: {
		    modificarDepartamento();
		    break;
		}
		case 5: {
		    modificarEmpleado();
		    break;
		}
		case 6: {
		    leerDepartamentos();
		    break;
		}
		case 7:{
		    leerDepartamentosSevilla();
		    break;
		}
		case 8:{
		    leerDepartamentosPorLocalidad();
		    break;
		}
		case 9: {
		    leerDepartamentosPorLocalidadMultiple();
		    break;
		}
		case 7:
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

    private static void leerUnDepartamentos() {
	try {
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
	} catch (ObjectNotFoundException onfe) {
	    System.out.println("El departamento solicitado no existe");
	}
    }

    private static void modificarDepartamento() {
	try {
	    SessionFactory sesion = NewHibernateUtil.getSessionFactory();
	    Session sesionCreada = sesion.openSession();
	    Transaction tx = sesionCreada.beginTransaction();
	    System.out.println("Introduzca el numero de departamento a modificar");
	    byte id = Byte.parseByte(sc.nextLine());

	    Departamentos dept = (Departamentos) sesionCreada.load(Departamentos.class, id);

	    System.out.print("Introduce el nuevo nombre de departamento: ");
	    dept.setDnombre(sc.nextLine());
	    System.out.print("Introduce la nueva localidad del departamento: ");
	    dept.setLoc(sc.nextLine());

	    sesionCreada.update(dept);
	    try {
		tx.commit();
	    } catch (ConstraintViolationException cve) {
		System.out.println("El empleado ya existe");
	    }
	    

	} catch (ObjectNotFoundException onfe) {
	    System.out.println("El departamento solicitado no existe");
	}
    }

    private static void modificarEmpleado() {
	try {
	    SessionFactory sesion = NewHibernateUtil.getSessionFactory();
	    Session sesionCreada = sesion.openSession();
	    System.out.println("Introduzca el numero de empleado a modificar");
	    Empleados emple = (Empleados) sesionCreada.get(
		    Empleados.class, Short.parseShort(sc.nextLine()));
	    Transaction tx = sesionCreada.beginTransaction();
	    
	    //emple.toString();

	    System.out.print("Introduce el nuevo apellido del empleado: ");
	    emple.setApellido(sc.nextLine());
	    System.out.print("Introduce el nuevo oficio del empleado: ");
	    emple.setOficio(sc.nextLine());
	    System.out.print("Introduce el nuevo director del empleado: ");
	    emple.setDir(Short.parseShort(sc.nextLine()));
	    System.out.print("Introduce la nueva fecha de alta del empleado: ");
	    emple.setFechaAlt(Date.valueOf(sc.nextLine()));
	    System.out.print("Introduce el nuevo salario del empleado: ");
	    emple.setSalario(Float.parseFloat(sc.nextLine()));
	    System.out.print("Introduce la nueva comision del empleado: ");
	    emple.setComision(Float.parseFloat(sc.nextLine()));
	    System.out.print("Introduce el nuevo departamento del empleado: ");
	    emple.setDepartamentos(
		    (Departamentos) sesionCreada.get(Departamentos.class,
			    Byte.parseByte(sc.nextLine()))
	    );
	    sesionCreada.update(emple);

	    try {
		tx.commit();
	    } catch (ConstraintViolationException cve) {
		System.out.println("El empleado ya existe");
	    }
	    sesionCreada.close();

	} catch (ObjectNotFoundException onfe) {
	    System.out.println("El empleado no existe solicitado no existe");
	} catch (PropertyValueException pve){
	    System.out.println(pve.getMessage());
	}
	
    }

    private static void leerDepartamentos() {
	SessionFactory sesion = NewHibernateUtil.getSessionFactory();
        Session sesionCreada = sesion.openSession();
        Query q = sesionCreada.createQuery("from Departamentos");
        
        List<Departamentos> listaDepartamentos = q.list();
        Iterator<Departamentos> iterator = listaDepartamentos.iterator();
	while(iterator.hasNext()){
	    System.out.println(iterator.next());
	}
	sesionCreada.close();
    }

    private static void leerDepartamentosSevilla() {
	SessionFactory session = NewHibernateUtil.getSessionFactory();
	Session sesionCreada = session.openSession();
	
	String hql = "FROM Departamentos AS dep WHERE dep.loc='SEVILLA'";
	
	Query q = sesionCreada.createQuery(hql);
	
	List<Departamentos> listaDepartamentos = q.list();
	Iterator <Departamentos> iterator = listaDepartamentos.iterator();
	System.out.println("Departamentos de Sevilla");
	System.out.println("========================");
	while(iterator.hasNext()){
	    Departamentos dep = iterator.next();
	    System.out.println(dep);
	}
	sesionCreada.close();
    }

    private static void leerDepartamentosPorLocalidad() {
	SessionFactory session = NewHibernateUtil.getSessionFactory();
	Session sesioncreada = session.openSession();
	
	Scanner sc = new Scanner (System.in);
	
	System.out.print("Introduce la localidad del departamento: ");
	String localidad = sc.nextLine().toUpperCase();
	
	String hql = "FROM Departamentos AS dep WHERE loc =:localidadDepartamento";
	Query q = sesioncreada.createQuery(hql);
	q.setParameter("localidadDepartamento",localidad);
	List<Departamentos> lista = q.list();
	Iterator<Departamentos> iterator = lista.iterator();
	
	while(iterator.hasNext()){
	    System.out.println(iterator.next());
	}
	
	sesioncreada.close();
    }

    private static void leerDepartamentosPorLocalidadMultiple() {
	SessionFactory sesion = NewHibernateUtil.getSessionFactory();
	Session sesioncreada = sesion.openSession();
	
	Scanner sc = new Scanner(System.in);
	
	List<String> listaLocalidades = new ArrayList<String>();
	System.out.print("Introduce la localidad 1: ");
	listaLocalidades.add(sc.nextLine().toUpperCase());
	System.out.print("Introduce la localidad 2: ");
	listaLocalidades.add(sc.nextLine().toUpperCase());
	
	String hql = "FROM Departamentos"
		+ " AS dep "
		+ "WHERE dep.loc in "
		+ "(:listaDepartamentosLocalidad) ORDER BY dep.deptNo";
	Query q = sesioncreada.createQuery(hql);
	
	q.setParameterList("listaDepartamentosLocalidad", listaLocalidades);
	List <Departamentos> listaDepartamentos = q.list();
	Iterator<Departamentos> iterator = listaDepartamentos.iterator();
	System.out.println("Busqueda de departamentos por las localidades "+listaLocalidades.get(0)+" y "+listaLocalidades.get(1));
	while(iterator.hasNext()){
	    System.out.println(iterator.next());
	}
    }
}
