package edu.eci.arsw.blueprints.controllers;

import java.util.LinkedHashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import edu.eci.arsw.blueprints.model.*;
import javax.validation.Valid;
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
	BlueprintsServices servicios;
	
	@RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> manejadorBlueprints(){
        try {
            return new ResponseEntity<>(servicios.getAllBlueprints(), HttpStatus.ACCEPTED);
        }catch(Exception ex){
            Logger.getLogger(BlueprintAPIController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>("",HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(path  = "/{author}",method = RequestMethod.GET)
    public ResponseEntity<?>  manejadorBlueprintsByAuthor(@PathVariable("author") String author){
        try {
            return new ResponseEntity<>(servicios.getBlueprintsByAuthor(author), HttpStatus.ACCEPTED);
        }catch(Exception ex){
            Logger.getLogger(BlueprintAPIController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
	
	@RequestMapping(path  = "/{author}/{bpname}",method = RequestMethod.GET)
    public ResponseEntity<?> manejadorBlueprintsByAuthor(@PathVariable("author") String author,@PathVariable("bpname") String bpname){
        try {
            return new ResponseEntity<>( servicios.getBlueprint(author, bpname),HttpStatus.ACCEPTED);
        } catch (Exception ex) {
            Logger.getLogger(BlueprintAPIController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
	

    @RequestMapping(path = "/create",method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<?> manejadorPostBlueprint(@RequestBody Blueprint bp){
        try { 
			servicios.addNewBlueprint(bp);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }catch(Exception ex){
            Logger.getLogger(BlueprintAPIController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>("",HttpStatus.FORBIDDEN);
        }
    }

    @RequestMapping(path = "/{author}/{bpname}",method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<?> manejadorPutBlueprint(@PathVariable("author") String author,@PathVariable("name") String name,@RequestBody Blueprint bp ) {
        try {
            servicios.actualizarBlueprint(bp,author,name);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } catch (Exception ex) {
            Logger.getLogger(BlueprintAPIController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>( HttpStatus.NOT_FOUND);
        }
    }
}