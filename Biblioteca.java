
package biblioteca;
import java.util.Date;
import java.util.*;
import static java.lang.System.*;

//Librerias y archivos para leer de Excel
import javax.swing.JFileChooser;
import java.io.FileInputStream;
import java.io.FileOutputStream;



import org.apache.poi.ss.usermodel.Cell;

import org.apache.poi.ss.usermodel.Row;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
//End Librerias y archivos para leer de Excel


/**
 * 
 * Clase Principal Biblioteca
 * 
 * 
 */
public class Biblioteca {
    private String nombre;
    private String ubicacion;
    private int telefono;
    private String bibliotecologo;
    private Date fechaActual;
    
    private int diasPrestamoLibro;
    private int diasPrestamoRevista;
    
    private String sitioWeb;

    // listas dinamicas
    private Vector<Libro> listaLibros =  new Vector<Libro>();
    private Vector<Revista> listaRevistas=  new Vector<Revista>();
    private Vector<Cliente> listaClientes=  new Vector<Cliente>();
    private Vector<Venta> listaVentas =  new Vector<Venta>();
    private Vector<Prestamo> listaPrestamo =  new Vector<Prestamo>();
    
    /**
     * Constructor para biblioteca
     * 
     * @param _nombre string Nombre de la Biblioteca
     * @param _ubicacion string ubicacion de la biblioteca
     * @param _telefono int telefono de la biblioteca 
     * @param _bibliotecologo string nombre del bibliotecologo 
     * @param _fechaActual Date que contiene la fecha al inciar al programa 
     * @param _diasPrestamoLibro Int que contiene la cantidad de dias prestamo para los libros
     * @param _diasPrestamoRevista Int que contiene la cantidad de dias prestamo para las revistas 
     */
    public Biblioteca(String _nombre, String _ubicacion, int _telefono
            , String _bibliotecologo, Date _fechaActual, String _sitio, int _diasPrestamoLibro,
            int _diasPrestamoRevista) {
        this.nombre = _nombre;
        this.ubicacion = _ubicacion;
        this.bibliotecologo = _bibliotecologo;
        this.telefono = _telefono;
        this.fechaActual = _fechaActual;
        this.sitioWeb = _sitio;
        
        this.diasPrestamoLibro = _diasPrestamoLibro;
        this.diasPrestamoRevista = _diasPrestamoRevista;
        
        
        
    }
    /**
     * Constructor para biblioteca, este metodo coloca los dias de prestamo por defecto: 10 en libro y 5 en revista
     *
     * @param _nombre string Nombre de la Biblioteca
     * @param _ubicacion string ubicacion de la biblioteca
     * @param _telefono int telefono de la biblioteca
     * @param _bibliotecologo string nombre del bibliotecologo
     * @param _fechaActual Date que contiene la fecha al inciar al programa
     * @param _sitio Sitio web de la biblioteca
     */
    public Biblioteca(String _nombre, String _ubicacion, int _telefono
            , String _bibliotecologo, Date _fechaActual,String _sitio) {
        this( _nombre,  _ubicacion,  _telefono
            ,  _bibliotecologo,  _fechaActual,_sitio,10,5);
        
        
        // PRUEBAS
        
        
        
        
    }

    // SETTERS Y GETTERS
    public void setNombre(String _nombre) {
        this.nombre = _nombre;
    }
    
    public String getNombre() {
        return this.nombre;
    }

    public void setUbicacion(String _ubicacion) {
        this.ubicacion = _ubicacion;
    }
    
    public String getUbicacion() {
        return this.ubicacion;
    } 
    
    public void setBibliotecologo(String _biblio) {
        this.bibliotecologo = _biblio;
    }
    
    public String getBibliotecologo() {
        return this.bibliotecologo;
    }
    
    public void setTelefono(int _telefono) {
        this.telefono = _telefono;
    }
    
    public int getTelefono() {
        return this.telefono;
    }
    
