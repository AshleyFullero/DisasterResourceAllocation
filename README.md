# EQUIRELIEF: GREEDY APPROXIMATION ALGORITHM

## 🆘 Household-Based Smart Distribution for Disaster Relief  
### 📦 Knapsack Problem: Heuristic Approach

---

## 📌 Overview

**EQUIRELIEF** is a Java-based disaster relief distribution system that applies a **Greedy Approximation Algorithm** to efficiently allocate limited resources (e.g., food, water, medical kits) to affected families. The system uses a **household-based prioritization** method and models the distribution as a **Knapsack Problem** using a **heuristic approach**.

---

## 🎯 Goal

To provide a **smart, fast, and fair** way to distribute emergency supplies by maximizing the total utility of distributed items while respecting capacity constraints.

---

## 💡 Core Concept

The algorithm is based on the **Greedy heuristic solution to the 0/1 Knapsack Problem**:

- Each resource has a **weight** (cost or volume) and **value** (urgency or priority).
- Families are assigned scores based on configurable **priority metrics** (e.g., size, vulnerability, special needs).
- The algorithm sorts items or families based on **value-to-weight ratio** and greedily allocates resources accordingly until the knapsack (relief supply limit) is full.

---

### Algorithm Steps:
1. Gather household data and total available resources.
2. Sort families based on priority metric (e.g., size or urgency).
3. Allocate resources greedily from highest to lowest priority.
4. Stop when no more resources can be allocated.

---

## 💻 Installation

```bash
git clone https://github.com/AshleyFullero/DisasterResourceAllocation.git
cd DisasterResourceAllocation
```

## 🚀 Usage

1. Prepare or input the list of families and available resources.
2. Run the program.
3. View the results in the output JavaFX table.
4. Visualize allocations and priorities via color-coding.

---

## ⚙️ Features

- 📋 Configurable household and resource data
- 🔍 Greedy heuristic allocation based on value-to-weight ratio
- 🧮 Custom priority scoring per family
- 📊 JavaFX UI for visualization
- ✅ Fast and practical for field deployment




