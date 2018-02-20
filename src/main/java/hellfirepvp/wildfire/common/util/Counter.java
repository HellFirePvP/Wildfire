/*******************************************************************************
 * HellFirePvP / Wildfire 2018
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/Wildfire
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.wildfire.common.util;

/**
 * This class is part of the Wildfire Mod
 * The complete source code for this mod can be found on github.
 * Class: Counter
 * Created by HellFirePvP
 * Date: 20.02.2018 / 21:17
 */
public class Counter {

    public int value;

    public Counter(int value) {
        this.value = value;
    }

    public void decrement() {
        value--;
    }

    public void increment() {
        value++;
    }

}
