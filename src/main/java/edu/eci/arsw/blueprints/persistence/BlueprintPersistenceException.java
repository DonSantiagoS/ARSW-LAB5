package edu.eci.arsw.blueprints.persistence;

/**
 * ---------------------------------------------------------------------------------------------------------------------------
 * ---------------------------------------------------------------------------------------------------------------------------
 * 													CLASE: BlueprintPersistenceException
 * ---------------------------------------------------------------------------------------------------------------------------
 * 
 * ---------------------------------------------------------------------------------------------------------------------------
 * @author Santiago Buitrago
 * @author Steven Garzon
 * @version 1.0
 * ---------------------------------------------------------------------------------------------------------------------------
 */
/**
 *
 * @author hcadavid
 */
public class BlueprintPersistenceException extends Exception{
	
	/**
	*/
    public BlueprintPersistenceException(String message) {
        super(message);
    }

	/**
	*/
    public BlueprintPersistenceException(String message, Throwable cause) {
        super(message, cause);
    }   
}