    public void setFechaActual(Date _fecha) {
        this.fechaActual = _fecha;
    }
    
    public Date getFechaActual() {
        return this.fechaActual;
    }
    
    public void setDiasPrestamoLibro(int _dias) {
        this.diasPrestamoLibro = _dias;
    }
    
    public int getDiasPrestamoLibro() {
        return this.diasPrestamoLibro;
    }
    
    public void setDiasPrestamoRevista(int _dias) {
        this.diasPrestamoRevista = _dias;
    }
    
    public int getDiasPrestamoRevista() {
        return this.diasPrestamoRevista;
    }
    
    public Vector<Libro> getListaLibros(){
        return listaLibros;
    }
    public Vector<Revista> getListaRevistas(){
        return listaRevistas;
    }
    public Vector<Prestamo> getListaPrestamo(){
        return listaPrestamo;
    }
    
    //VALIDACIONES
    
    /**
     * Metodo que valida que el numero de telefono tenga 8 digitos
     * 
     * @param _numTel numero de telefono
     * @return  Retorna si el numero de telefono es valido
     */
    private boolean validarTelefono(int _numTel) {
        String _telefono = Integer.toString(_numTel);
        return _telefono.length() == 8;
    }

    /**
     *
     * Metodo que recibe un libro y lo coloca de nuevo como Disponible
     * Devuelve solo un libro a la vez
     *
     * @param _cliente Recibe un cliente al que se le presto el libro
     * @param _libro Recibe el libro prestado
     */
    public void devolverLiteratura(Cliente _cliente, Articulo _libro){
        
    //... si el cliente esta registrado
        if (this.clienteRegistrado(_cliente.getId())) {
            
            
        }
        
    }
    /**
     * Toma un prestamo y determina si el cliente debe tener una multa
     * 
     * @param _prestamo recibe un prestamo
     * @return retorna True si el cliente de dicho prestamo tiene una multa
     */
    public boolean tieneMulta(Prestamo _prestamo) {
        
        
        int cantidadDiasPrestamo = _prestamo.getDuracionPrestamo();
        Date diaInicialPrestamo = _prestamo.getFechaInicial();
        int diasDesdePrestamo = this.diasEntreFechas(diaInicialPrestamo, fechaActual);
        
        if (cantidadDiasPrestamo < diasDesdePrestamo) {
            //... si los diferencia de dias es mayor a la establecidad
            // el usuario tiene una multa
            
            _prestamo.setDiasAtrso(this.getDiasMulta(_prestamo));
            
            return true;
           
        }
        return false;
        
    }
    
    public int getDiasMulta(Prestamo _prestamo) {
        int cantidadDiasPrestamo = _prestamo.getDuracionPrestamo();
        Date diaInicialPrestamo = _prestamo.getFechaInicial();
        int diasDesdePrestamo = this.diasEntreFechas(diaInicialPrestamo, fechaActual);
        
        if ((diasDesdePrestamo - cantidadDiasPrestamo) < 0)
            return 0;
        
        return diasDesdePrestamo - cantidadDiasPrestamo;
    
    }
    
    public Vector<Cliente> getListaClientes() {return this.listaClientes;}
    
    
    /**
     * Revisa los prestamos existente y si un cliente deberia de tener una multa
     * le cambia la morosidad a True a dicho Cliente.
     * 
     */
    public void revisarMorosidad() {
        
        int largo = listaPrestamo.size();
        for (int i = 0; i < largo; i++) {

            Prestamo prestamo = listaPrestamo.get(i);
            
            if (prestamo.getPrestAvtivo() && this.tieneMulta(prestamo)) {
                Articulo articulo = prestamo.getArticulo();
                Cliente clienteMoroso = articulo.getCliente();
                clienteMoroso.setMoroso(true); 
                System.out.println("Cliente moroso");
            }
        }
    }
    
