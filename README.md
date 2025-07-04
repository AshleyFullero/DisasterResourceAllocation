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

This approach does **not always yield the optimal solution**, but it ensures **speed, scalability, and simplicity**—ideal for real-time disaster response.

---

## ⚙️ Features

- 📋 Configurable household and resource data
- 🔍 Greedy heuristic allocation based on value-to-weight ratio
- 🧮 Custom priority scoring per family
- 📊 JavaFX UI (optional) for visualization
- ✅ Fast and practical for field deployment




