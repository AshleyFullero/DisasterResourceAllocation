package org.htech.disasterproject.service;

import javafx.concurrent.Task;
import org.htech.disasterproject.modal.Family;
import org.htech.disasterproject.modal.Resource;
import org.htech.disasterproject.utilities.AllocationConfig;
import org.htech.disasterproject.utilities.SessionManager;

import java.util.List;

public class KnapsackAllocationTask extends Task<KnapsackAllocationService.AllocationResult> {

    private final List<Family> families;
    private final List<Resource> resources;
    private final AllocationConfig config;

    public KnapsackAllocationTask(List<Family> families, List<Resource> resources) {
        this.families = families;
        this.resources = resources;
        this.config = new AllocationConfig();
    }

    @Override
    protected KnapsackAllocationService.AllocationResult call() throws Exception {
        KnapsackAllocationService service = new KnapsackAllocationService();
        updateMessage("Preparing calculation...");
        Thread.sleep(1500);

        updateMessage("Analyzing " + resources.size() + " resource types...");
        Thread.sleep(2000);

        updateMessage("Planning for " + families.size() + " families...");
        Thread.sleep(2000);

        updateMessage("Running allocation algorithm...");
        KnapsackAllocationService.AllocationResult result = service.calculateAllocations(families, resources, config);
        updateMessage("Finalizing allocation plan...");
        Thread.sleep(1500);
        return result;
    }
}