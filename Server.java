package Servidor;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.Vector;

import java.net.*;
import java.io.*;
/**
 * Created by Sergio Hidalgo Fonseca on 14/6/2017.
 */
public class Server {
    //Sockets
    
    
    final int PUERTO=5000;
    ServerSocket sc;
    Socket so;
    DataOutputStream salida;
    String mensajeRecibido;
    
    public void initServer(InterfazServidor gui){
    BufferedReader entrada;
    try{
    sc = new ServerSocket(PUERTO );/* crea socket servidor que escuchara en puerto 5000*/
    so=new Socket();
    System.out.println("Esperando una conexión:");
    //while (true) {
            /*try {
                so = sc.accept();
                System.out.println("Un cliente se ha conectado.");
            } catch (IOException e) {
                System.out.println("I/O error: " + e);
            }*/
            // new threa for a client
            //Thread socketServer = new Thread(new Runnable());
            SocketThread socketserver = new SocketThread(gui, this, so, sc);
            socketserver.start();         
            //new SocketThread(so).start();
        //}
    
        
        
    /*
    so = sc.accept();
    //Inicia el socket, ahora esta esperando una conexión por parte del cliente
    System.out.println("Un cliente se ha conectado.");
    //Canales de entrada y salida de datos
    entrada = new BufferedReader(new InputStreamReader(so.getInputStream()));
    salida = new DataOutputStream(so.getOutputStream());
    System.out.println("Confirmando conexion al cliente....");
    salida.writeUTF("Conexión exitosa...n envia un mensaje :D");
    //Recepcion de mensaje
    mensajeRecibido = entrada.readLine();
    System.out.println(mensajeRecibido);
    salida.writeUTF("Se recibio tu mensaje.n Terminando conexion...");
    salida.writeUTF("Gracias por conectarte, adios!");
    System.out.println("Cerrando conexión...");
    sc.close();//Aqui se cierra la conexión con el cliente
    */
    }catch(Exception e ){
        System.out.println("Err000r: "+e.getMessage());
        }
    }
    //Fin Sockets
    
    private Vector<Ticket> ticketsList =  new Vector<>();
    private Vector<Employee> employeesList = new Vector<>();
    private Vector<Ticket> RedTickestList =  new Vector<>();
    private Vector<Ticket> GreenTicketsList =  new Vector<>();
    private Vector<Ticket> YellowTicketsList =  new Vector<>();
    private String excelFileName = "";
    private String excelFilePath = "";
    
    private static Server INSTANCE = null;
    
    private Server() {}
    
    private synchronized static void createInstance() {
        if (INSTANCE == null)
            INSTANCE = new Server();
    }
    
    public static Server getInstance() {
        createInstance();
        return INSTANCE;
    }