    /**
     * Metodo que pone a todos sus clientes morosos y les envia un correo
     * 
     * 
     */
    public void consultarMulta() {
        
        // PUNTO 11 CALCULAR MULTAS
        
        
        this.revisarMorosidad();
        
        int largo = this.listaClientes.size();
        for (int i = 0; i < largo; i++) {
            Cliente cliente = this.listaClientes.get(i);
            if (cliente.getMoroso()) {
                this.enviarCorreoMultas(cliente.getId());
                
                // El sistema debe desplegar al usuario una lista de las personas
                // a las que se les envió el correo electrónico.

            
            }
        
        }
    }
    /**
     * Metodo utilizado cuando un cliente ya esta moroso, recibe el ID del cliente 
     * y le envia un correo con todos los articulos que tienen una multa
     * @param _idCliente 
     */
    public void enviarCorreoMultas(int _idCliente){
        String Mensaje = "Buenas, a continuacion se le dara una lista con los "
                + "articulos a los tiene que pagar una multa:  " + "\n";
        
        Vector<Prestamo> listaPrestamos = this.getListaPrestamos(_idCliente);
        
        long multaTotal = 0;
        
        
        int largo = listaPrestamos.size();
        for (int i = 0;i < largo; i++) {
            
            Prestamo prestamo = listaPrestamos.get(i);
            long multa = prestamo.calcularMulta(this.getDiasMulta(prestamo));
            String nombreArticulo = prestamo.getArticulo().getNombre();
            Mensaje += nombreArticulo + "\n*Multa por este Articulo: " + 
                        Long.toString(multa);
            multaTotal += multa;
        } 
        Mensaje += "\nMulta TOTAL a pagar: " + Long.toString(multaTotal);
        
        Cliente cliente = this.retCliente(_idCliente);
        
        Prestamo.EnviarCorreo(cliente.getCorreo(), Mensaje);
    
    }
    
    
    
    /**
     * 
     * @param _idCliente 
     */
    public String mensajePagarMultas(int _idCliente) {
        
        this.revisarMorosidad();
        
        if (this.clienteRegistrado(_idCliente) && this.retCliente(_idCliente).getMoroso() ) {
            String Mensaje = "Buenas, a continuacion se le dara una lista con los "
                + "articulos a los tiene que pagar una multa:" + "\n";

            Vector<Prestamo> listaPrestamos = this.getListaPrestamos(_idCliente);

            long multaTotal = 0;


            int largo = listaPrestamos.size();
            for (int i = 0;i < largo; i++) {

                Prestamo prestamo = listaPrestamos.get(i);
                long multa = prestamo.calcularMulta(this.getDiasMulta(prestamo));
                String nombreArticulo = prestamo.getArticulo().getNombre();
                Mensaje += "\nNombre: " + nombreArticulo + "\nMulta por este Articulo: " + 
                            Long.toString(multa);
                multaTotal += multa;
            } 
            Mensaje += "\nMulta TOTAL a pagar: " + Long.toString(multaTotal);
            return Mensaje;
           
        }
        return "";
    }
    
    public void pagarMulta(int _idCliente) {
        
        if (this.clienteRegistrado(_idCliente) && this.retCliente(_idCliente).getMoroso() ) {
            
            Cliente cliente = this.retCliente(_idCliente);
            cliente.setMoroso(false);
            Vector<Prestamo> listaPrestamos = this.getListaPrestamos(_idCliente);
            int largo = listaPrestamos.size();
            for (int i = 0;i < largo; i++) {

                Prestamo prestamo = listaPrestamos.get(i);
                prestamo.setPrestActivo(false);
                prestamo.getArticulo().CambiarEstado();
            } 
        }
    }    
    
    
    
