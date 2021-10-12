package edu.ufl.cise.cs1.controllers;
import game.controllers.AttackerController;
import game.models.*;
import java.awt.*;
import java.util.List;

public final class StudentAttackerController implements AttackerController {

	public void init(Game game) {
	}

	public void shutdown(Game game) {
	}

	public int update(Game game, long timeDue) {

		int action = -1;

		// get a list of all the regular pills
		List<Node> lilPills = game.getPillList();

		// list of all the defenders
		List<Defender> allDefenders = game.getDefenders();


		// create attacker
		Attacker attacker = game.getAttacker();


		// code for level 1
		if (game.getLevel() == 0) {
			for (Defender eachDefender : allDefenders) {
				int distance = attacker.getLocation().getPathDistance(eachDefender.getLocation());
				if (eachDefender.isVulnerable() && distance < 10) {
					action = attacker.getNextDir(eachDefender.getLocation(), true);
					return action;
				}
			}

			// only make the attacker go towards the little pills, as we can finish the level this way
			action = attacker.getNextDir(attacker.getTargetNode(lilPills, true), true);
			return action;
		}

		// once we pass level 1
		else {

			// create list for the powerpills
			List<Node> bigpills = game.getPowerPillList();
			if (!bigpills.isEmpty()) {

				// create nodes to direct the attacker towards pills
				Node bigPillTarget;
				Node littlePillTarget = null;

				// go through all defenders to get access to their info
				for (Defender eachDefender : allDefenders) {

					// create target for each pill
					bigPillTarget = attacker.getTargetNode(bigpills, true);
					littlePillTarget = attacker.getTargetNode(lilPills, true);

					// find the distance between attacker and each defender
					int distance = attacker.getLocation().getPathDistance(eachDefender.getLocation());

					// whenever the defender is vulnerable, we should go for all the smaller pills
					if (eachDefender.isVulnerable()) {
						action = attacker.getNextDir(littlePillTarget, true);
						if (bigpills.isEmpty() && eachDefender.isVulnerable()) {
							action = attacker.getNextDir(eachDefender.getLocation(), true);
						}

						// if the defenders are close enough then eat them
						if (distance < 35) {
							action = attacker.getNextDir(eachDefender.getLocation(), true);
						}
						return action;
					}

					// whenever they are not vulnerable we should go for the closest pill
					if ((!eachDefender.isVulnerable())) {
						action = attacker.getNextDir(bigPillTarget, true);

						// run away whenever we are out of pills and defenders are not vulnerable
						if (bigpills.isEmpty() && (!eachDefender.isVulnerable())) {
							action = attacker.getNextDir(eachDefender.getLocation(), false);
						}
						return action;
					}

					// whenever we eat all of the big pills, we should eat the little pills closest to us
					if (attacker.getTargetNode(bigpills, true) == null) {
						action = attacker.getNextDir(littlePillTarget, true);
						return action;
					}
				}

				// go through the list of power pills
				for (Node pillies : bigpills) {

					// the target should be equal to the location of the big pills
					bigPillTarget = game.getAttacker().getTargetNode(bigpills, true);

					// find the distance between attacker and bigpills
					int pillydistance = attacker.getLocation().getPathDistance(pillies);

					// if we are far from the big pill we should go towards it
					if (pillydistance > 5) {
						action = attacker.getNextDir(bigPillTarget, true);
						return action;
					}
				}
			}
			else {

				Node bigPillTarget;
				Node littlePillTarget = null;
				littlePillTarget = attacker.getTargetNode(lilPills, true);
				action = attacker.getNextDir(littlePillTarget, true);
				return action;
			}
			return -1;
		}
	}
}






