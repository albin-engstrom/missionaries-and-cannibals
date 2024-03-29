package search;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Performs a Breadth First Search
 * 
 * @author Albin Engström
 */
public class BreadthFirstSearch {

	/**
	 * The node holding the state
	 * 
	 * The numbers represent, in order from left to right, Number of
	 * missionaries on the starting bank, number of cannibals on the starting
	 * bank, is the boat on the starting bank, 1 = true, 0 = false.
	 */
	ArrayList<Integer> node = new ArrayList<Integer>();

	/**
	 * The goal state
	 */
	ArrayList<Integer> goal = new ArrayList<Integer>();

	/**
	 * Holds the nodes of the frontier
	 */
	ArrayDeque<ArrayList<Integer>> frontier = new ArrayDeque<ArrayList<Integer>>();

	/**
	 * Holds the explored nodes
	 */
	HashSet<ArrayList<Integer>> explored = new HashSet<ArrayList<Integer>>();

	/**
	 * Holds the child nodes
	 */
	ArrayList<ArrayList<Integer>> childNodes = new ArrayList<ArrayList<Integer>>();

	/**
	 * A map that maps the child nodes to their parent node
	 */
	HashMap<ArrayList<Integer>, ArrayList<Integer>> parentMap = new HashMap<ArrayList<Integer>, ArrayList<Integer>>();

	/**
	 * Constructor
	 */
	public BreadthFirstSearch() {

		// Initialize node to the starting state
		node.add(3);
		node.add(3);
		node.add(1);

		// Add node to frontier
		frontier.add(node);

		// Initialize goal to the goal state
		goal.add(0);
		goal.add(0);
		goal.add(0);
	}

	/**
	 * Search for a solution
	 * 
	 * @param solution
	 *            An ArrayList with the nodes making up the path to the goal
	 *            state
	 * @return true if a solutions is found, else false
	 */
	public boolean search(ArrayList<ArrayList<Integer>> solution) {

		// Return true if the starting state is the goal state
		if (node.equals(goal)) {
			return true;
		}

		// Loop forever
		while (1 < 2) {

			// Return false if frontier is empty
			if (frontier.isEmpty()) {
				return false;
			}

			// Get a node form frontier
			node = frontier.remove();

			// Add node to explored
			explored.add(node);

			// Apply actions to node, populating the childNodes List
			applyActions(node);

			// Iterate through childNodes
			for (ArrayList<Integer> tmpNode : childNodes) {

				// Check if an equivalent of tmpNode exists in explored or
				// frontier
				if (!explored.contains(tmpNode) && !frontier.contains(tmpNode)) {

					// Map tmpNode to node, it's parent
					parentMap.put(tmpNode, node);

					// Check if tmpNode equals goal
					if (tmpNode.equals(goal)) {

						// Add tmpNode to solution
						solution.add(tmpNode);

						// An ArrayList to hold the parent node
						ArrayList<Integer> parent;

						// While tmpNode is a key in parentMap
						while (parentMap.containsKey(tmpNode)) {

							// Get parent of tmpNode from parentMap
							parent = parentMap.get(tmpNode);

							// Add parent to solution
							solution.add(parent);

							// Remove tmpNodes parent from parentMap
							parentMap.remove(tmpNode);

							// Set tmpNode to parent
							tmpNode = parent;
						}

						// Return true if so
						return true;

					} else {

						// Add tmpNode to frontier otherwise
						frontier.add(tmpNode);
					}
				}
			}
		}
	}

	/**
	 * Applies the possible actions to a state
	 * 
	 * @param node
	 *            The node representing the state to apply actions to
	 */
	private void applyActions(ArrayList<Integer> node) {

		// Reset childNodes
		childNodes = new ArrayList<ArrayList<Integer>>();

		// Initialize the child nodes to their parent's state
		for (int i = 0; i < 5; i++) {

			// An temporary node
			ArrayList<Integer> tmpNode = new ArrayList<Integer>();

			// Set the state of tmpNode to match node
			tmpNode.add(node.get(0));
			tmpNode.add(node.get(1));
			tmpNode.add(node.get(2));

			// Add tmpNode to childNodes
			childNodes.add(tmpNode);
		}

		// Get the boat's position
		int boatPosition = node.get(2);

		// Apply actions to give the child nodes new states

		// One missionary goes by boat
		createChildState(childNodes.get(0), 1, 0, boatPosition);

		// Two missionaries goes by boat
		createChildState(childNodes.get(1), 2, 0, boatPosition);

		// One cannibal goes by boat
		createChildState(childNodes.get(2), 0, 1, boatPosition);

		// Two cannibals goes by boat
		createChildState(childNodes.get(3), 0, 2, boatPosition);

		// One of each goes by boat
		createChildState(childNodes.get(4), 1, 1, boatPosition);

		// And ArrayList tp hold the child nodes to remove
		ArrayList<ArrayList<Integer>> removeChildNodes = new ArrayList<ArrayList<Integer>>();

		// Iterate through childNodes
		for (ArrayList<Integer> tmpNode : childNodes) {

			// Check if tmpNode was created by an illegal action
			if (tmpNode.get(0) < 0 || tmpNode.get(0) > 3) {

				// Add tmpNode to removeChildNodes if so
				removeChildNodes.add(tmpNode);

			} else if (tmpNode.get(1) < 0 || tmpNode.get(1) > 3) {

				// Add tmpNode to removeChildNodes if so
				removeChildNodes.add(tmpNode);
			}
			// Check if tmpNode is an illegal state
			else if (tmpNode.get(0) < tmpNode.get(1) && tmpNode.get(0) > 0) {

				// Add tmpNode to removeChildNodes if so
				removeChildNodes.add(tmpNode);
			} else if ((3 - tmpNode.get(0)) < (3 - tmpNode.get(1))
					&& (3 - tmpNode.get(0)) > 0) {

				// Add tmpNode to removeChildNodes if so
				removeChildNodes.add(tmpNode);
			}
		}

		// Remove the nodes in removeChildNodes from childNodes
		childNodes.removeAll(removeChildNodes);

	}

	/**
	 * Create a child state
	 * 
	 * @param node
	 *            The node with the state to modify
	 * @param missionaries
	 *            The number of missionaries traveling by boat
	 * @param cannibals
	 *            The number of missionaries traveling by boat
	 * @param boatPostition
	 *            The current position of the boat
	 */
	private void createChildState(ArrayList<Integer> node, int missionaries,
			int cannibals, int boatPostition) {

		// Check where the boat is, modify the state accordingly
		if (boatPostition == 1) {
			node.set(0, node.get(0) - missionaries);
			node.set(1, node.get(1) - cannibals);
			node.set(2, node.get(2) - 1);
		} else {
			node.set(0, node.get(0) + missionaries);
			node.set(1, node.get(1) + cannibals);
			node.set(2, node.get(2) + 1);
		}
	}
}