    /**
     * Funcion que recibe el id de un cliente y da una lista de los prestamos del
     * Cliente (prestamos activos)
     * 
     * @param _idCliente recibe el id de un cliente
     * @return retorna una lista con los prestamos asociados a dicho clinte 
     */
    public Vector<Prestamo> getListaPrestamos(int _idCliente) {
        Vector<Prestamo> listaPrestamos = new Vector<Prestamo>();
        
        int largo = listaPrestamo.size();
        for (int i = 0; i < largo; i++) {

            Prestamo prestamo = listaPrestamo.get(i);

            Articulo articulo = prestamo.getArticulo();
            Cliente cliente = articulo.getCliente();
            if (cliente.getMoroso() && cliente.getId() == _idCliente && 
                    prestamo.getPrestAvtivo()) {
                listaPrestamos.add(prestamo);
            }

        }
        return listaPrestamos;    
    }
    
    /**
     * Funcion que recibe el id de un cliente y da una lista de los prestamos del
     * Cliente
     * 
     * @param _idCliente recibe el id de un cliente
     * @return retorna una lista con los prestamos asociados a dicho clinte 
     */
    public Vector<Prestamo> getListaPrestamosTotales(int _idCliente) {
        Vector<Prestamo> listaPrestamos = new Vector<Prestamo>();
        
        int largo = listaPrestamo.size();
        for (int i = 0; i < largo; i++) {

            Prestamo prestamo = listaPrestamo.get(i);

            Articulo articulo = prestamo.getArticulo();
            Cliente cliente = articulo.getCliente();
            if (cliente.getId() == _idCliente) {
                listaPrestamos.add(prestamo);
            }

        }
        return listaPrestamos;    
    }
    
    
    
    
    
    /**
     * Metodo que recibe dos fechas y retorna la cantidad de dias entre una fecha
     * y otra
     * 
     * @param _fechai  una fecha Inicial tipo Date
     * @param _fechaf  una fecha Final tipo Date
     * @return retorna un entero el cual es la cantidad de dias entre ambos
     */
    public int diasEntreFechas(Date _fechai, Date _fechaf) {
        
        if (_fechai.compareTo(_fechaf) == 0 ) {
            return 0;
        }
        
        int days1 = (int) (_fechai.getTime()/(60*60*24*1000));
        int days2 = (int) (_fechaf.getTime()/(60*60*24*1000));
        
       
        
        return days2-days1;
        
        /*
            
        String fechaNueva = "24-04-2017";
        Date inputDate = new Date();
        
        java.text.DateFormat dateFormat = new java.text.SimpleDateFormat("dd-MM-yyyy");
        
        try{
            inputDate = dateFormat.parse(fechaNueva);
        }catch (java.text.ParseException e){
            e.printStackTrace();
        }

        int x = this.diasEntreFechas(this.fechaActual,inputDate);
        System.out.print(x);
        */
    }
    
    
    /**
     * Metodo que devuelve un articulo si este no esta moroso
     * 
     * @param _idCliente id del cliente que devolvera un libro  
     * @param _articuloADevolver articulo que el cliente va a devolver
     * @return retorna true si el devolver articulo fue efectivo
     */
    public boolean devolverArticulo(int _idCliente, Articulo _articuloADevolver) {
        this.revisarMorosidad();
        
        if (this.clienteRegistrado(_idCliente) ){
            //_articuloADevolver.CambiarEstado();
            int largo = this.listaPrestamo.size();
            for (int i = 0; i < largo; i++) {
                Prestamo prestamo = this.listaPrestamo.get(i);
                if (prestamo.getArticulo().getCliente().getId() == _idCliente &&
                        prestamo.getDiasAtraso() == 0) {
                    prestamo.setPrestActivo(false);
                    _articuloADevolver.CambiarEstado();
                    return true;
                  
             
                } else if (prestamo.getArticulo().getCliente().getId() == _idCliente &&
                        prestamo.getDiasAtraso() != 0) {
                    return false;
                }
            
            }
                    
        }
        return true;
    }
    
    
    