    public void readExcel() {
        try {
            final JFileChooser fc = new JFileChooser();
            fc.showOpenDialog(fc);

            java.io.File file = fc.getSelectedFile();
            String path = fc.getCurrentDirectory().getAbsolutePath();
            excelFilePath = path;
            System.out.println(path);
            String Filename = fc.getName(file);
            excelFileName = Filename;

            FileInputStream fileInputStream = new FileInputStream(path + "/" + Filename);
            //excelFilePath = "C:/Users/CASA/Desktop/PrograTresPOO/POO-PrograsDOSyTRES";
            //excelFileName = "TICKETS.xlsx";
            //FileInputStream fileInputStream = new FileInputStream("C:/Users/CASA/Desktop/PrograTresPOO/POO-PrograsDOSyTRES/TICKETS.xlsx");
            XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
            XSSFSheet worksheet = workbook.getSheetAt(0);


            int cont = 1;
            while (true){
                XSSFRow row = worksheet.getRow(cont);

                if(row != null) {
                    XSSFCell cellB1 = row.getCell((short) 1);
                    double b1Val = cellB1.getNumericCellValue();

                    XSSFCell cellC1 = row.getCell((short) 2);
                    String c1Val = cellC1.getStringCellValue();

                    int b1 = (int) b1Val;

                    System.out.println("B1: " + b1Val);
                    System.out.println("C1: " + c1Val);
                    cont++;

                    addTicket(b1, c1Val);
                }
                else{
                    break;
                }

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            //} catch (NullPointerException e){
            //      System.out.println("Yap");
        }
       return;
    }

    public void saveExcel() {
        try {
            FileInputStream fileInputStream = new FileInputStream(excelFilePath + "/" + excelFileName);
            XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
            XSSFSheet worksheet = workbook.getSheetAt(0);


            int cont = 1;
            for(int index = 0; RedTickestList.get(index)!=null; index++){
                Ticket _ticket = RedTickestList.get(index);
                XSSFRow row = worksheet.getRow(cont);

                if(_ticket.getTicketStatus() == TicketStatus.RESOLVED){
                    XSSFCell cellA1 = row.getCell((short) 0);
                    cellA1.setCellValue(_ticket.getDateReceivedString() + " - " + _ticket.getTimeReceived());

                    XSSFCell cellB1 = row.getCell((short) 1);
                    cellB1.setCellValue(_ticket.getClientID());

                    XSSFCell cellC1 = row.getCell((short) 2);
                    cellC1.setCellValue(_ticket.getSubject());

                    XSSFCell cellD1 = row.getCell((short) 3);
                    cellD1.setCellValue(_ticket.getTicketID());

                    XSSFCell cellE1 = row.getCell((short) 4);
                    cellE1.setCellValue(_ticket.getTicketCategoryStr());

                    XSSFCell cellF1 = row.getCell((short) 5);
                    cellF1.setCellValue(_ticket.getEmployeeID());

                    XSSFCell cellG1 = row.getCell((short) 6);
                    cellG1.setCellValue(_ticket.getDateResolvedString() + " - " + _ticket.getTimeResolvedString());

                    XSSFCell cellH1 = row.getCell((short) 7);
                    cellH1.setCellValue(_ticket.getTime());

                    XSSFCell cellI1 = row.getCell((short) 8);
                    cellI1.setCellValue(_ticket.getTicketComment());

                    XSSFCell cellJ1 = row.getCell((short) 9);
                    cellJ1.setCellValue(_ticket.getTicketStatusStr());

                    cont++;
                } else {

                    XSSFCell cellA1 = row.getCell((short) 1);
                    cellA1.setCellValue(_ticket.getDateReceivedString() + " - " + _ticket.getTimeReceived());

                    XSSFCell cellB1 = row.getCell((short) 1);
                    cellB1.setCellValue(_ticket.getClientID());

                    XSSFCell cellC1 = row.getCell((short) 2);
                    cellC1.setCellValue(_ticket.getSubject());

                    cont++;
                }

            }

            for(int index1 = 0; YellowTicketsList.get(index1)!=null; index1++){
                Ticket _ticket = YellowTicketsList.get(index1);
                XSSFRow row = worksheet.getRow(cont);

                if(_ticket.getTicketStatus() == TicketStatus.RESOLVED){
                    XSSFCell cellA1 = row.getCell((short) 0);
                    cellA1.setCellValue(_ticket.getDateReceivedString() + " - " + _ticket.getTimeReceived());

                    XSSFCell cellB1 = row.getCell((short) 1);
                    cellB1.setCellValue(_ticket.getClientID());

                    XSSFCell cellC1 = row.getCell((short) 2);
                    cellC1.setCellValue(_ticket.getSubject());

                    XSSFCell cellD1 = row.getCell((short) 3);
                    cellD1.setCellValue(_ticket.getTicketID());

                    XSSFCell cellE1 = row.getCell((short) 4);
                    cellE1.setCellValue(_ticket.getTicketCategoryStr());

                    XSSFCell cellF1 = row.getCell((short) 5);
                    cellF1.setCellValue(_ticket.getEmployeeID());

                    XSSFCell cellG1 = row.getCell((short) 6);
                    cellG1.setCellValue(_ticket.getDateResolvedString() + " - " + _ticket.getTimeResolvedString());

                    XSSFCell cellH1 = row.getCell((short) 7);
                    cellH1.setCellValue(_ticket.getTime());

                    XSSFCell cellI1 = row.getCell((short) 8);
                    cellI1.setCellValue(_ticket.getTicketComment());

                    XSSFCell cellJ1 = row.getCell((short) 9);
                    cellJ1.setCellValue(_ticket.getTicketStatusStr());

                    cont++;

                } else {

                    XSSFCell cellA1 = row.getCell((short) 1);
                    cellA1.setCellValue(_ticket.getDateReceivedString() + " - " + _ticket.getTimeReceived());

                    XSSFCell cellB1 = row.getCell((short) 1);
                    cellB1.setCellValue(_ticket.getClientID());

                    XSSFCell cellC1 = row.getCell((short) 2);
                    cellC1.setCellValue(_ticket.getSubject());

                    cont++;

                }
            }

            for(int index2 = 0; GreenTicketsList.get(index2)!=null; index2++){
                Ticket _ticket = GreenTicketsList.get(index2);
                XSSFRow row = worksheet.getRow(cont);

                if(_ticket.getTicketStatus() == TicketStatus.RESOLVED){
                    XSSFCell cellA1 = row.getCell((short) 0);
                    cellA1.setCellValue(_ticket.getDateReceivedString() + " - " + _ticket.getTimeReceived());

                    XSSFCell cellB1 = row.getCell((short) 1);
                    cellB1.setCellValue(_ticket.getClientID());

                    XSSFCell cellC1 = row.getCell((short) 2);
                    cellC1.setCellValue(_ticket.getSubject());

                    XSSFCell cellD1 = row.getCell((short) 3);
                    cellD1.setCellValue(_ticket.getTicketID());

                    XSSFCell cellE1 = row.getCell((short) 4);
                    cellE1.setCellValue(_ticket.getTicketCategoryStr());

                    XSSFCell cellF1 = row.getCell((short) 5);
                    cellF1.setCellValue(_ticket.getEmployeeID());

                    XSSFCell cellG1 = row.getCell((short) 6);
                    cellG1.setCellValue(_ticket.getDateResolvedString() + " - " + _ticket.getTimeResolvedString());

                    XSSFCell cellH1 = row.getCell((short) 7);
                    cellH1.setCellValue(_ticket.getTime());

                    XSSFCell cellI1 = row.getCell((short) 8);
                    cellI1.setCellValue(_ticket.getTicketComment());

                    XSSFCell cellJ1 = row.getCell((short) 9);
                    cellJ1.setCellValue(_ticket.getTicketStatusStr());

                    cont++;

                } else {

                    XSSFCell cellA1 = row.getCell((short) 1);
                    cellA1.setCellValue(_ticket.getDateReceivedString() + " - " + _ticket.getTimeReceived());

                    XSSFCell cellB1 = row.getCell((short) 1);
                    cellB1.setCellValue(_ticket.getClientID());

                    XSSFCell cellC1 = row.getCell((short) 2);
                    cellC1.setCellValue(_ticket.getSubject());

                    cont++;

                }
            }

            for(int index3 = 0; ticketsList.get(index3)!=null; index3++){
                Ticket _ticket = ticketsList.get(index3);
                XSSFRow row = worksheet.getRow(cont);

                XSSFCell cellA1 = row.getCell((short) 1);
                cellA1.setCellValue(_ticket.getDateReceivedString() + " - " + _ticket.getTimeReceived());

                XSSFCell cellB1 = row.getCell((short) 1);
                cellB1.setCellValue(_ticket.getClientID());

                XSSFCell cellC1 = row.getCell((short) 2);
                cellC1.setCellValue(_ticket.getSubject());

                cont++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            //} catch (NullPointerException e){
            //      System.out.println("Yap");
        }
    }



    /**
     * Creates a ticket and adds it to the List of tickets in the server
     * 
     * @param _clientID ID of the client that creates the ticket.
     * @param _subjet the ticket's subject.
     * @param _ticketID The ticket's ID.
     */
    void addTicket(int _clientID, String _subjet) {
        Ticket newTicket = new Ticket( _clientID,  _subjet);
        this.ticketsList.add(newTicket);
        
    }
    
    /**
     * Method returns the List of Tickets in a period of time
     * 
     * @param _date1i Date 1
     * @param _date2f Date 2
     * @return 
     */
    public Vector<Ticket> getTicketsInDates(Date _date1i, Date _date2f) {
        
        Vector<Ticket> newListTickets =  new Vector<>();
        Ticket ticket;
        for (int i = 0; i < this.ticketsList.size(); i++) {
            ticket = this.ticketsList.get(i);
            if (_date1i.compareTo(ticket.getDateReceived()) >= 0 && 
                    _date2f.compareTo(ticket.getDateReceived()) <= 0) {
                newListTickets.add(ticket);
            }    
        }       
        return newListTickets;
    }
    

    /**
     * Method used in updateTicket, updates the information of a ticket.
     * 
     * @param _ticketID Ticket ID used.
     * @param _complain The complain string.
     * @param _secondsSpent Time spent in seconds working on this specific ticket.
     * @param _resolvedComment Comment made by the Employee who solved the ticket.
     * @param _dateResolved  Date when the ticket was solved.
     */
    public void ticketDetails(int _ticketID, String _complain, int _secondsSpent, 
            String _resolvedComment, Date _dateResolved) {
        
        Ticket ticket;
        int timeSpent = 0;
        for (int i = 0; i < this.ticketsList.size(); i++) {
            ticket = this.ticketsList.get(i);
            if (ticket.getTicketID()== _ticketID) {
                ticket.setComentary(_resolvedComment);
                timeSpent = ticket.getAttentionMetric(ticket.getTicketCategory(), timeSpent);
                ticket.setTimeSolved(_secondsSpent);
                ticket.setDateResolved(_dateResolved);
                ticket.setComplain(_complain);
                
            }
            
        }    
        
            
    }
    
    /**
     * This method is only used when a ticket has been solved by an employee.
     * It updates some information on the ticket and changes the Ticket status to
     * SOLVED.
     * 
     * @param _employeeiD The employee who solved the ticket.
     * @param _ticketID The ID of the ticket solved.
     * @param _complain The complain string.
     * @param _secondsSpent Time spent in seconds working on this specific ticket.
     * @param _resolvedComment Comment made by the Employee who solved the ticket.
     * @param _dateResolved Date when the ticket was solved.
     */
    public void updateTicket(int _employeeiD,int _ticketID, String _complain, int _secondsSpent, 
            String _resolvedComment, Date _dateResolved) {
    
        this.ticketDetails(_ticketID, _complain, _secondsSpent, _resolvedComment,
                _dateResolved);
        
        
        Ticket ticket;
        for (int i = 0; i < this.ticketsList.size(); i++) {
            ticket = this.ticketsList.get(i);
            if (ticket.getTicketID()== _ticketID) {
                ticket.setTicketStatus("Atendido");
                ticket.setEmployeeID(_employeeiD);
                this.addTicket2Employee(_employeeiD, ticket);
            }
        }  
    }
    
    public void updateUnresolvedTicket(int _employeeiD,int _ticketID, String _complain, int _secondsSpent, 
            String _resolvedComment, Date _dateResolved) {
    
        this.ticketDetails(_ticketID, _complain, _secondsSpent, _resolvedComment,
                _dateResolved);
        
        
        Ticket ticket;
        for (int i = 0; i < this.ticketsList.size(); i++) {
            ticket = this.ticketsList.get(i);
            if (ticket.getTicketID()== _ticketID) {
                ticket.setTicketStatus("Pendiente");
                ticket.setEmployeeID(_employeeiD);
                this.addTicket2Employee(_employeeiD, ticket);
            }
        }  
    }
    
    public Vector<Ticket> getREDticketList() {
        return this.RedTickestList;
    
    }
    public String getREDticketString() {
        
        Ticket _ticket;
        String redTickets = "";
        for (int i = 0; i < this.RedTickestList.size(); i++) {
            _ticket = this.RedTickestList.get(i);
            if (_ticket.getTicketStatus() == TicketStatus.IN_QUEUE){
                redTickets += _ticket.getSubject() + ";" + _ticket.getTicketID() + 
                        ";" + _ticket.getDateReceivedString() + ";";
            }
        }
    System.out.println(redTickets);
    return redTickets;
    }
    
    public Vector<Ticket> getYELLOWticketList() {
        return this.YellowTicketsList;
    
    }
    public String getYELLOWticketString() {
        
        Ticket _ticket;
        String yellowTickets = "";
        for (int i = 0; i < this.YellowTicketsList.size(); i++) {
            _ticket = this.YellowTicketsList.get(i);
            if (_ticket.getTicketStatus() == TicketStatus.IN_QUEUE){
                yellowTickets += _ticket.getSubject() + ";" + _ticket.getTicketID() + 
                        ";" + _ticket.getDateReceivedString() + ";";
            }
        }
    System.out.println(yellowTickets);
    return yellowTickets;
    }
    
    public Vector<Ticket> getGREENticketList() {
        return this.GreenTicketsList;
        
    }
        public String getGREENticketString() {
        
        Ticket _ticket;
        String greenTickets = "";
        for (int i = 0; i < this.GreenTicketsList.size(); i++) {
            _ticket = this.GreenTicketsList.get(i);
            if (_ticket.getTicketStatus() == TicketStatus.IN_QUEUE){
                greenTickets += _ticket.getSubject() + ";" + _ticket.getTicketID() + 
                        ";" + _ticket.getDateReceivedString() + ";";
            }
        }
    System.out.println(greenTickets);
    return greenTickets;
    }
    
    
    public Vector<Ticket> getAllTickets() {
        return this.ticketsList;
    
    }
    
    /**
     * This method creates and adds an Employee to the server's list of Employees
     * 
     * @param _employeeID ID if the employee int.
     * @param _email Email of the employee string.
     * @param _password password of the employee string.
     * @param _fullName full name of the Employee being registered.
     */
    public void addEmployee(int _employeeID,String _email, String _password, String _fullName) {
        Employee newEmployee = new Employee(_employeeID, _email,  _password,  _fullName);
        this.employeesList.add(newEmployee);
    }
    
    public Vector<Employee> getEmployeeList() {return this.employeesList;}
    

    /**
     * This method adds a Ticket to the list of tickets solved by a specific Employee
     * 
     * @param _employeeID ID of the employee who worked on the Ticket.
     * @param _ticket Ticket worked on.
     */
    public void addTicket2Employee(int _employeeID,Ticket _ticket) {
        Employee employee;
        
        for (int i = 0; i < this.employeesList.size(); i++) {
            employee = this.employeesList.get(i);
            if (employee.getID() == _employeeID) {
                employee.addTicket(_ticket);
            }
            
        } 
    }
    
    //////////////////////////////////////////////////
    //////////////////////////////////////////////////
    //////////////////////////////////////////////////
    //////////////////////////////////////////////////
    //////////////////////////////////////////////////
    //////////////////////////////////////////////////
    //////////////////////////////////////////////////
    
    /**
     * This method connects an employee to the server
     * 
     * @param _employeeID ID of the employee who's going to connect.
     */
    public void connectEmployee(int _employeeID) {
        Employee employee;
        for (int i = 0; i < this.employeesList.size(); i++) {
            employee = this.employeesList.get(i);
            if (employee.getID() == _employeeID) {
                employee.connect();
            }
        }
    }

    /**
     * This method disconnects an employee from the server
     * 
     * @param _employeeID ID of the employee who's going to disconnect.
     */
    public void disconnectEmployee(int _employeeID) {
        Employee employee;
        for (int i = 0; i < this.employeesList.size(); i++) {
            employee = this.employeesList.get(i);
            if (employee.getID() == _employeeID) {
                employee.disconnect();
            }
        }
    }

    /**
     * This method is used when an employee starts working on a ticket.
     * It changes the ticket status to PROCCESSING and adds the ticket to the employees
     * list of tickets received.
     * 
     * @param _ticketID the ID of the ticket that is going to be worked on.
     * @param _employeeID the ID of the employee who's going to process the Ticket.
     * @return returns true if the employee successfully starts working on the ticket
     * and false if the ticket's status wasn't PENDING
     */
    public boolean reserveTicket(int _ticketID, int _employeeID) {
        Ticket ticket;
        TicketStatus status = TicketStatus.PENDING;
        for (int i = 0; i < this.ticketsList.size(); i++) {
            ticket = this.ticketsList.get(i);
            if (ticket.getTicketID()== _ticketID) {
                if (ticket.getTicketStatus() == status) {
                    ticket.setTicketStatus("En Atencion");
                    Employee employee = this.getEmployee(_employeeID);
                    employee.addTicketReceived(ticket);
                    return true;
                } else {
                    return false;
                
                }
            }
        }
        return false;
    }
    
    /**
     * Method the returns an Employee object
     * 
     * @param _employeeID the ID of the employee wanted
     * @return returns the Employee object
     */
    public Employee getEmployee(int _employeeID) {
        Employee employee = new Employee();
        
        for (int i = 0;i < this.employeesList.size();i ++) {
            employee = this.employeesList.get(i);
            if (_employeeID == employee.getID() ) {
                break;
            }
        }
        return employee;
    }
    
    public Employee getEmployeeByEmail(String _employeeEMAIL) {
        Employee employee = new Employee();
         if (this.employeesList.size() > 0){
            for (int i = 0;i < this.employeesList.size();i ++) {
                employee = this.employeesList.get(i);
                if (_employeeEMAIL.equals(employee.getEmail()) ) {
                    break;
                }
            }
         }
        return employee;
    }
    
    /**
     * Method used when an employee cant solve a Ticket, so the ticket changes
     * status to PENDING again. 
     * 
     * @param _ticketID The ticket that's going to change it's status.
     */
    public void releaseTicket(int _ticketID) {
        Ticket ticket;
        for (int i = 0; i < this.ticketsList.size(); i++) {
            ticket = this.ticketsList.get(i);
            if (ticket.getTicketID()== _ticketID) {
                ticket.setTicketStatus("Pendiente");
            }
        }
    }
    
    /**
     * Average time spent by an employee
     * 
     * @param _employeeID receives an Employee ID
     * @return returns the average time spent by that Employee solving tickets
     */
    public int averageTimeByEmployee(int _employeeID) {
        Employee employee;
        int timeSpent = 0;
        
        for (int i = 0; i < this.employeesList.size(); i++) {
            employee = this.employeesList.get(i);
            if (employee.getID() == _employeeID) {
                timeSpent = employee.getAverageTime();
                break;
            }
        }
        return timeSpent;
    }
    
    /**
     * Average time spent resolving tickets
     * 
     * @return returns an int (seconds) of the average time spent processing
     * tickets
     */
    public int averateTimeTickets() {
        int count = 0;
        int totalSecs = 0;
        Ticket ticket;
        TicketStatus resolved = TicketStatus.RESOLVED;
        for (int i = 0; i < this.ticketsList.size(); i++) {
            ticket = this.ticketsList.get(i);
            if (ticket.getTicketStatus() == resolved) {
                totalSecs += ticket.getTime();
                count++;
            }
        }
        
        return totalSecs/count;
    }


    /**
     * This method returns a vector with the complete list of those tickets
     * solved by a specific employee
     * 
     * @param _employeeID The id of the employee
     * @return returns the complete list of Tickets solved by this employee
     */
    public Vector<Ticket> getTicketsSolvedByEmployee(int _employeeID) {
        Employee employee;
        Vector<Ticket> nullVector = new Vector<Ticket>();
        for (int i = 0; i < this.employeesList.size(); i++) {
            employee = this.employeesList.get(i);
            if (employee.getID() == _employeeID) {
                return employee.getTicketsResolved();
            }
        }
        
        return nullVector;
    }

    /**
     * This method creates a String with the information of  Tickets processed by 
     * a employee, depending on the login given.
     * 
     * @param _email Employee's email String
     * @param _password Employee's password String
     * @param _date Date wanted (used to determine the Tickets used in the report)
     * @return returns a string with a report of the employee's work through the 
     * date given
     */
    public String getEmployeeReport(String _email, String _password, Date _date) {
        Employee employee;
        String msg = "";
        for (int i = 0; i < this.employeesList.size(); i++) {
            employee = this.employeesList.get(i);
            if (employee.successfulLogin(_email, _password)) {
                Vector<Ticket> ticketsListInDateSolved = new Vector<Ticket>();
                Vector<Ticket> ticketsListInDateReceived = new Vector<Ticket>();
                ticketsListInDateSolved = employee.getTicketsInDateSolved(_date);
                ticketsListInDateReceived = employee.getTicketsInDateReceived(_date);
                
                int ticketsSolved = ticketsListInDateSolved.size();
                int ticketsReceived = ticketsListInDateReceived.size();
                int average = (ticketsSolved/ticketsReceived) *100;
                
                String ticketsSolv = Integer.toString(ticketsSolved);
                String ticketsRec = Integer.toString(ticketsReceived);
                String avg = Integer.toString(average);
                
                msg += "La cantidad de tickets resueltos es: " + ticketsSolv +
                        ", la cantidad de tickets recibidos es: " + ticketsRec +
                        ". El promedio de de resolucion es: " + avg;
                return msg;
            }
        }
        msg = "Usuario y/o contrasenha errone@";
        return msg;
    
    }

    /**
     * This method adds a ticket depending on the category given to it's respective list
     * (Red,Yellow,Green)
     * 
     * @param _ticket The ticket so it can be added to the List of tickets
     * @param _categ  The category in which the ticket belongs
     */
    public void addTicketToCategoryList(Ticket _ticket, TicketCategory _categ) {
        
        if (_categ == TicketCategory.LOW) {
            this.GreenTicketsList.add(_ticket);
        } else if (_categ == TicketCategory.MEDIUM) {
            this.YellowTicketsList.add(_ticket);
        } else if (_categ == TicketCategory.URGENT) {
            this.RedTickestList.add(_ticket);
        }
        
    
    }
    /**
     * Method the sets the category of certain ticket
     * 
     * 
     * @param _ticketID the Ticjet ID 
     * @param _category the catgory in which the administrator wants that ticket
     * to be in
     */
    public void setTicketCategory(int _ticketID, String _category) {
        Ticket ticket;
        TicketCategory categ = TicketCategory.LOW;
        for (int i = 0;i < this.ticketsList.size();i++) {
            ticket = this.ticketsList.get(i);
            if (ticket.getTicketID() == _ticketID) {
                ticket.setTicketCategory(_category);
                categ = ticket.getTicketCategory();
                this.addTicketToCategoryList(ticket, categ);
            }
        }
    }
    //Busca el ticket con ell subject enviado por parametro
    public void setTicketCategoryWString(String _ticketSubject, String _category) {
        Ticket ticket;
        //ticket.get
        TicketCategory categ = TicketCategory.LOW;
        for (int i = 0;i < this.ticketsList.size();i++) {
            ticket = this.ticketsList.get(i);
            if (ticket.getSubject() == _ticketSubject){
                ticket.setTicketCategory(_category);
                categ = ticket.getTicketCategory();
                this.addTicketToCategoryList(ticket, categ);
            }
            
        }
        
        
    
    }
    //Busca el ticket con ell subject enviado por parametro
    public Ticket getTicketWString(String _ticketSubject) {
        Ticket ticket = new Ticket(0, "");
        //TicketCategory categ = TicketCategory.LOW;
        for (int i = 0;i < this.ticketsList.size();i++) {
            ticket = this.ticketsList.get(i);
            if (ticket.getSubject().equals(_ticketSubject)){
                break;
                /*ticket.setTicketCategory(_category);
                categ = ticket.getTicketCategory();
                this.addTicketToCategoryList(ticket, categ);*/
            }
            
        }
        return ticket;
    }
    
    public void closeSocket(){
        try{
            if (sc != null){
                sc.close();
            }
        }catch(IOException e){
            return;
        }
    
    }

    
     /**
     *
     * Method that verifies if string received is correct.
     *
     * @param _string Receives a string 
     * @return Returns true if the string is right.
     */
    private boolean verifyString(String _string) {
        if (_string != null && _string.trim().length() > 0)
            // si el string ingresado no es vacio, no es nullo y ademas no 
            // contiene numeros (por que es el nombre de un autor o un editor etc)
            return true;
        return false;
        
    }
    
        
    /**
     *
     * Method that verifies if a email is right (has @)
     *
     * @param _correoE String that is the email.
     * @return Returns true if its written correctly
     */
    private boolean verifyEmail(String _email) {
        int flag = 0;
        
        if (!this.verifyString(_email))
            return false;
        
        if (_email.length() == 0 )
            return false;

        if (_email.charAt(0) == '@')
            return false;

        for (int i = 0; i < _email.length(); i++){
            char letter = _email.charAt(i);

            if (letter == '@'){
                if (_email.charAt(i + 1) == ' ') {
                    return false ;
                }
                flag = 1;
            }
        }
        if (flag != 1) {
            return false;
        }
        
        return true;
    }
    
    
    

    
public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(InterfazServidor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(InterfazServidor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(InterfazServidor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(InterfazServidor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                InterfazServidor dialog = new InterfazServidor(new javax.swing.JFrame(), true);
                //Employee empleado1 = new Employee(402380478, "rojassegniniroberto@gmail.com", "2016139072", "Roberto Rojas Segnini");
                //Employee empleado2 = new Employee(402380478, "sergiohidalgo1610@gmail.com", "2016183437 ", "Sergio Hidalgo Fonseca");
                //Employee empleado3 = new Employee(402380478, "dab18@hotmail.com", "2014089192", "Daniel Alvarado Bonilla");
                
                Server server = Server.getInstance();
                server.readExcel();
                
                server.addEmployee(402380478, "rojassegniniroberto@gmail.com", "2016139072", "Roberto Rojas Segnini");
                server.addEmployee(201360478, "sergiohidalgo1610@gmail.com", "2016183437 ", "Sergio Hidalgo Fonseca");
                server.addEmployee(704150696, "dab18@hotmail.com", "2014089192", "Daniel Alvarado Bonilla");
                
                //SocketThread socket = new SocketThread(dialog, server);
                //server.initServer(socket);
                System.out.println("AAAAAAAAAAAAH");
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.LogInServer.setVisible(true);
            }
        });
        
        
    }

    


}
