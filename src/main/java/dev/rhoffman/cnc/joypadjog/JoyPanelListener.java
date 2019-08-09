/*
    Copyright 2019 Ryan Hoffman
    GPLv3 - see LICENSE file in project root

    Forked from:

    ---------------------------------------------------------------------

    Copyright 2018 Will Winder

    This file is part of Universal Gcode Sender (UGS).

    UGS is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    UGS is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with UGS.  If not, see <http://www.gnu.org/licenses/>.

 */
package dev.rhoffman.cnc.joypadjog;

/**
 * A listener for button events in the {@link JoyPanel}.
 * Joypad events are covered by dev.rhoffman.gamepadevents.GamepadEventListener
 *
 * @author Joacim Breiler
 */
public interface JoyPanelListener {

    /**
     * Is called when the button was single clicked
     *
     * @param button the enum for the button
     */
    void onButtonClicked(JoyPanelUIEnum button);

}