    /**
     *
     * Metodo que valida que el correo tenga un @ y un cuerpo valido
     *
     * @param _correoE Recibe un string el cual es un correo electronico 
     * @return Retorna si el string es un correo adecuado
     */
    private boolean validarCorreo(String _correoE) {
        int bandera = 0;
        
        if (!this.validarString(_correoE))
            return false;
        
        if (_correoE.length() == 0 )
            return false;

        if (_correoE.charAt(0) == '@')
            return false;

        for (int i = 0; i < _correoE.length(); i++){
            char letra = _correoE.charAt(i);

            if (letra == '@'){
                if (_correoE.charAt(i + 1) == ' ') {
                    return false ;
                }
                bandera = 1;
            }
        }
        if (bandera != 1) {
            return false;
        }
        
        return true;
    }
   // metodo utilizado para Nombre, Autor etc. Cuando se encesita un String sin 
   // numeros 
    
    
    /**
     *
     * Metodo que valida que la entrada sea un string
     *
     * @param _string recibe un string
     * @return Retorna true si es un string valido
     */
    private boolean validarString(String _string) {
        if (_string != null && _string.trim().length() > 0 && 
        this.validarStringSinNumeros(_string))
            // si el string ingresado no es vacio, no es nullo y ademas no 
            // contiene numeros (por que es el nombre de un autor o un editor etc)
            return true;
        return false;
        
    }

    /**
     *
     * Metodo que valida que la entrada sea un genero de un libro
     *
     * @param _tipo Recibe un string el cual es el tipo de libro 
     * @return Retorna true si es (novela , teatro, poesia, infantil, o ensayo)
     */
    private boolean validarTipoLibro(String _tipo){
        if (!this.validarString(_tipo))
            return false;
        
        switch (_tipo) {
            case "novela":
                return true;
                
            case "teatro":
                return true;
                
            case "poesia":
                return true;
               
            case "infantil":
                return true;
                
            case "ensayo":
                return true;
        }
        return false;
    }
    
    /**
     *
     * Metodo que valida que el string de entrada no tenga numeros
     *
     * @param _string recibe un string
     * @return  retorna true si el string no contiene numeros
     */
    private boolean validarStringSinNumeros(String _string) {
        for (int i = 0; i < _string.length(); i++) {
            char letra = _string.charAt(i);
            switch (letra) {
                case '0':
                    return false;
                case '1':
                    return false;
                case '2':
                    return false;
                case '3':
                    return false;
                case '4':
                    return false;
                case '5':
                    return false;
                case '6':
                    return false;
                case '7':
                    return false;
                case '8':
                    return false;
                case '9':
                    return false;
            }   
        }
        return true;
    }

    /**
     *
     * Metodo que retorna el Objeto cliente por el numero de entrada
     *
     * @param _id recibe un id (entero)  
     * @return retorna el objeto cliente con el ID dado
     */
    public Cliente retCliente(int _id){  //Busca un cliente segun el Id y lo retorna

        int largo = listaClientes.size();
        for (int i = 0; i < largo; i++) {

            Cliente cliente = listaClientes.get(i);

            if (cliente.getId() == _id) {
                return cliente;
            }

        }
        return null; //Si no tiene un return por defecto, no compila
    }
    
    public Libro retLibro(String _id){  //Busca un Libro segun el Id y lo retorna

        int largo = listaLibros.size();
        for (int i = 0; i < largo; i++) {

            Libro libro = listaLibros.get(i);

            if (libro.getId() == _id) {
                return libro;
            }

        }
        return null; //Si no tiene un return por defecto, no compila
    }
    

    // REGISTRA EL CLIENTE
    
