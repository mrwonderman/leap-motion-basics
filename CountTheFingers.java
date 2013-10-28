import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Hand;
import com.leapmotion.leap.HandList;

public class CountTheFingers {

	private static int finger_count;
	private static int hand_count;

	public static void main(String[] args) {
		Controller controller = new Controller();
		System.out.println("Put your hands over the sensor.");

		// ugly, but does it's job
		while (true) {
			if (controller.isConnected()) {
				HandList hands = controller.frame().hands();

				// check if finger count has changed
				if (getFingerCount(hands) != finger_count) {
					hand_count = hands.count();
					finger_count = getFingerCount(hands);
					System.out.println("amount of fingers: " + finger_count
							+ " amount of hands: " + hand_count);
				}
			}

		}

	}

	/**
	 * Returns the amount of fingers of the hands recognized.
	 * 
	 * @param hands
	 *            A list of hands, see {@link HandList}
	 * 
	 * @return the number of fingers from all hands. The default value is 0.
	 */
	private static int getFingerCount(HandList hands) {
		int count = 0;
		for (Hand hand : hands) {
			count += hand.fingers().count();
		}
		return count;
	}
}
