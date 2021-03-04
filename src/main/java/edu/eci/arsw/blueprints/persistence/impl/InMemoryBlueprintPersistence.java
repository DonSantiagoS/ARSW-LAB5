package edu.eci.arsw.blueprints.persistence.impl;

import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;
import edu.eci.arsw.blueprints.persistence.BlueprintNotFoundException;
import edu.eci.arsw.blueprints.persistence.BlueprintPersistenceException;
import edu.eci.arsw.blueprints.persistence.BlueprintsPersistence;
import java.util.HashMap;
import java.util.Map;
import java.util.HashSet;
import java.util.Set;
import org.springframework.stereotype.Service;

 /**
 * ---------------------------------------------------------------------------------------------------------------------------
 * ---------------------------------------------------------------------------------------------------------------------------
 * 													CLASE: InMemoryBlueprintPersistence
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
@Service("InMemoryBlueprintPersistence")
public class InMemoryBlueprintPersistence implements BlueprintsPersistence{

    private final Map<Tuple<String,String>,Blueprint> blueprints=new HashMap<>();
	
	/**
	*/
    public InMemoryBlueprintPersistence() {
        //load stub data
        Point[] pts1=new Point[]{new Point(120, 120),new Point(100, 100)};
		Point[] pts2=new Point[]{new Point(13, 12),new Point(43, 42)};
		Point[] pts3=new Point[]{new Point(120, 150),new Point(54, 67)};
        Blueprint bp1=new Blueprint("Santiago", "bp1 ",pts1);
        blueprints.put(new Tuple<>(bp1.getAuthor(),bp1.getName()), bp1);
		Blueprint bp2=new Blueprint("Santiago", "bp2 ",pts2);
        blueprints.put(new Tuple<>(bp2.getAuthor(),bp2.getName()), bp2);
		Blueprint bp3=new Blueprint("Juan", "bp3 ",pts3);
        blueprints.put(new Tuple<>(bp3.getAuthor(),bp3.getName()), bp3);
        
    }
    
    /**
	*/
	@Override
    public void actualizarBlueprint(Blueprint bp,String author,String name) throws BlueprintNotFoundException {
        Blueprint bpa=getBlueprint(author,name);
        bpa.setPoints(bp.getPoints());
    }
	
	/**
	*/
	@Override
    public  HashSet<Blueprint> getAllBlueprints(){
        return new HashSet<Blueprint>(blueprints.values());
    }
	
	/**
	*/
	@Override
	public Set<Blueprint> getBlueprintsByAuthor(String author) throws BlueprintNotFoundException{
		Set<Blueprint> autorBp = new HashSet<>();
        Set<Tuple<String,String>> llaves = blueprints.keySet();
        for(Tuple<String,String> i : llaves){
            if(i.getElem1().equals(author)){
                autorBp.add(blueprints.get(i));
            }
        }
        return autorBp;
	}
	
	/**
	*/
    @Override
    public void saveBlueprint(Blueprint bp) throws BlueprintPersistenceException {
        if (blueprints.containsKey(new Tuple<>(bp.getAuthor(),bp.getName()))){
            throw new BlueprintPersistenceException("The given blueprint already exists: "+bp);
        }
        else{
            blueprints.put(new Tuple<>(bp.getAuthor(),bp.getName()), bp);
        }        
    }
	
	/**
	*/
    @Override
    public Blueprint getBlueprint(String author, String bprintname) throws BlueprintNotFoundException {
        return blueprints.get(new Tuple<>(author, bprintname));
    }
}