    /**
     *
     * Si el Usuario da alguna da algun parametro erroneo del cliente
     * o el identificador ya existe en el sistema, no se registra
     * 
     * @param _nombre nombre del cliente string
     * @param _correo correo del cliente string
     * @param _id ID del cliente, int 
     * @param _telefono num de telefono del cliente, int
     *
     * 
     */
    public void registrarCliente(String _nombre, String _correo, int _id, int 
            _telefono ){
        if (this.validarString(_nombre) && this.validarCorreo(_correo) && 
                this.validarTelefono(_telefono)){
            if (!this.clienteRegistrado(_id)) {
                // si no existe el cliente 
                // crea el cliente
                Cliente clienteNuevo = new Cliente( _nombre, _correo, _id, _telefono);
                this.listaClientes.add(clienteNuevo);
                out.println("Cliente ingresado satisfactoriamente");
            
            }else{
                out.println("La identificacion del cliente a registrar ya existe");
            }
            
        } else {
        
            out.println("Error al crear el cliente");
        }
    }
    /**
     *
     * Registra un libro en el sistema de forma manual.
     * Revisa que el nombre del libro, la editorial y el autor sean entradas validas,
     * si lo son, registra el libro.
     *
     * @param _nombre string nombre del Libro (no puede contener numeros)
     * @param _autor string nombre del autor 
     * @param _año int año del libro
     * @param _editorial string editorial del libro
     * @param _genero  string genero del libro
     *
     */
    public void registrarLibro(String _nombre, String _autor, int _año, String
            _editorial, Genero _genero) {
        
        if (this.validarString(_autor) && this.validarString(_editorial) 
            && this.validarString(_nombre) ) {
            // crear el Libro y lo agrega a la lista
            Libro libroNuevo = new Libro(_nombre, _autor, _año, _editorial, _genero);
            this.listaLibros.add(libroNuevo);
            
        } else {
            out.println("Error en los datos dados para registrar el Libro");
        
        }
    }

    /**
     *
     * Registra una revista en el sistema de forma manual.
     * Revisa que el nombre de la revista sea una entrada valida,
     * si lo es, registra el libro.
     *
     * @param _nombre string nombre de la Revista
     * @param _numero int codigo de la Revista
     * @param _año int año de la Revista
     * @param _categoria string tipo de revista (para venta o para prestamo)
     * @param _costo  int costo de las revistas para ventas
     */
    public void registrarRevista(String _nombre, int _numero, int _año, String
            _categoria, int _costo) {
      if (this.validarString(_nombre)){
          Revista revistaNueva = new Revista(_nombre, _numero, _año, _categoria, _costo);
          this.listaRevistas.add(revistaNueva);

      }  else {
          out.println("Error en los datos dados para registrar la Revista");
      }
    }
    
            
            
    // usado para verificar si el cliente ya esta registrado en el sistema
    /**
     *
     * Metodo que revisa si existe un cliente ya registrado en el sistema
     *
     * @param _id int Identificador del cliente a registrar
     * @return retorna si el cliente se encuentra en el registro de clientes o no
     */
    public boolean clienteRegistrado(int _id) {
        int largo = this.listaClientes.size();
        if (largo>0) {
            for (int i = 0; i < largo; i++) {
                if (this.listaClientes.get(i).getId() == _id) {
                    return true;
                }
            }
        }
        return false;
    }


    /**
     *
     * Metodo que realiza la venta de una revista, se crea un objeto Venta y se inserta en el registro
     *
     * @param _idCliente int Identificador del cliente
     * @param _revista Objeto Revista que va a ser comprada por un cliente
     */
    public void venderRevista(int _idCliente, Revista _revista) {

         if ( clienteRegistrado( _idCliente) && _revista.getEstado() == Estado.Disponible) {
             //System.out.println("HOLA");
             Cliente cliente = retCliente(_idCliente);
             _revista.setEstado(Estado.Vendida);
             _revista.setCliente(cliente);

           Venta nuevaVenta = new Venta(fechaActual,this.listaRevistas, cliente, _revista.getNombre());
           listaVentas.add(nuevaVenta);

         }
         else{
            throw new java.lang.IllegalArgumentException();
         }

    }

