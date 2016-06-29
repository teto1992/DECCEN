package edu.stefano.deccen.controls;

import edu.stefano.deccen.centralities.AbstractDeccenCD;
import java.util.ArrayList;
import peersim.cdsim.CDState;
import peersim.config.Configuration;
import peersim.core.CommonState;
import peersim.core.Control;
import peersim.core.Network;
import peersim.core.Node;

public class DeccenObserver implements Control {

    private static final String PAR_PROT = "deccen";
    private static final String PAR_CYCLES = "cycles";
    private final int pid;
    private long cycles, NOSP, reports;
    private final ArrayList<Integer> convergedNodes;

    public DeccenObserver(String prefix) {
        pid = Configuration.getPid(prefix + "." + PAR_PROT);
        cycles = Configuration.getLong(prefix + "." + PAR_CYCLES);
        NOSP = 0;
        reports = 0;
        convergedNodes = new ArrayList<>();
    }

    @Override
    public boolean execute() {
        if (CDState.getCycleT() != 0) {
        if (CommonState.getTime() == cycles - 1) {
            System.out.println("***Final Stats***");
            for (int i = 0; i < Network.size(); i++) {
                Node n = Network.get(i);
                AbstractDeccenCD prot = (AbstractDeccenCD) n.getProtocol(pid);
                NOSP += prot.getNOSPNumber();
                reports += prot.getReportsNumber();
                System.out.println(i + " centrality: " + (prot.getCentrality()));
                System.out.println("distances: " + prot.distances);
                System.out.println("NOSP table: " + prot.shortestPathsNumber);
            }

            System.out.println("Exchanged NOSPS: " + NOSP);
            System.out.println("Exchanged Reports: " + reports);
            System.out.println("Exchanged total: " + (NOSP + reports));

        } else {

            long cycleNOSP = 0, cycleReports = 0;
            for (int i = 0; i < Network.size(); i++) {
                boolean countPhaseFinished = false;
                Node n = Network.get(i);
                AbstractDeccenCD prot = (AbstractDeccenCD) n.getProtocol(pid);
                cycleNOSP += prot.getNOSPNumber();
                cycleReports += prot.getReportsNumber();
                if (prot.distances.size() == Network.size()) {
                    countPhaseFinished = true;
                }

//                if (countPhaseFinished && !convergedNodes.contains(i)) {
//                    System.out.println("Node " + i + " converges at cycle " + (2 * CommonState.getIntTime()) + "!");
//                    convergedNodes.add(i);
//                }
            }

            System.out.println("Cycle " + CommonState.getTime() + " Exchanged NOSPS: " + cycleNOSP);
            System.out.println("Cycle " + CommonState.getTime() + " Exchanged Reports: " + cycleReports);
            System.out.println("Cycle " + CommonState.getTime() + " Exchanged Total: " + (cycleReports + cycleNOSP));

            }
        }
        return false;
    }
}
