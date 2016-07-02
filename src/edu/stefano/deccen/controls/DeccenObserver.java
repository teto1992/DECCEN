package edu.stefano.deccen.controls;

import edu.stefano.deccen.centralities.AbstractDeccenCD;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import peersim.cdsim.CDState;
import peersim.config.Configuration;
import peersim.core.Control;
import peersim.core.Network;
import peersim.core.Node;

public class DeccenObserver implements Control {

    private static final String PAR_PROT = "deccen";
    private static final String PAR_CYCLES = "cycles";
    private final int pid;
    private long NOSP, reports;
    private final HashSet<Integer> convergedNodes;

    public DeccenObserver(String prefix) {
        pid = Configuration.getPid(prefix + "." + PAR_PROT);
        NOSP = 0;
        reports = 0;
        convergedNodes = new HashSet<>();
    }

    @Override
    public boolean execute() {
        File file1 = new File("logmessages.txt");
        File file2 = new File("logcentrality.txt");
        File file3 = new File("logmessagespercycle.txt");

        if (!file1.exists()) {
            try {
                file1.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(DeccenObserver.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (!file2.exists()) {
            try {
                file2.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(DeccenObserver.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (!file3.exists()) {
            try {
                file3.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(DeccenObserver.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        FileWriter messages = null, centrality = null, centralitypercycle = null;
        try {
            messages = new FileWriter(file1.getName(), true);
            centrality = new FileWriter(file2.getName(), true);
            centralitypercycle = new FileWriter(file3.getName(), true);
        } catch (IOException ex) {
            Logger.getLogger(DeccenObserver.class.getName()).log(Level.SEVERE, null, ex);
        }

        BufferedWriter bf1 = new BufferedWriter(messages);
        BufferedWriter bf2 = new BufferedWriter(centrality);
        BufferedWriter bf3 = new BufferedWriter(centralitypercycle);

        if (CDState.getCycleT() != 0) {
            long cycleNOSP = 0, cycleReports = 0, centr = 0;
            for (int i = 0; i < Network.size(); i++) {
                Node n = Network.get(i);
                AbstractDeccenCD prot = (AbstractDeccenCD) n.getProtocol(pid);
                NOSP += prot.getNOSPNumber();
                reports += prot.getReportsNumber();
                centr += prot.getCentrality();
                cycleNOSP += prot.getNOSPNumber();
                cycleReports += prot.getReportsNumber();
            }
            //System.out.println(CDState.getCycle() + ", " + convergedNodes.size());
            try {
                bf1.write(CDState.getCycle() + ", " + NOSP + ", " + reports + "\n");
                bf2.write(CDState.getCycle() + ", "  + centr + "\n");
                bf3.write(CDState.getCycle() + ", " + cycleNOSP + ", " + cycleReports + "\n");

            } catch (IOException ex) {
                Logger.getLogger(DeccenObserver.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        try {
            bf1.close();
            bf2.close();
            bf3.close();
        } catch (IOException ex) {
            Logger.getLogger(DeccenObserver.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
}
