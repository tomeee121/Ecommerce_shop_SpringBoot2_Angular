package pl.project.wwsis.ecommerceshop.exception;

public class EmailExistException extends Exception{
    public EmailExistException(String message){
        super(message);
    }
}
