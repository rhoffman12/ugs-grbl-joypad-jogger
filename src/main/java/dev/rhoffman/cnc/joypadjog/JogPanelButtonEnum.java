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
 * The buttons that can trigger events in the {@link JogPanel}
 *
 * @author Joacim Breiler
 */
public enum JogPanelButtonEnum {
    BUTTON_XPOS,
    BUTTON_XNEG,
    BUTTON_YPOS,
    BUTTON_YNEG,
    BUTTON_ZPOS,
    BUTTON_ZNEG,
    BUTTON_DIAG_XNEG_YNEG,
    BUTTON_DIAG_XNEG_YPOS,
    BUTTON_DIAG_XPOS_YNEG,
    BUTTON_DIAG_XPOS_YPOS,
    BUTTON_TOGGLE_UNIT
}
