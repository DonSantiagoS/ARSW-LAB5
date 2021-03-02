package edu.eci.arsw.blueprints.controllers;

import java.util.LinkedHashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.services.BlueprintsServices;
import java.util.logging.Level;
import java.util.logging.Logger;

 
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
@RestController
@RequestMapping(value="/blueprints")
public class BlueprintAPIController {
	
    @Autowired
    @Qualifier("BlueprintsServices")
	BlueprintsServices services;
	
	@RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> manejadorBlueprints(){
        try {
            return new ResponseEntity<>(services.getAllBlueprints(), HttpStatus.ACCEPTED);
        }catch(Exception ex){
            Logger.getLogger(BlueprintAPIController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>("",HttpStatus.NOT_FOUND);
        }
    }
	
    @RequestMapping(value="/{author}", method=RequestMethod.GET)
    public ResponseEntity<?>  manejadorBlueprintsByAuthor(@PathVariable("author") String author){
        try {
            return new ResponseEntity<>(services.getBlueprintsByAuthor(author), HttpStatus.ACCEPTED);
        }catch(Exception ex){
            Logger.getLogger(BlueprintAPIController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
	
    @RequestMapping(value="/crear-blueprint", method=RequestMethod.POST)
    public ResponseEntity<?> manejadorPostBlueprint(@RequestBody Blueprint bp){
        try { 
			services.addNewBlueprint(bp);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }catch(Exception ex){
            Logger.getLogger(BlueprintAPIController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>("",HttpStatus.FORBIDDEN);
        }
    }
	
    @RequestMapping(value="/{author}/{name}", method=RequestMethod.PUT)
    public ResponseEntity<?> manejadorPutBlueprint(@PathVariable("author") String author,@PathVariable("name") String name,@RequestBody Blueprint bp ) {
        try {
            services.actualizarBlueprint(bp,author,name);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } catch (Exception ex) {
            Logger.getLogger(BlueprintAPIController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>( HttpStatus.NOT_FOUND);
        }
    }
}