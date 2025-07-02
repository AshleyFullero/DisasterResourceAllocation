package org.htech.disasterproject.service;

import org.htech.disasterproject.modal.Family;
import org.htech.disasterproject.modal.Resource;
import org.htech.disasterproject.utilities.AllocationConfig;

import java.util.*;
import java.util.stream.Collectors;


public class KnapsackAllocationService {


    private record PricedResource(Resource resource, double greedyScore) {}


    public record AllocationResult(
            Map<Family, Map<Resource, Integer>> allocatedItems,
            Map<Resource, Integer> remainingResources,
            double finalRemainingWeight
    ) {}


    public AllocationResult calculateAllocations(List<Family> families, List<Resource> resources, AllocationConfig config) {
        if (families.isEmpty() || resources.isEmpty()) {
            return new AllocationResult(new HashMap<>(), new HashMap<>(), config.getKnapsackCapacity());
        }

        double knapsackCapacityLeft = config.getKnapsackCapacity();
        System.out.println("Starting Knapsack Allocation. Initial Capacity: " + knapsackCapacityLeft + " KG");

        Map<Resource, Integer> availableQuantities = resources.stream()
                .collect(Collectors.toMap(r -> r, Resource::getTotalQuantity));

        final int totalPopulation = families.stream().mapToInt(Family::getFamilySize).sum();
        if (totalPopulation == 0) return new AllocationResult(new HashMap<>(), availableQuantities, knapsackCapacityLeft);

        Map<Family, Double> familyNeedScores = families.stream()
                .collect(Collectors.toMap(f -> f, f -> (double) f.getFamilySize() / totalPopulation));

        List<PricedResource> sortedResources = resources.stream()
                .map(r -> {
                    double weight = r.getWeightKg() > 0 ? r.getWeightKg() : 0.001;
                    double score = r.getImportanceScore() / weight;
                    return new PricedResource(r, score);
                })
                .sorted(Comparator.comparingDouble(PricedResource::greedyScore).reversed())
                .toList();

        System.out.println("\nSorted Resources by Greedy Score:");
        sortedResources.forEach(pr -> System.out.printf("- %s (Score: %.2f)%n", pr.resource.getName(), pr.greedyScore));

        Map<Family, Map<Resource, Integer>> finalAllocations = new LinkedHashMap<>();
        families.forEach(f -> finalAllocations.put(f, new HashMap<>()));

        for (PricedResource pricedResource : sortedResources) {
            Resource resource = pricedResource.resource();
            int availableQty = availableQuantities.get(resource);
            if (availableQty <= 0) continue;

            System.out.println("\n--- Processing: " + resource.getName() + " ---");
            System.out.println("Available: " + availableQty + " units. Knapsack space left: " + knapsackCapacityLeft + " KG");

            int quantityToAllocate;
            double totalResourceWeight = availableQty * resource.getWeightKg();

            if (totalResourceWeight > knapsackCapacityLeft) {
                if (resource.getWeightKg() > 0) {
                    quantityToAllocate = (int) Math.floor(knapsackCapacityLeft / resource.getWeightKg());
                } else {
                    quantityToAllocate = availableQty;
                }
                System.out.println("Not all units fit. Adjusting quantity to allocate: " + quantityToAllocate);
            } else {
                quantityToAllocate = availableQty;
            }

            if (quantityToAllocate <= 0) {
                System.out.println("No more space for this item. Skipping subsequent items.");
                break;
            }

            if (resource.getName().equalsIgnoreCase("Baby Formula")) {
                List<Family> familiesWithInfants = families.stream().filter(this::hasInfantOrBaby).toList();
                if (!familiesWithInfants.isEmpty()) {
                    System.out.println("Special rule: Distributing Baby Formula only to families with infants.");
                    long totalFormulaActuallyDistributed = 0;

                    for (Family infantFamily : familiesWithInfants) {
                        double needScore = (double) infantFamily.getFamilySize() /totalPopulation;
                        long share = Math.round(needScore * quantityToAllocate);

                        if (share > 0) {
                            finalAllocations.computeIfAbsent(infantFamily, k -> new HashMap<>()).put(resource, (int) share);
                            totalFormulaActuallyDistributed += share;
                            System.out.printf("  -> Calculated share for %s: %.3f * %d = %.3f -> Rounded to %d cans%n",
                                    infantFamily.getFamilyHeadName(), needScore, quantityToAllocate, (needScore * quantityToAllocate), share);
                        }
                    }

                    double weightOfAllAvailableCans = resource.getTotalQuantity() * resource.getWeightKg();
                    long finalTotalFormulaActuallyDistributed1 = totalFormulaActuallyDistributed;
                    availableQuantities.computeIfPresent(resource, (r, qty) -> qty - (int) finalTotalFormulaActuallyDistributed1);

                    knapsackCapacityLeft -= weightOfAllAvailableCans;
                    System.out.printf("Total distributed: %d cans. Remaining units are left over. New remaining space: %.2f KG%n", totalFormulaActuallyDistributed, knapsackCapacityLeft);

                } else {
                    System.out.println("No families with infants noted. Skipping Baby Formula distribution.");
                }
                continue;
            }


            long totalDistributed = 0;
            Map<Family, Long> familyAllocs = new HashMap<>();

            for (Family family : families) {
                double need = familyNeedScores.get(family);
                long share = Math.round(need * quantityToAllocate);
                familyAllocs.put(family, share);
                totalDistributed += share;
            }

            long sumOfRoundedAllocations = familyAllocs.values().stream().mapToLong(Long::longValue).sum();

            if (sumOfRoundedAllocations == 0 && quantityToAllocate > 0) {
                System.out.println("    -> Sum of rounded allocations is zero. No units of " + resource.getName() + " will be distributed due to insufficient quantity for fair distribution.");
                continue;
            }


            long discrepancy = quantityToAllocate - totalDistributed;
            List<Family> familiesSortedByNeed = families.stream()
                    .sorted(Comparator.comparingInt(Family::getFamilySize).reversed()).toList();

            int famIdx = 0;
            while (discrepancy != 0) {
                Family familyToAdjust = familiesSortedByNeed.get(famIdx % familiesSortedByNeed.size());
                if (discrepancy > 0) {
                    familyAllocs.merge(familyToAdjust, 1L, Long::sum);
                    discrepancy--;
                } else if(familyAllocs.get(familyToAdjust) > 0) {
                    familyAllocs.merge(familyToAdjust, -1L, Long::sum);
                    discrepancy++;
                }
                famIdx++;
            }


            for(Map.Entry<Family, Long> entry : familyAllocs.entrySet()) {
                finalAllocations.get(entry.getKey()).put(resource, entry.getValue().intValue());
            }

            availableQuantities.computeIfPresent(resource, (r, qty) -> qty - quantityToAllocate);
            knapsackCapacityLeft -= (quantityToAllocate * resource.getWeightKg());

            System.out.println("Allocated " + quantityToAllocate + " units. Remaining space: " + knapsackCapacityLeft + " KG");
        }

        System.out.println("\n--- Allocation Process Complete ---");
        System.out.println("Final Remaining Knapsack Capacity: " + knapsackCapacityLeft + " KG");

        return new AllocationResult(finalAllocations, availableQuantities, knapsackCapacityLeft);
    }

    private boolean hasInfantOrBaby(Family family) {
        String notes = family.getNotes();
        if (notes == null || notes.trim().isEmpty()) return false;
        String lowerNotes = notes.toLowerCase();
        return lowerNotes.contains("baby") || lowerNotes.contains("infant");
    }
}