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

import com.willwinder.ugs.nbp.lib.lookup.CentralLookup;
import com.willwinder.ugs.nbp.lib.services.LocalizingService;
import com.willwinder.universalgcodesender.listeners.ControllerListener;
import com.willwinder.universalgcodesender.listeners.ControllerStatus;
import com.willwinder.universalgcodesender.listeners.UGSEventListener;
import com.willwinder.universalgcodesender.model.Alarm;
import com.willwinder.universalgcodesender.model.BackendAPI;
import com.willwinder.universalgcodesender.model.Position;
import com.willwinder.universalgcodesender.model.UGSEvent;
import com.willwinder.universalgcodesender.model.UnitUtils;
import com.willwinder.universalgcodesender.services.JogService;
import com.willwinder.universalgcodesender.types.GcodeCommand;
import com.willwinder.universalgcodesender.utils.SwingHelpers;
//import dev.rhoffman.gamepadevents.*;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.windows.TopComponent;

import java.awt.*;
import org.openide.util.Lookup;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import javax.swing.JPopupMenu;

/**
 * The joypad jog control panel
 *
 * @author Ryan Hoffman
 */
@TopComponent.Description(
        preferredID = "JoyTopComponent"
)
@TopComponent.Registration(
        mode = "middle_left",
        openAtStartup = true)
@ActionID(
        category = JoyTopComponent.CATEGORY,
        id = JoyTopComponent.ACTION_ID)
@ActionReference(
        path = JoyTopComponent.WINOW_PATH)
@TopComponent.OpenActionRegistration(
        displayName = "Joypad Jog Controller",
        preferredID = "JoyTopComponent"
)
public final class JoyTopComponent extends TopComponent
        implements UGSEventListener, ControllerListener { //, JoyPanelListener, GamepadEventListener {

    public static final String WINOW_PATH = LocalizingService.MENU_WINDOW_PLUGIN;
    public static final String CATEGORY = LocalizingService.CATEGORY_WINDOW;
    public static final String ACTION_ID = "dev.rhoffman.cnc.joypadjog.JoyTopComponent";

    private final BackendAPI backend;
    private final JoyPanel joyPanel;
    private final JogService jogService;
    private static final ScheduledExecutorService EXECUTOR_SERVICE = Executors.newSingleThreadScheduledExecutor();
//    private GamepadMonitor gamepads;

    public JoyTopComponent() {
        backend = CentralLookup.getDefault().lookup(BackendAPI.class);
        jogService = CentralLookup.getDefault().lookup(JogService.class);
//        gamepadInit();

        joyPanel = new JoyPanel();
        joyPanel.setEnabled(jogService.canJog());
        joyPanel.setFeedRate(Double.valueOf(jogService.getFeedRate()).intValue());
        joyPanel.setStepSizeXY(jogService.getStepSizeXY());
        joyPanel.setStepSizeZ(jogService.getStepSizeZ());
        joyPanel.setUnit(jogService.getUnits());
        joyPanel.setUseStepSizeZ(jogService.useStepSizeZ());
//        joyPanel.addListener(this);
        
        backend.addUGSEventListener(this);
        backend.addControllerListener(this);

        setLayout(new BorderLayout());
        setName(LocalizingService.JogControlTitle);
        setToolTipText(LocalizingService.JogControlTooltip);

        setPreferredSize(new Dimension(250, 270));

        add(joyPanel, BorderLayout.CENTER);

    }

//    private void gamepadInit() {
//        if (gamepads!=null) gamepadShutdown();
//        gamepads = new GamepadMonitor(); // TODO: configuration options
//    }
//
//    private void gamepadShutdown() {
//        if (gamepads!=null) gamepads.shutdown();
//    }

    @Override
    protected void componentClosed() {
        super.componentClosed();
//        gamepadShutdown();
        backend.removeUGSEventListener(this);
        backend.removeControllerListener(this);
    }

    @Override
    public void UGSEvent(UGSEvent event) {
        boolean canJog = jogService.canJog();
        if (canJog != joyPanel.isEnabled()) {
            joyPanel.setEnabled(canJog);
        }

//        if (event.isSettingChangeEvent()) {
//            joyPanel.setFeedRate(Double.valueOf(backend.getSettings().getJogFeedRate()).intValue());
//            joyPanel.setStepSizeXY(backend.getSettings().getManualModeStepSize());
//            joyPanel.setStepSizeZ(backend.getSettings().getzJogStepSize());
//            joyPanel.setUnit(backend.getSettings().getPreferredUnits());
//            joyPanel.setUseStepSizeZ(backend.getSettings().useZStepSize());
//        }
    }

    @Override
    public void controlStateChange(UGSEvent.ControlState state) {
    }

    @Override
    public void fileStreamComplete(String filename, boolean success) {

    }

    @Override
    public void receivedAlarm(Alarm alarm) {

    }

    @Override
    public void commandSkipped(GcodeCommand command) {

    }

    @Override
    public void commandSent(GcodeCommand command) {

    }

    @Override
    public void commandComplete(GcodeCommand command) {
        // If there is a command with an error, assume we are jogging and cancel any event
        if (command.isError()) {
            jogService.cancelJog();
        }
    }

    @Override
    public void commandComment(String comment) {

    }

    @Override
    public void probeCoordinates(Position p) {

    }

    @Override
    public void statusStringListener(ControllerStatus status) {

    }

//    @Override
//    public void onButtonClicked(JoyPanelButtonEnum button) {
//        switch (button) {
//            case BUTTON_XNEG:
//                jogService.adjustManualLocationXY(-1, 0);
//                break;
//            case BUTTON_XPOS:
//                jogService.adjustManualLocationXY(1, 0);
//                break;
//            case BUTTON_YNEG:
//                jogService.adjustManualLocationXY(0, -1);
//                break;
//            case BUTTON_YPOS:
//                jogService.adjustManualLocationXY(0, 1);
//                break;
//            case BUTTON_DIAG_XNEG_YNEG:
//                jogService.adjustManualLocationXY(-1, -1);
//                break;
//            case BUTTON_DIAG_XNEG_YPOS:
//                jogService.adjustManualLocationXY(-1, 1);
//                break;
//            case BUTTON_DIAG_XPOS_YNEG:
//                jogService.adjustManualLocationXY(1, -1);
//                break;
//            case BUTTON_DIAG_XPOS_YPOS:
//                jogService.adjustManualLocationXY(1, 1);
//                break;
//            case BUTTON_ZNEG:
//                jogService.adjustManualLocationZ(-1);
//                break;
//            case BUTTON_ZPOS:
//                jogService.adjustManualLocationZ(1);
//                break;
//            case BUTTON_TOGGLE_UNIT:
//                if (jogService.getUnits() == UnitUtils.Units.MM) {
//                    jogService.setUnits(UnitUtils.Units.INCH);
//                } else {
//                    jogService.setUnits(UnitUtils.Units.MM);
//                }
//                break;
//            default:
//        }
//    }
//
//    @Override
//    public void onStepSizeZChanged(double value) {
//        jogService.setStepSizeZ(value);
//    }
//
//    @Override
//    public void onStepSizeXYChanged(double value) {
//        jogService.setStepSizeXY(value);
//    }
//
//    @Override
//    public void onFeedRateChanged(int value) {
//        jogService.setFeedRate(value);
//    }

//    @Override
//    public void handleButtonEvent(ButtonEvent buttonEvent) {
//
//    }
//
//    @Override
//    public void handleStickEvent(StickEvent stickEvent) {
//
//    }
}
