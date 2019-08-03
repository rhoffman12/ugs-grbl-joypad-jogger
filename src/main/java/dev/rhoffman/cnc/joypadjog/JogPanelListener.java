/*
    Copyright 2019 Ryan Hoffman
    Forked from:
        Universal Gcode Sender (UGS)
        https://github.com/winder/Universal-G-Code-Sender
        Copyright 2018 Will Winder

    GPLv3 - see LICENSE file in project root

 */
package dev.rhoffman.cnc.joypadjog;

/**
 * A listener for button events in the {@link JogPanel}.
 *
 * @author Joacim Breiler
 */
public interface JogPanelListener {

    /**
     * Is called when the button was single clicked
     *
     * @param button the enum for the button
     */
    void onButtonClicked(JogPanelButtonEnum button);

    /**
     * Is called when the button has been long pressed
     *
     * @param button the enum for the button
     */
    void onButtonLongPressed(JogPanelButtonEnum button);

    /**
     * Is called when a long pressed button has been released
     *
     * @param button the enum for the button
     */
    void onButtonLongReleased(JogPanelButtonEnum button);

    /**
     * Is called when the step size of the Z-axis is changed
     *
     * @param value the step size
     */
    void onStepSizeZChanged(double value);

    /**
     * Is called when the step size of the XY-axis is changed
     *
     * @param value the step size
     */
    void onStepSizeXYChanged(double value);

    /**
     * Is called when the feed rate is changed
     *
     * @param value the feed rate
     */
    void onFeedRateChanged(int value);
}