    /**
     *
     * Metodo que realiza el prestamo de una revista, se crea un Objeto prestamo y se inserta en el registro
     *
     * @param _idCliente int Identificador del cliente
     * @param _revista Objeto Revista que va a ser comprada por un cliente
     */
    public void prestarRevista(int _idCliente, Revista _revista) {
        if ( clienteRegistrado( _idCliente) ) {
            Cliente cliente = retCliente(_idCliente);
            if (!cliente.getMoroso()) {
                _revista.setEstado(Estado.Prestada);
                _revista.setCliente(cliente);

                Prestamo nuevoPrest = new Prestamo (fechaActual, _revista, diasPrestamoRevista);
                listaPrestamo.add(nuevoPrest);

            }
        }
    }

    /**
     *
     * Metodo que realiza el prestamo de un libro, se crea un Objeto prestamo y se inserta en el registro
     *
     * @param _idCliente int Identificador del cliente
     * @param _libro Objeto Libro que va a ser comprada por un cliente
     */
    public void prestarLibro(int _idCliente, Libro _libro) {
        if ( clienteRegistrado( _idCliente)&& !retCliente( _idCliente).getMoroso()) {  //falta una segunda expresion despues del &&
            Cliente cliente = retCliente(_idCliente);
            if (!cliente.getMoroso()) {
                _libro.CambiarEstado();  //No estaba escrito el metodo en la clase libro
                _libro.setCliente(cliente);

                Prestamo nuevoPrest = new Prestamo (fechaActual, _libro, diasPrestamoLibro);
                nuevoPrest.setPrestActivo(true);
                listaPrestamo.add(nuevoPrest);

            }
        }
        else{
            throw new java.lang.IllegalArgumentException();
        //System.out.println("No existe el cliente");
        }
    }

    
    // antes de dar estos parametros se deben de ingresar un entero correcto
    /**
     *
     * Metodo que cambia la fecha del sistema y los dias de prestamo de los libros y revistas
     *
     * @param _fechaActual Date fecha del sistema a cambiar
     * @param _diasPrestamoLibro int Dias de prestamo para un libro
     * @param _diasPrestamoRevista int Dias de prestamo para una revista
     */
    public void cambiarParametros(Date _fechaActual, int _diasPrestamoLibro, 
            int _diasPrestamoRevista) {
        
        this.fechaActual = _fechaActual;
        this.diasPrestamoLibro = _diasPrestamoLibro;
        this.diasPrestamoRevista = _diasPrestamoRevista;
    }

    /**
     *
     * Metodo que revisa el estado de todos los libros, el estado de los libros que sean iguales a la entrada
     * se insertan en una lista aparte para hacer un filtro
     *
     * @param _estado Estado Indica el estado a revisar de los libros
     */
    public void consultarLibro(Estado _estado) {
        // crear la lista
        Vector<Libro> listaConsultada = new Vector<Libro>(); 
        
           for (int i = 0; i < this.listaLibros.size(); i++) {
               // si el libro tiene el mismo estado que esta recibiendo el metodo
               if (this.listaLibros.get(i).getEstado() == _estado) {

		// método get estado :)
                   listaConsultada.add(this.listaLibros.get(i));
               }   
           }
           // aca se puede hacer una funcion que lo que hace es imprimir todo 
           // en la interfaz
    
    }

