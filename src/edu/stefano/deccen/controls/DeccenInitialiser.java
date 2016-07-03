/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.stefano.deccen.controls;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;
import peersim.core.Control;

/**
 * This class initialises the system at time t0.
 *
 * @author stefano
 */
public class DeccenInitialiser implements Control {

    public DeccenInitialiser(String prefix) {
    }

    /**
     * It sets up the NOSP table and the distances table for each node v with
     * (v, 1) and (v, 0) respectively. It posts the message (v,1) to be sent
     * later by the PostMan Control.
     *
     * @return false
     */
    @Override
    public boolean execute() {
        File file1 = new File("logmessages.txt");
        File file2 = new File("logcentrality.txt");
        File file3 = new File("logmessagespercycle.txt");
        File file4 = new File("lastUpdate.txt");
        File file5 = new File("result.txt"); 
        
        try {
            Files.deleteIfExists(file1.toPath());
            Files.deleteIfExists(file2.toPath());
            Files.deleteIfExists(file3.toPath());
            Files.deleteIfExists(file4.toPath());
            Files.deleteIfExists(file5.toPath());
            file1.createNewFile();
            file2.createNewFile();
            file3.createNewFile();
            file4.createNewFile();
            file5.createNewFile();            
        } catch (IOException ex) {
            Logger.getLogger(DeccenInitialiser.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
    }

}