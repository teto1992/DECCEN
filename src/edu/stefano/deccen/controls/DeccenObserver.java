package edu.stefano.deccen.controls;

import edu.stefano.deccen.centralities.AbstractDeccenCD;
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

    public DeccenObserver(String prefix) {
        pid = Configuration.getPid(prefix + "." + PAR_PROT);
        cycles = Configuration.getLong(prefix + "." + PAR_CYCLES);
        NOSP = 0;
        reports = 0;
    }

    @Override
    public boolean execute() {

        if (CommonState.getTime() == cycles - 1) {
            System.out.println(CommonState.getTime() + "Final Stats");
            for (int i = 0; i < Network.size(); i++) {
                Node n = Network.get(i);
                AbstractDeccenCD prot = (AbstractDeccenCD) n.getProtocol(pid);
                NOSP += prot.getNOSPNumber();
                reports += prot.getReportsNumber();
                System.out.println(i + " stress: 1/" + ((int) (prot.getCentrality())));

            }

            System.out.println("Exchanged NOSPS: " + NOSP);
            System.out.println("Exchanged Reports: " + reports);

        } else {
            long cycleNOSP = 0, cycleReports = 0;
            for (int i = 0; i < Network.size(); i++) {
                Node n = Network.get(i);
                AbstractDeccenCD prot = (AbstractDeccenCD) n.getProtocol(pid);
                cycleNOSP += prot.getNOSPNumber();
                cycleReports += prot.getReportsNumber();
            }
            System.out.println("Cycle " + CommonState.getTime() + " Exchanged NOSPS: " + cycleNOSP);
            System.out.println("Cycle " + CommonState.getTime() + " Exchanged Reports: " + cycleReports);
        }
        return false;
    }
}