    /**
     *
     * Metodo que revisa el estado de todas las revistas, el estado de las revistas que sean iguales a la entrada
     * se insertan en una lista aparte para hacer un filtro
     *
     * @param _estado Estado Indica el estado a revisar en las revistas
     */
    public void consultarRevista(Estado _estado) {
        // crear la lista
        Vector<Revista> listaConsultada = new Vector<Revista>(); 
        
           for (int i = 0; i < this.listaLibros.size(); i++) {
               // si el libro tiene el mismo estado que esta recibiendo el metodo
               if (this.listaRevistas.get(i).getEstado() == _estado) {

		// método get estado :)
                   listaConsultada.add(this.listaRevistas.get(i));
               }   
           }
           // aca se puede hacer una funcion que lo que hace es imprimir todo 
           // en la interfaz
    
    }

    
    /**
     *
     * Metodo que lee un archivo de Excel para insertar libros y revistas al sistema
     *
     */
    public void LeerExcel() {
                String c1Val = "";
                String f1Val = "";
                double f1Val2 = 0.0;
                double c1Val2 = 0.0;
		try {
                        final JFileChooser fc = new JFileChooser();
                        fc.showOpenDialog(fc);
                        
                        java.io.File file = fc.getSelectedFile();
                        String path = fc.getCurrentDirectory().getAbsolutePath();
                        System.out.println(path);
                        String Filename = fc.getName(file);
                        
			FileInputStream fileInputStream = new FileInputStream(path + "/" + Filename);
			XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
			XSSFSheet worksheet = workbook.getSheetAt(0);

			
                        int cont = 0;
                        while (true){
                            XSSFRow row1 = worksheet.getRow(cont);
                            XSSFCell cellA1 = row1.getCell((short) 0);
                            String a1Val = cellA1.getStringCellValue();

                            XSSFCell cellB1 = row1.getCell((short) 1);
                            String b1Val = cellB1.getStringCellValue();
                            
                            try{
                                XSSFCell cellC1 = row1.getCell((short) 2);
                                c1Val = cellC1.getStringCellValue();
                                
                            } catch(IllegalStateException e){
                                XSSFCell cellC1 = row1.getCell((short) 2);
                                c1Val2 = cellC1.getNumericCellValue();
                            }
                            
                            XSSFCell cellD1 = row1.getCell((short) 3);
                            double d1Val = cellD1.getNumericCellValue();

                            XSSFCell cellE1 = row1.getCell((short) 4);
                            String e1Val = cellE1.getStringCellValue();
                            
                            try{
                                XSSFCell cellF1 = row1.getCell((short) 5);
                                f1Val = cellF1.getStringCellValue();
                            } catch(IllegalStateException e){
                                XSSFCell cellF1 = row1.getCell((short) 5);
                                f1Val2 = cellF1.getNumericCellValue();
                            }
                            System.out.println("A1: " + a1Val);
                            System.out.println("B1: " + b1Val);
                            System.out.println("C1: " + c1Val);
                            System.out.println("C1: " + c1Val2);
                            System.out.println("D1: " + d1Val);
                            System.out.println("E1: " + e1Val);
                            System.out.println("F1: " + f1Val + "\n");
                            System.out.println("F1: " + f1Val2);
                            cont++;
                            Genero generoLibro = Genero.Ensayo;
                            
                            switch(f1Val){
                                case "Ensayo": generoLibro = Genero.Ensayo;
                                break;
                                case "Novela": generoLibro = Genero.Novela;
                                break;
                                case "Infantil": generoLibro = Genero.Infantil;
                                break;
                                case "Poesia": generoLibro = Genero.Poesia;
                                break;
                                case "Teatro": generoLibro = Genero.Teatro;
                                break;
                                
                            }
                            
                            int intd1Val = (int) d1Val;
                            
                            if (a1Val.equals("Libro")){
                                registrarLibro(b1Val, c1Val, intd1Val, e1Val, generoLibro);
                            }
                            else{
                                if (a1Val.equals("Revista")){
                                int intf1Val2 = (int) f1Val2;
                                int intc1Val2 = (int) c1Val2;
                                registrarRevista(b1Val, intc1Val2, intd1Val, e1Val, intf1Val2);
                                }
                            }
                        }
                        
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		//} catch (NullPointerException e){
                  //      System.out.println("Yap");
                }
                
	}
}
    //} //con estas llaves me pedia una clase, un enum o una interfaz y por ende error xq no habia nada


/* Bibliografia
https://poi.apache.org/apidocs/org/apache/poi/ss/usermodel/Row.html
https://docs.oracle.com/javase/7/docs/api/java/io/File.html
Para descargar xmlbeans: http://www.java2s.com/Code/Jar/x/Downloadxmlbeans230jar.htm
https://mvnrepository.com/artifact/org.apache.commons/commons-collections4/4.0
*/
    
    
    
//}

//public void validarDatos(String pNombre, String pCorreo, String pCedula)
