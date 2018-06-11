/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servidor;

import java.util.Date;
import java.util.Vector;


/**
 *
 * @author danielalvarado
 */
public class Employee {
    int employeeID;
    private String email;
    private String password;
    private String fullName;
    private  EmployeeStatus status;
    //NUEVO
    
    private Vector<Ticket> ticketsReceived =  new Vector<Ticket>();
    private Vector<Ticket> ticketList =  new Vector<Ticket>();
    
    public Employee() {};
    
    public Employee (int _employeeID,String _email, String _password, String _fullName) {
        
        this.employeeID = _employeeID;
        this.email = _email;
        this.fullName = _fullName;
        this.password = _password;
        EmployeeStatus _status = EmployeeStatus.DISCONNECTED;
        
        this.status = _status;
        
    }
    
    public String getEmail() {return this.email;}
    public String getFullName() {return this.fullName;}
    
    /**
     * This methods changes the Employee's status to CONNECTED
     */
    public void connect() {
        EmployeeStatus connect = EmployeeStatus.CONNECTED;
        this.status = connect;
    }
    
    /**
     * This methods changes the Employee's status to DISCONNECTED
     */
    public void disconnect() {
        EmployeeStatus disconnect = EmployeeStatus.DISCONNECTED;
        this.status = disconnect;
    }

    /**
     * This methods adds a ticket to the Tickets solved by this employee
     * @param _newTicket 
     */
    public void addTicket(Ticket _newTicket) {
        this.ticketList.add(_newTicket);
    }
    
    public Vector<Ticket> getTicketsResolved() {return this.ticketList;}
    
    public int getID() {return this.employeeID;}
    

    /**
     * This methods simply adds a ticket to the list that contains every ticket 
     * the employee has received
     * @param _ticket Object ticket received by the employee.
     */
    public void addTicketReceived(Ticket _ticket) {
        this.ticketsReceived.add(_ticket);
    }
    
    /**
     * Gets Average time by this employee
     * 
     * @return returns the average time in Seconds a employee spent solving
     *         tickets
     */
    public int getAverageTime() {
        int count = 0;
        int totalSecs = 0;
        Ticket ticket;
        for (int i = 0; i < this.ticketList.size(); i++) {
            ticket = this.ticketList.get(i);
            totalSecs += ticket.getTime();
            count ++;
        }
        
        return totalSecs/count;
        
    }
    

    /**
     * This methods returns true if the combination of 
     * password and email is correct
     * @param _email string with the email.
     * @param _password string with the password
     * @return returns true if they are correct, false if they're not.
     * 
     */
    public boolean successfulLogin(String _email,String _password) {
        if (this.email.equals(_email) && this.password.equals(_password)) {
            return true;
        }
        return false;
    }
    
    /**
     * This method is used to get a list of tickets solved in a before the date given.
     * @param _date Object date used to get the tickets.
     * @return returns the Vector with all the tickets the employee solved before
     * the date
     */
    public Vector<Ticket> getTicketsInDateSolved(Date _date) {
        Vector<Ticket> list = new Vector<Ticket>();
        Ticket ticket;
        for (int i = 0; i < this.ticketList.size();i ++) {
            ticket = this.ticketList.get(i);
            if (_date.compareTo(ticket.getDateReceived()) <= 0) {
                list.add(ticket);
            }
        }
        return list;
    }
    
    public Vector<Ticket> getTicketsReceived() {return this.ticketsReceived;}
    

    /**
     * This method is used to get a list of tickets received in a before the date given.
     * @param _date Object date used to get the tickets.
     * @return returns the Vector with all the tickets the employee received before
     * the date
     */
    public Vector<Ticket> getTicketsInDateReceived(Date _date) {
        Vector<Ticket> list = new Vector<Ticket>();
        Ticket ticket;
        for (int i = 0; i < this.ticketsReceived.size();i ++) {
            ticket = this.ticketsReceived.get(i);
            if (_date.compareTo(ticket.getDateReceived()) <= 0) {
                list.add(ticket);
            }
        }
        return list;
    }
    
}
