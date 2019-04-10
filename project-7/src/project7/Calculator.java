package project7;

import java.util.Arrays;
import javafx.concurrent.Task;

/**
 * project 7 Task subclass (extends class Task) counting numbers in the
 * background, publishing them as they are found.
 *
 * @author Andy Osorio / Thomas Lawless / Justin Moran SCCC Spring 2019
 */
public class Calculator extends Task<Integer> {

    // boolean array for finding primes
    private final boolean[] numbers;

    public Calculator(int max) {
        numbers = new boolean[max];
        Arrays.fill(numbers, true);
    }

    /**
     * The long-running code to be run in a worker thread.
     *
     * @return
     */
    @Override
    protected Integer call() {

        for (int i = 0; i <= numbers.length; i++) {
            if (isCancelled()) {
                // if calculation has been canceled
                updateMessage("Cancelled");
                return 0;
            } else {
                try {
                    // slow the thread
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    updateMessage("Interrupted");
                    return 0;
                }

                updateProgress(i, numbers.length);

                updateMessage(String.format("Numbers"));
                updateValue(i);

                numbers[i] = true;

            }
        }

        return 0;
    }

}
