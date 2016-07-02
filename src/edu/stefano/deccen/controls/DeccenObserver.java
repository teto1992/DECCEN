package edu.stefano.deccen.controls;

import edu.stefano.deccen.centralities.AbstractDeccenCD;
import edu.stefano.deccen.centralities.BetweennessCentralityCD;
import edu.stefano.deccen.centralities.ClosenessCentralityCD;
import edu.stefano.deccen.centralities.StressCentralityCD;
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
    private final int pid, cycles;
    private long NOSP, reports; //accumulate the total number of exchanged messages

    public DeccenObserver(String prefix) {
        pid = Configuration.getPid(prefix + "." + PAR_PROT);
        cycles = Configuration.getPid(prefix + "." + PAR_PROT);
        NOSP = 0;
        reports = 0;
    }

    @Override
    public boolean execute() {
        
        if (CDState.getCycleT() != 0) { // do not execute at the beginning of a cycle
            long size = Network.size();
            long cycleNOSP = 0, cycleReports = 0, centr = 0; //sum up the NOSP, the Reports and the centralities for a cycle
            Class c = null;
            for (int i = 0; i < size; i++) {
                Node n = Network.get(i);
                AbstractDeccenCD prot = (AbstractDeccenCD) n.getProtocol(pid);
                c = prot.getClass();
                NOSP += prot.getNOSPNumber();
                reports += prot.getReportsNumber();
                centr += prot.getCentrality();
                cycleNOSP += prot.getNOSPNumber();
                cycleReports += prot.getReportsNumber();
                System.out.println(i + " centr " + prot.getCentrality());
            }

            File file1 = new File("logmessages.txt");
            File file2 = new File("logcentrality.txt");
            File file3 = new File("logmessagespercycle.txt");
            FileWriter messages = null, centrality = null, centralitypercycle = null;

            try {
                if (!file1.exists()) {
                    file1.createNewFile();
                }
                if (!file2.exists()) {
                    file2.createNewFile();
                }
                if (!file3.exists()) {
                    file3.createNewFile();
                }
                messages = new FileWriter(file1.getName(), true);
                centrality = new FileWriter(file2.getName(), true);
                centralitypercycle = new FileWriter(file3.getName(), true);
            } catch (IOException ex) {
                Logger.getLogger(DeccenObserver.class.getName()).log(Level.SEVERE, null, ex);
            }

            BufferedWriter bf1 = new BufferedWriter(messages);
            BufferedWriter bf2 = new BufferedWriter(centrality);
            BufferedWriter bf3 = new BufferedWriter(centralitypercycle);

            try {
                bf1.write(CDState.getCycle() + ", " + NOSP + ", " + reports + "\n");
                bf2.write(CDState.getCycle() + ", " + centr + "\n");
                bf3.write(CDState.getCycle() + ", " + cycleNOSP + ", " + cycleReports + "\n");
                bf1.close();
                bf2.close();
                bf3.close();
            } catch (IOException ex) {
                Logger.getLogger(DeccenObserver.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return false;
    }
}
