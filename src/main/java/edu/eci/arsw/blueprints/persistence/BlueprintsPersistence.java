package edu.eci.arsw.blueprints.persistence;

import edu.eci.arsw.blueprints.model.Blueprint;
import java.util.Set;

 /**
 * ---------------------------------------------------------------------------------------------------------------------------
 * ---------------------------------------------------------------------------------------------------------------------------
 * 													CLASE: BlueprintsPersistence
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
public interface BlueprintsPersistence {
    
    /**
     * 
     * @param bp the new blueprint
     * @throws BlueprintPersistenceException if a blueprint with the same name already exists,
     *    or any other low-level persistence error occurs.
     */
    public void saveBlueprint(Blueprint bp) throws BlueprintPersistenceException;
    
    /**
     * 
     * @param author blueprint's author
     * @param bprintname blueprint's author
     * @return the blueprint of the given name and author
     * @throws BlueprintNotFoundException if there is no such blueprint
     */
    public Blueprint getBlueprint(String author,String bprintname) throws BlueprintNotFoundException; 
	
	public Set<Blueprint> getBlueprintsByAuthor(String author) throws BlueprintNotFoundException;
	
	 public void actualizarBlueprint(Blueprint bp,String author,String name) throws BlueprintNotFoundException;
	
	public Set<Blueprint> getAllBlueprints() throws BlueprintNotFoundException;
}