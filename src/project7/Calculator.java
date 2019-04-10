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

    // boolean array for counting numbers
    private final boolean[] numbers;
    
    /**
     * The calculator class constructor. This calls a worker
     * thread to count through a number.
     * 
     * @param number the number to be counted through 
     */
    public Calculator(int number) {
        numbers = new boolean[number];
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
                    Thread.sleep(50);
                } catch (InterruptedException ex) {
                    updateMessage("Interrupted");
                    return 0;
                }
                updateProgress(i, numbers.length);
                updateMessage("Counting...");
                updateValue(i);
                numbers[i] = true;
            }
        }
        updateMessage("Finished counting!");
        return 0;
    }

}
