# The Prisoners and the Light Switch

There are 100 prisoners in solitary cells. There's a central living room with one light bulb; this bulb is initially off. No prisoner can see the light bulb from his or her own cell.

Every now and then, the warden picks a prisoner equally at random, and that prisoner visits the living room. While there, the prisoner can toggle the bulb if he or she wishes. Also, the prisoner has the option of asserting that all 100 prisoners have been to the living room by now. If this assertion is false, all 100 prisoners are shot. However, if it is indeed true, all prisoners are set free. Thus, the assertion should only be made if the prisoner is 100% certain of its validity.

The prisoners are allowed to get together one night in the courtyard, to discuss a plan. What plan should they agree on, so that eventually, someone will make a correct assertion?

## Results

Below is an animated .gif that shows the results for various runs of 100 prisoners simulations. The x-axis represents the total number of calls by the warden the simulation took to free the prisoners. The y-axis is the frequency of those values. The x-values are divided by 100, so you'll have to multiply them by 100 to get the real value of calls made.

Each frame represents a run of the program. The chart in the first frame corresponds to the total number of calls necessary to free 100 prisoners, in 1000 simulations. Every frame that follows increases the number of simulations by 10x, thus the evident smoothing of the gaussian curve.

![image](http://i.imgur.com/Fi0s002.gif)
